package com.besscroft.xanadu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.common.param.storage.StorageAddParam;
import com.besscroft.xanadu.common.param.storage.StorageUpdateParam;
import com.besscroft.xanadu.common.vo.StorageInfoVo;

import java.util.List;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/18 21:12
 */
public interface StorageService extends IService<Storage> {

    /**
     * 驱动分页列表
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param type 驱动类型
     * @return 驱动分页列表数据
     */
    List<Storage> storagePage(Integer pageNum, Integer pageSize, Integer type);

    /**
     * 删除驱动
     * @param storageId 驱动 id
     */
    void deleteStorage(Long storageId);

    /**
     * 新增驱动
     * @param param 请求参数
     */
    void addStorage(StorageAddParam param);

    /**
     * 更新驱动
     * @param param 请求参数
     */
    void updateStorage(StorageUpdateParam param);

    /**
     * 获取存储配置详情
     * @param storageId 存储id
     * @return 存储配置详情
     */
    StorageInfoVo getInfo(Long storageId);

}
