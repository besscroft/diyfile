package com.besscroft.diyfile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.constant.SystemConstants;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.SystemConfig;
import com.besscroft.diyfile.common.entity.User;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.mapper.SystemConfigMapper;
import com.besscroft.diyfile.mapper.UserMapper;
import com.besscroft.diyfile.service.StorageService;
import com.besscroft.diyfile.service.SystemConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    private final UserMapper userMapper;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

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
        if (CollectionUtils.isEmpty(configList)) {
            return new HashMap<>();
        }
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
            CacheConstants.BARK_ID,
            CacheConstants.BACK_STATUS
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

    @Override
    @Cacheable(value = CacheConstants.BACK_STATUS, unless = "#result == null")
    public Integer getBarkStatus() {
        return StrUtil.equals(this.baseMapper.queryByConfigKey("barkStatus").getConfigValue(), "1") ? 1 : 0;
    }

    @Override
    @Cacheable(value = CacheConstants.STATISTICS, unless = "#result == null")
    public Map<String, Object> getTotalInfo() {
        Map<String, Object> map = new HashMap<>();
        List<User> userList = userMapper.selectList(null);
        map.put("userCount", Optional.ofNullable(userList.size()).orElse(0));
        map.put("userDisableCount", Optional.ofNullable(userList.stream().filter(user -> Objects.equals(user.getStatus(), SystemConstants.STATUS_NO)).count()).orElse(0L));
        List<Storage> storageList = storageService.list();
        map.put("storageCount", Optional.ofNullable(storageList.size()).orElse(0));
        map.put("storageActiveCount", Optional.ofNullable(storageList.stream().filter(storage -> Objects.equals(storage.getEnable(), SystemConstants.STATUS_OK)).count()).orElse(0L));
        return map;
    }

    @Override
    public String getBackupJsonString() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        List<SystemConfig> list = this.list();
        map.put("systemConfig", list);
        List<StorageInfoVo> info = storageService.getAllInfo();
        map.put("storageInfo", info);
        return objectMapper.writeValueAsString(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreData(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Map<String, Object> map = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            if (map.containsKey("systemConfig")) {
                List<SystemConfig> list = objectMapper.convertValue(map.get("systemConfig"), new TypeReference<>() {
                });
                this.saveOrUpdateBatch(list);
            }
            if (map.containsKey("storageInfo")) {
                List<StorageInfoVo> list = objectMapper.convertValue(map.get("storageInfo"), new TypeReference<>() {
                });
                list.forEach(storage -> storage.setEnable(SystemConstants.STATUS_NO));
                storageService.saveStorageInfoVoList(list);
            }
        } catch (IOException e) {
            log.error("还原数据异常:{}", e);
            throw new DiyFileException("还原数据失败！");
        }
    }

}
