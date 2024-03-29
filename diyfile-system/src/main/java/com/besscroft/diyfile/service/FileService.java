package com.besscroft.diyfile.service;

import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @Description 文件服务
 * @Author Bess Croft
 * @Date 2023/1/22 20:29
 */
public interface FileService {

    /**
     * 获取默认驱动信息
     * @return 驱动信息
     */
    StorageInfoVo defaultStorage();

    /**
     * 获取默认文件列表
     * @return 文件列表
     */
    List<FileInfoVo> defaultItem();

    /**
     * 获取文件列表
     * @param storageId 存储 id
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    List<FileInfoVo> getItem(Long storageId, String folderPath);

    /**
     * 获取文件列表
     * @param storageKey 存储 key
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    List<FileInfoVo> getItemByKey(String storageKey, String folderPath);

    /**
     * 获取文件信息
     * @param storageId 存储 id
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return 文件信息
     */
    FileInfoVo getFileInfo(Long storageId, String filePath, String fileName);

    /**
     * 获取文件信息
     * @param storageKey 存储 key
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return 文件信息
     */
    FileInfoVo getFileInfo(String storageKey, String filePath, String fileName);

    /**
     * 获取文件上传地址
     * @param storageKey 存储 key
     * @param path 文件/夹路径
     * @return 文件上传地址
     */
    String getUploadUrl(String storageKey, String path);

    /**
     * 删除文件
     * @param storageKey 存储 key
     * @param filePath 文件路径
     */
    void deleteFile(String storageKey, String filePath);

    /**
     * 获取文件下载地址
     */
    String getDownloadUrl(String storageKey, String filePath, String fullPath);

    /**
     * 获取下载文件流
     */
    ResponseEntity<Resource> getDownloadFile(String storageKey, String filePath);

}
