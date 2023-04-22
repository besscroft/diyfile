package com.besscroft.diyfile.storage.service.base;

import com.besscroft.diyfile.common.vo.FileInfoVo;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @Description 文件基础服务接口
 * @Author Bess Croft
 * @Date 2023/1/20 11:16
 */
public interface FileBaseService {

    /**
     * 获取文件下载地址
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param fullPath 文件全路径
     * @return 文件下载地址
     */
    String getFileDownloadUrl(String fileName, String filePath, String fullPath);

    /**
     * 获取文件资源
     * @param fileName 文件名
     * @param filePath 文件路径
     * @return 文件资源
     */
    ResponseEntity<Resource> getFileResource(String fileName, String filePath);

    /**
     * 获取文件列表
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    List<FileInfoVo> getFileList(String folderPath);

    /**
     * 获取存储类型
     * @return 存储类型
     */
    Integer getStorageType();

}
