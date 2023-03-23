package com.besscroft.diyfile.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.besscroft.diyfile.common.exception.DiyFileException;

/**
 * @Description 路径处理工具类
 * @Author Bess Croft
 * @Date 2023/3/14 17:38
 */
public class PathUtils {

    /**
     * 去除路径前面的斜杠
     * @param path 路径
     * @return 去除斜杠后的路径
     */
    public static String removeLeadingSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }

    /**
     * 去掉路径最后一个斜杠
     * @param path 路径
     * @return 去除斜杠后的路径
     */
    public static String removeTrailingSlash(String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    /**
     * 路径解码
     */
    public static String decode(String path) {
        return URLUtil.decode(path);
    }

    /**
     * 异常路径处理
     */
    public static void checkPath(String path) {
        if (StrUtil.containsAll(path, "../", "./")) {
            throw new DiyFileException("路径不合法！");
        }
    }

    /**
     * 判断路径为文件还是文件夹
     * @param path 路径
     * @return true 文件夹 false 文件
     */
    public static boolean isFolder(String path) {
        // 不包含扩展名的 . 肯定为文件夹
        if (!StrUtil.contains(path, ".") || StrUtil.equals(path, "/")) return true;
        int lastSlashIndex = path.lastIndexOf('/');
        String realPath = path.substring(lastSlashIndex + 1);
        return !realPath.contains(".");
    }

}
