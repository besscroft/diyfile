package com.besscroft.diyfile.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.besscroft.diyfile.common.exception.DiyFileException;

import java.util.Objects;

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
        if (!StrUtil.contains(path, ".") || StrUtil.equals(path, "/")) {
            return true;
        }
        int lastSlashIndex = path.lastIndexOf('/');
        String realPath = path.substring(lastSlashIndex + 1);
        return !realPath.contains(".");
    }

    /**
     * 获取路径中的文件名，同时兼容 Windows 和 Linux
     * @param path 路径
     * @return 文件名
     */
    public static String getFileName(String path) {
        if (StrUtil.contains(path, "\\")) {
            return StrUtil.subAfter(path, "\\", true);
        }
        if (StrUtil.contains(path, "/")) {
            return StrUtil.subAfter(path, "/", true);
        }
        return path;
    }

    /**
     * 文件/夹路径处理
     * @param path 文件/夹路径
     * @return 处理后的文件/夹路径
     */
    public static String handlePath(String mountPath, String path) {
        // 如果设定的挂载路径为 "/"
        if (Objects.equals("/", mountPath)) {
            // 如果传入的挂载路径为空，则使用默认挂载路径
            if (StrUtil.isBlank(path)) {
                return mountPath;
            } else if (!Objects.equals("/", path)) {
                // 如果传入的挂载路径不为空，且不是 "/"
                if (Objects.equals("/", mountPath)) {
                    // 如果设定的挂载路径为 "/"，传入的挂载路径不为空，且不是 "/"，则使用传入的挂载路径
                    return path;
                } else {
                    return mountPath + path;
                }
            } else {
                // 如果传入的挂载路径不为空，且是 "/"，则使用默认挂载路径
                return mountPath;
            }
        } else {
            // 如果设定的挂载路径不是 "/"
            // 如果传入的挂载路径为空，则使用默认挂载路径
            if (StrUtil.isBlank(path)) {
                return mountPath;
            } else if (!Objects.equals("/", path)) {
                // 如果传入的挂载路径不为空，且不是 "/"
                return mountPath + path;
            } else {
                // 如果传入的挂载路径不为空，且是 "/"，则使用默认挂载路径
                return mountPath;
            }
        }
    }

}
