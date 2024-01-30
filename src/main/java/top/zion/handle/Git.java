package top.zion.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import top.zion.util.CommandUtil;
import top.zion.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author zion
 * @since 2024/1/17 10:10:49
 */
public class Git {
    public static void handle(String zipPath, Object git) {
        if (git instanceof HashMap<?,?>) {
            HashMap<String, String> gits = (HashMap<String, String>) git;
            if (MapUtil.isNotEmpty(gits)) {
                gits.forEach((name, gitUrl) -> {
                    try {
                        String localDirectory = zipPath + File.separator + "git" + File.separator + name;
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
}
