package com.besscroft.xanadu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.entity.StorageConfig;
import com.besscroft.xanadu.mapper.StorageConfigMapper;
import com.besscroft.xanadu.service.StorageConfigService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/18 21:14
 */
@Service
public class StorageConfigServiceImpl extends ServiceImpl<StorageConfigMapper, StorageConfig> implements StorageConfigService {
}
