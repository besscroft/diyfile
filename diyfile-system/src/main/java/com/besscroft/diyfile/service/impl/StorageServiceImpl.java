package com.besscroft.diyfile.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.constant.SystemConstants;
import com.besscroft.diyfile.common.converter.StorageConverterMapper;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.StorageAddParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateParam;
import com.besscroft.diyfile.common.util.ParamUtils;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.mapper.StorageConfigMapper;
import com.besscroft.diyfile.mapper.StorageMapper;
import com.besscroft.diyfile.service.StorageConfigService;
import com.besscroft.diyfile.service.StorageService;
import com.besscroft.diyfile.storage.context.StorageApplicationContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description 存储服务实现类
 * @Author Bess Croft
 * @Date 2022/12/18 21:13
 */
@Service
@RequiredArgsConstructor
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {

    private final StorageConfigService storageConfigService;
    private final StorageConfigMapper storageConfigMapper;
    private final StorageApplicationContext storageApplicationContext;
    private final Cache<String, Object> caffeineCache;

    @Override
    public List<Storage> storagePage(Integer pageNum, Integer pageSize, Integer type) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage(type);
    }

    @Override
    public void deleteStorage(Long storageId) {
        caffeineCache.invalidate(CacheConstants.DEFAULT_STORAGE);
        caffeineCache.invalidate(CacheConstants.ENABLE_STORAGE);
        Assert.isTrue(this.baseMapper.deleteById(storageId) > 0, "存储删除失败！");
        Assert.isTrue(storageConfigMapper.deleteByStorageId(storageId) > 0, "存储删除失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStorage(StorageAddParam param) {
        Storage storage = StorageConverterMapper.INSTANCE.AddParamToStorage(param);
        this.baseMapper.insert(storage);
        param.getConfigList().forEach(storageConfig -> storageConfig.setStorageId(storage.getId()));
        storageConfigService.saveBatch(param.getConfigList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStorage(StorageUpdateParam param) {
        Storage storage = StorageConverterMapper.INSTANCE.UpdateParamToStorage(param);
        Storage oldStorage = this.baseMapper.selectById(storage.getId());
        if (!Objects.equals(storage.getType(), oldStorage.getType()))
            throw new DiyFileException("存储类型不允许修改！");
        storage.setStorageKey(oldStorage.getStorageKey());
        // 移除缓存
        caffeineCache.invalidate(CacheConstants.DEFAULT_STORAGE);
        this.baseMapper.updateById(storage);
        storageConfigService.updateBatchById(param.getConfigList());
    }

    @Override
    public StorageInfoVo getInfo(Long storageId) {
        Storage storage = this.baseMapper.selectById(storageId);
        StorageInfoVo vo = StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
        // 配置信息查询
        List<StorageConfig> configList = storageConfigMapper.selectByStorageId(storageId);
        for (StorageConfig config: configList) {
            if (Objects.equals(config.getConfigKey(), "client_secret")) {
                config.setConfigValue(StrUtil.sub(config.getConfigValue(), 0, 15) + "***");
            }
            if (Objects.equals(config.getConfigKey(), "refresh_token")) {
                config.setConfigValue(StrUtil.sub(config.getConfigValue(), 0, 15) + "***");
            }
        }
        vo.setConfigList(configList);
        return vo;
    }

    @Override
    public void updateStatus(Long storageId, Integer status) {
        Storage storage = this.baseMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        storage.setEnable(status);
        if (Objects.equals(status, SystemConstants.STATUS_OK)) {
            try {
                storageApplicationContext.init(storage);
            } catch (Exception e) {
                throw new DiyFileException("启用存储失败！");
            }
        } else {
            try {
                storageApplicationContext.destroy(storage.getId());
            } catch (Exception e) {
                throw new DiyFileException("停用存储失败！");
            }
        }
        Assert.isTrue(this.baseMapper.updateById(storage) > 0, "更新状态失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long storageId) {
        Storage storage = this.baseMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        this.baseMapper.updateDefaultByNo();
        storage.setDefaultStatus(SystemConstants.STATUS_OK);
        // 移除缓存
        caffeineCache.invalidate(CacheConstants.DEFAULT_STORAGE);
        Assert.isTrue(this.baseMapper.updateById(storage) > 0, "设置默认存储失败！");
    }

    @Override
    public FileInitParam getFileInitParam(Long storageId) {
        Storage storage = this.baseMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        List<StorageConfig> configList = storageConfigMapper.selectByStorageId(storageId);
        return ParamUtils.getFileInitParam(storage, configList);
    }

    @Override
    @Cacheable(cacheNames = "storageKey" ,key = "#storageKey", unless = "#result == null")
    public Long getStorageIdByStorageKey(String storageKey) {
        return this.baseMapper.selectIdByStorageKey(storageKey);
    }

    @Override
    public List<Storage> getEnableStorage() {
        return (List<Storage>) Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.ENABLE_STORAGE))
                .orElseGet(() -> {
                    List<Storage> storageList = this.baseMapper.selectAllByEnable();
                    caffeineCache.put(CacheConstants.ENABLE_STORAGE, storageList);
                    return storageList;
                });
    }

    @Override
    public Long getDefaultStorageId() {
        StorageInfoVo infoVo = (StorageInfoVo) Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.DEFAULT_STORAGE))
                .orElseGet(() -> {
                    Storage storage = this.baseMapper.selectByDefault();
                    if (Objects.isNull(storage)) return new StorageInfoVo();
                    StorageInfoVo vo = StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
                    caffeineCache.put(CacheConstants.DEFAULT_STORAGE, vo);
                    return vo;
                });
        return infoVo.getId();
    }

}
