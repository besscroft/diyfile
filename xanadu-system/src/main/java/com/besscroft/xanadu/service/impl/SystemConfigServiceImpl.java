package com.besscroft.xanadu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.entity.SystemConfig;
import com.besscroft.xanadu.mapper.SystemConfigMapper;
import com.besscroft.xanadu.service.SystemConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2023/1/7 19:09
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Override
    public List<SystemConfig> getConfig() {
        return this.list();
    }

    @Override
    public String getSiteTitle() {
        return this.baseMapper.queryByConfigKey("title").getConfigValue();
    }

    @Override
    public Map<String, String> getSiteConfig() {
        List<SystemConfig> configList = this.baseMapper.queryAllByType(1);
        if (CollectionUtils.isEmpty(configList)) return new HashMap<>();
        return configList.stream().collect(Collectors.toMap(SystemConfig::getConfigKey, SystemConfig::getConfigValue));
    }

}
