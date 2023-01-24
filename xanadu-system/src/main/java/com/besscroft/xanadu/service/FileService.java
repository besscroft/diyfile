package com.besscroft.xanadu.service;

import com.besscroft.xanadu.common.vo.FileInfoVo;

import java.util.List;

/**
 * @Description 文件服务
 * @Author Bess Croft
 * @Date 2023/1/22 20:29
 */
public interface FileService {

    /**
     * 获取默认文件列表
     * @return 文件列表
     */
    List<FileInfoVo> defaultItem();

    /**
     * 获取文件列表
     * @param storageId 存储 id
     * @param folderPath 文件夹路径
     * @return
     */
    List<FileInfoVo> getItem(Long storageId, String folderPath);

}
