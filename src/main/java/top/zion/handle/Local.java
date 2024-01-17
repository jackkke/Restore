package top.zion.handle;

import cn.hutool.core.collection.CollUtil;
import top.zion.util.ZipUtil;

import java.util.List;

/**
 * @author zion
 * @since 2024/1/17 10:13:05
 */
public class Local {
    public static void handle(String zipPath, Object path) {
        List<String> paths = (List<String>) path;
        if (CollUtil.isNotEmpty(paths)) {
            paths.forEach(localPath -> ZipUtil.zipPath(zipPath, localPath, false));
        }
    }
}
