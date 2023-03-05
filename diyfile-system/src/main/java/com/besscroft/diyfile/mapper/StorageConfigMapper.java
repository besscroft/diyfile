package com.besscroft.diyfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.diyfile.common.entity.StorageConfig;

import java.util.List;

/**
 * @Description 存储配置 Mapper 接口
 * @Author Bess Croft
 * @Date 2022/12/18 21:10
 */
public interface StorageConfigMapper extends BaseMapper<StorageConfig> {

    /**
     * 根据存储 id 删除存储配置
     * @param storageId 存储 id
     */
    int deleteByStorageId(Long storageId);

    /**
     * 根据存储 id 获取存储配置
     * @param storageId 存储 id
     * @return 存储配置
     */
    List<StorageConfig> selectByStorageId(Long storageId);

    /**
     * 根据存储 id 和 key 获取存储配置
     * @param storageId 存储 id
     * @param key 存储配置 key
     * @return 存储配置 value
     */
    String selectValueByStorageIdAndKey(Long storageId, String key);

}
