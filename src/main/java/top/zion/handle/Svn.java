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
 * @since 2024/1/17 09:53:23
 */
public class Svn {

    public static void handle(String zipPath, Object svn) {
        if (svn instanceof HashMap<?,?>) {
            HashMap<String, String> svns = (HashMap<String, String>) svn;
            if (MapUtil.isNotEmpty(svns)) {
                svns.forEach((name, svnUrl) -> {
                    try {
                        String targetDirectory = zipPath + File.separator + "svn" + File.separator + name;
                        int exitCode = CommandUtil.doCmd(String.format("svn checkout %s %s", svnUrl, targetDirectory));
                        System.out.printf("SVN %s Checkout end, exitCode is %s!%n", svnUrl, exitCode);
                    } catch (IOException | InterruptedException e) {
                        System.err.printf("SVN Checkout error : %s%n", e.getMessage());
                    }
                });
                ZipUtil.zipPath(zipPath, zipPath + File.separator + "svn", true);
            }
        }
    }
}
