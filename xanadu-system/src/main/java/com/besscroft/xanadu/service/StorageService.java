package com.besscroft.xanadu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.common.param.FileInitParam;
import com.besscroft.xanadu.common.param.storage.StorageAddParam;
import com.besscroft.xanadu.common.param.storage.StorageUpdateParam;
import com.besscroft.xanadu.common.vo.StorageInfoVo;

import java.util.List;

/**
 * @Description 存储服务
 * @Author Bess Croft
 * @Date 2022/12/18 21:12
 */
public interface StorageService extends IService<Storage> {

    /**
     * 存储分页列表
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param type 存储类型
     * @return 存储分页列表数据
     */
    List<Storage> storagePage(Integer pageNum, Integer pageSize, Integer type);

    /**
     * 删除存储
     * @param storageId 存储 id
     */
    void deleteStorage(Long storageId);

    /**
     * 新增存储
     * @param param 请求参数
     */
    void addStorage(StorageAddParam param);

    /**
     * 更新存储
     * @param param 请求参数
     */
    void updateStorage(StorageUpdateParam param);

    /**
     * 获取存储配置详情
     * @param storageId 存储 id
     * @return 存储配置详情
     */
    StorageInfoVo getInfo(Long storageId);

    /**
     * 更新存储启用状态
     * @param storageId 存储 id
     * @param status 启用状态
     */
    void updateStatus(Long storageId, Integer status);

    /**
     * 获取存储服务配置参数
     * @param storageId 存储 id
     * @return 存储服务配置参数
     */
    FileInitParam getFileInitParam(Long storageId);

    /**
     * 获取默认存储 id
     * @param storageKey 存储 key
     * @return 存储 id
     */
    Long getStorageIdByStorageKey(String storageKey);

}
