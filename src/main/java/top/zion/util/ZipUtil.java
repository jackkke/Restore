package top.zion.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.DesensitizedUtil;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author zion
 * @since 2024/1/17 10:05:24
 */
public class ZipUtil {
    public static void zipPath(String zipPath, String path, boolean delSource) {
        Path sourcePath = Paths.get(path);
        String zipFilePath = Paths.get(zipPath) + File.separator + sourcePath.getFileName() + ".zip";
        try (java.util.zip.ZipOutputStream zipOutputStream = new java.util.zip.ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)));
             Stream<Path> walk = Files.walk(sourcePath)) {
            walk.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try {
                            String entryName = sourcePath.relativize(filePath).toString();
                            ZipEntry zipEntry = new ZipEntry(entryName);
                            zipEntry.setMethod(ZipEntry.DEFLATED);
                            zipOutputStream.putNextEntry(zipEntry);
                            FileUtil.writeToStream(filePath.toFile(), zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.printf("zip %s error : %s%n", filePath, e.getMessage());
                        }
                    });
            System.out.printf("zip %s successful!%n", zipFilePath);
        } catch (IOException e) {
            System.err.printf("zip error : %s%n", e.getMessage());
        }
        if (delSource) FileUtil.del(path);
    }

    public static void zip(String zipPath, String password) {
        Path sourcePath = Paths.get(zipPath);
        String zipFilePath = sourcePath.getParent() + File.separator + sourcePath.getFileName() + ".zip";
        System.out.printf("zip %s started, pswd is %s%n", zipPath, DesensitizedUtil.firstMask(password));
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)), password.toCharArray());
             Stream<Path> walk = Files.walk(sourcePath)) {
            zipOutputStream.setComment("create by zion!");
            walk.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try {
                            String entryName = sourcePath.relativize(filePath).toString();
                            ZipParameters parameters = new ZipParameters();
                            parameters.setEncryptFiles(true);
                            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
                            parameters.setCompressionLevel(CompressionLevel.ULTRA);
                            parameters.setEncryptionMethod(EncryptionMethod.AES);
                            parameters.setFileNameInZip(entryName);
                            zipOutputStream.putNextEntry(parameters);
                            FileUtil.writeToStream(filePath.toFile(), zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.printf("zip %s error : %s%n", filePath, e.getMessage());
                        }
                    });
            System.out.printf("zip %s successful!%n", zipFilePath);
        } catch (IOException e) {
            System.err.printf("zip error : %s%n", e.getMessage());
        }
        FileUtil.del(zipPath);
    }
}
