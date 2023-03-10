package com.besscroft.diyfile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.mapper.StorageConfigMapper;
import com.besscroft.diyfile.service.StorageConfigService;
import org.springframework.stereotype.Service;

/**
 * @Description 存储配置服务实现类
 * @Author Bess Croft
 * @Date 2022/12/18 21:14
 */
@Service
public class StorageConfigServiceImpl extends ServiceImpl<StorageConfigMapper, StorageConfig> implements StorageConfigService {
}
