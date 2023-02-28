package com.besscroft.diyfile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.entity.SystemConfig;
import com.besscroft.diyfile.mapper.SystemConfigMapper;
import com.besscroft.diyfile.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 系统配置服务实现类
 * @Author Bess Croft
 * @Date 2023/1/7 19:09
 */
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Override
    @Cacheable(value = CacheConstants.SYSTEM_CONFIG, unless = "#result == null")
    public List<SystemConfig> getConfig() {
        return this.list();
    }

    @Override
    @Cacheable(value = CacheConstants.SITE_TITLE, unless = "#result == null")
    public String getSiteTitle() {
        return this.baseMapper.queryByConfigKey("title").getConfigValue();
    }

    @Override
    @Cacheable(value = CacheConstants.SITE_CONFIG, unless = "#result == null")
    public Map<String, String> getSiteConfig() {
        List<SystemConfig> configList = this.baseMapper.queryAllByType(1);
        if (CollectionUtils.isEmpty(configList)) return new HashMap<>();
        return configList.stream().collect(Collectors.toMap(SystemConfig::getConfigKey, SystemConfig::getConfigValue));
    }

    @Override
    @Cacheable(value = CacheConstants.SITE_BEIAN, unless = "#result == null")
    public String getBeian() {
        return this.baseMapper.queryByConfigKey("beian").getConfigValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {
            CacheConstants.SYSTEM_CONFIG,
            CacheConstants.SITE_TITLE,
            CacheConstants.SITE_CONFIG,
            CacheConstants.SITE_BEIAN,
            CacheConstants.BARK_ID
    }, allEntries = true)
    public void updateConfig(String configKey, String configValue) {
        long userId = StpUtil.getLoginIdAsLong();
        Assert.isTrue(this.baseMapper.updateConfig(configKey, configValue, userId) > 0, "更新失败！");
    }

    @Override
    @Cacheable(value = CacheConstants.BARK_ID, unless = "#result == null")
    public String getBarkId() {
        return this.baseMapper.queryByConfigKey("barkId").getConfigValue();
    }

}
