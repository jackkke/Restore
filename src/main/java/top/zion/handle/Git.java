package top.zion.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import top.zion.util.CommandUtil;
import top.zion.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author zion
 * @since 2024/1/17 10:10:49
 */
public class Git {
    public static void handle(String zipPath, Object git) {
        List<String> gits = (List<String>) git;
        if (CollUtil.isNotEmpty(gits)) {
            gits.forEach(gitUrl -> {
                try {
                    String localDirectory = zipPath + File.separator + "git" + File.separator + StrUtil.subAfter(gitUrl, "/", true).replace(".git", "");
                    int exitCode = CommandUtil.doCmd(String.format("git clone %s %s", gitUrl, localDirectory));
                    System.out.printf("Git %s Clone end, exitCode is %s!%n", gitUrl, exitCode);
                } catch (IOException | InterruptedException e) {
                    System.err.printf("Git Clone error : %s%n", e.getMessage());
                }
            });
            ZipUtil.zipPath(zipPath, zipPath + File.separator + "git", true);
        }
    }
}
