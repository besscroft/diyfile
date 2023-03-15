package com.besscroft.diyfile.common.util;

import cn.hutool.core.util.URLUtil;

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

}
