package com.besscroft.diyfile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.entity.SystemConfig;
import com.besscroft.diyfile.mapper.SystemConfigMapper;
import com.besscroft.diyfile.service.SystemConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
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

    private final Cache<String, Object> caffeineCache;

    @Override
    public List<SystemConfig> getConfig() {
        return (List<SystemConfig>) Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.SYSTEM_CONFIG))
                .orElseGet(() -> {
                    List<SystemConfig> configList = this.list();
                    caffeineCache.put(CacheConstants.SYSTEM_CONFIG, configList);
                    return configList;
                });
    }

    @Override
    public String getSiteTitle() {
        return Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.SITE_TITLE))
                .orElseGet(() -> {
                    String title = this.baseMapper.queryByConfigKey("title").getConfigValue();
                    caffeineCache.put(CacheConstants.SITE_TITLE, title);
                    return title;
                }).toString();
    }

    @Override
    public Map<String, String> getSiteConfig() {
        return (Map<String, String>) Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.SITE_CONFIG))
                .orElseGet(() -> {
                    List<SystemConfig> configList = this.baseMapper.queryAllByType(1);
                    if (CollectionUtils.isEmpty(configList)) return new HashMap<>();
                    Map<String, String> map = configList.stream().collect(Collectors.toMap(SystemConfig::getConfigKey, SystemConfig::getConfigValue));
                    caffeineCache.put(CacheConstants.SITE_CONFIG, map);
                    return map;
                });
    }

    @Override
    public String getBeian() {
        return Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.SITE_BEIAN))
                .orElseGet(() -> {
                    String beian = this.baseMapper.queryByConfigKey("beian").getConfigValue();
                    caffeineCache.put(CacheConstants.SITE_BEIAN, beian);
                    return beian;
                }).toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(String configKey, String configValue) {
        long userId = StpUtil.getLoginIdAsLong();
        // 清除缓存
        Set<String> keys = new HashSet<>();
        keys.add(CacheConstants.SYSTEM_CONFIG);
        keys.add(CacheConstants.SITE_TITLE);
        keys.add(CacheConstants.SITE_CONFIG);
        keys.add(CacheConstants.SITE_BEIAN);
        keys.add(CacheConstants.BARK_ID);
        caffeineCache.invalidateAll(keys);
        Assert.isTrue(this.baseMapper.updateConfig(configKey, configValue, userId) > 0, "更新失败！");
    }

    @Override
    public String getBarkId() {
        return Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.BARK_ID))
                .orElseGet(() -> {
                    String barkId = this.baseMapper.queryByConfigKey("barkId").getConfigValue();
                    if (StrUtil.isNotBlank(barkId)) {
                        caffeineCache.put(CacheConstants.BARK_ID, barkId);
                    }
                    return barkId;
                }).toString();
    }

}
