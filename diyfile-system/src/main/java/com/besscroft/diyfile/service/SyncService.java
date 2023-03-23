package com.besscroft.diyfile.service;

/**
 * @Description 存储数据同步服务
 * @Author Bess Croft
 * @Date 2023/3/21 21:43
 */
public interface SyncService {

    /**
     * 添加同步任务
     * @param beforeStorageKey 同步前存储 key
     * @param beforePath 同步前的路径（文件夹或文件相对路径）
     * @param afterStorageKey 同步后存储 key
     * @param afterPath 同步后的路径（文件夹相对路径）
     */
    void taskAdd(String beforeStorageKey, String beforePath, String afterStorageKey, String afterPath);

}
