package top.zion;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import top.zion.handle.Git;
import top.zion.handle.Local;
import top.zion.handle.Svn;
import top.zion.util.ZipUtil;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * @author zion
 * @since 2024/1/10 14:51:52
 */
public class Restore {
    private static final String CURRENT_SECONDS = String.valueOf(DateUtil.currentSeconds());
    public static void main(String[] args) {
        String classPath = Restore.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String fileName = "Restore" + CURRENT_SECONDS;
        String confName = "restore.yml";
        if (System.getProperty("org.graalvm.nativeimage.imagecode") != null) {
            System.out.println("Running as a native image.");
        } else {
            System.out.println("Running on JVM.");
            if (!classPath.endsWith(".jar") || ManagementFactory.getRuntimeMXBean().getInputArguments().stream().anyMatch(o -> o.startsWith("-agentlib:native-image-agent="))) {
                fileName = "../" + fileName;
                confName = "../" + confName;
                if (classPath.endsWith(".jar")) {
                    System.out.println("use agentlib:native-image-agent create config for native");
                }
            }
        }
        String jarPath = new File(classPath).getParent();
        String zipPath = Paths.get(jarPath, fileName).toString();
        File configFile = Paths.get(jarPath, confName).toFile();
        Dict dict = YamlUtil.loadByPath(configFile.getAbsolutePath());
        FileUtil.mkdir(zipPath);
        Svn.handle(zipPath, dict.getOrDefault("svn", null));
        Git.handle(zipPath, dict.getOrDefault("git", null));
        Local.handle(zipPath, dict.getOrDefault("path", null));
        ZipUtil.zip(zipPath, dict.get("password", CURRENT_SECONDS));
    }
}
