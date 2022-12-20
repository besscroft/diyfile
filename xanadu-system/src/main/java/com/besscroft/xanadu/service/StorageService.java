package com.besscroft.xanadu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.Storage;

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
     * @return 驱动分页列表数据
     */
    List<Storage> storagePage(Integer pageNum, Integer pageSize);

    /**
     * 删除驱动
     * @param storageId 驱动 id
     */
    void deleteStorage(Long storageId);

}
