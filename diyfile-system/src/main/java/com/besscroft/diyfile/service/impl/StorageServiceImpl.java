package com.besscroft.diyfile.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.constant.SystemConstants;
import com.besscroft.diyfile.common.converter.StorageConverterMapper;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
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
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import software.amazon.awssdk.regions.Region;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description 存储服务实现类
 * @Author Bess Croft
 * @Date 2022/12/18 21:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {

    private final StorageConfigService storageConfigService;
    private final StorageConfigMapper storageConfigMapper;
    private final StorageApplicationContext storageApplicationContext;

    @Override
    public List<Storage> storagePage(Integer pageNum, Integer pageSize, Integer type) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage(type);
    }

    @Override
    @CacheEvict(value = {
            CacheConstants.DEFAULT_STORAGE,
            CacheConstants.STORAGE_ID,
            CacheConstants.STORAGE_KEY,
            CacheConstants.ENABLE_STORAGE,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void deleteStorage(Long storageId) {
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
    @CacheEvict(value = {
            CacheConstants.DEFAULT_STORAGE,
            CacheConstants.STORAGE_ID,
            CacheConstants.STORAGE_KEY,
            CacheConstants.ENABLE_STORAGE,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void updateStorage(StorageUpdateParam param) {
        Storage storage = StorageConverterMapper.INSTANCE.UpdateParamToStorage(param);
        Storage oldStorage = this.baseMapper.selectById(storage.getId());
        if (!Objects.equals(storage.getType(), oldStorage.getType())) {
            throw new DiyFileException("存储类型不允许修改！");
        }
        storage.setStorageKey(oldStorage.getStorageKey());
        this.baseMapper.updateById(storage);
        // 如果是 OneDrive 存储，需要判断是否包含 ***，如果包含则不更新
        if (Objects.equals(StorageTypeEnum.ONE_DRIVE.getValue(), storage.getType())) {
            param.getConfigList().removeIf(config -> StrUtil.contains(config.getConfigValue(), "***"));
        }
        storageConfigService.updateBatchById(param.getConfigList());
    }

    @Override
    @Cacheable(value = CacheConstants.STORAGE_ID, key = "#storageId", unless = "#result == null")
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
    @Cacheable(value = CacheConstants.STORAGE_KEY, key = "#storageKey", unless = "#result == null")
    public StorageInfoVo getInfoByStorageKey(String storageKey) {
        Storage storage = this.baseMapper.selectByStorageKey(storageKey);
        return StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
    }

    @Override
    public List<StorageInfoVo> getAllInfo() {
        // 使用次数不多，暂时忽略 O(n²) 的步长
        List<Storage> list = this.list();
        List<StorageConfig> configList = storageConfigService.list();
        List<StorageInfoVo> voList = CollUtil.newArrayList();
        for (Storage storage: list) {
            StorageInfoVo vo = StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
            List<StorageConfig> configs = CollUtil.newArrayList();
            for (StorageConfig config: configList) {
                if (Objects.equals(config.getStorageId(), storage.getId())) {
                    configs.add(config);
                }
            }
            vo.setConfigList(configs);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    @CacheEvict(value = {
            CacheConstants.DEFAULT_STORAGE,
            CacheConstants.STORAGE_ID,
            CacheConstants.STORAGE_KEY,
            CacheConstants.ENABLE_STORAGE,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void updateStatus(Long storageId, Integer status) {
        Storage storage = this.baseMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        storage.setEnable(status);
        if (Objects.equals(status, SystemConstants.STATUS_OK)) {
            try {
                storageApplicationContext.init(storage);
            } catch (Exception e) {
                log.error("启用存储失败:{}", e.getMessage());
                throw new DiyFileException("启用存储失败！");
            }
        } else {
            try {
                storageApplicationContext.destroy(storage.getId());
            } catch (Exception e) {
                log.error("停用存储失败:{}", e.getMessage());
                throw new DiyFileException("停用存储失败！");
            }
        }
        Assert.isTrue(this.baseMapper.updateById(storage) > 0, "更新状态失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {
            CacheConstants.DEFAULT_STORAGE,
            CacheConstants.STORAGE_ID,
            CacheConstants.STORAGE_KEY,
            CacheConstants.ENABLE_STORAGE,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void setDefault(Long storageId) {
        Storage storage = this.baseMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        this.baseMapper.updateDefaultByNo();
        storage.setDefaultStatus(SystemConstants.STATUS_OK);
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
    @Cacheable(value = CacheConstants.STORAGE_ID_BY_KEY ,key = "#storageKey", unless = "#result == null")
    public Long getStorageIdByStorageKey(String storageKey) {
        return this.baseMapper.selectIdByStorageKey(storageKey);
    }

    @Override
    @Cacheable(value = CacheConstants.ENABLE_STORAGE, unless = "#result == null")
    public List<Storage> getEnableStorage() {
        return this.baseMapper.selectAllByEnable();
    }

    @Override
    @Cacheable(value = CacheConstants.DEFAULT_STORAGE, unless = "#result == null")
    public Long getDefaultStorageId() {
        Storage storage = this.baseMapper.selectByDefault();
        if (Objects.isNull(storage)) {
            return null;
        }
        StorageInfoVo vo = StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
        return vo.getId();
    }

    @Override
    @CacheEvict(value = {
            CacheConstants.DEFAULT_STORAGE,
            CacheConstants.STORAGE_ID,
            CacheConstants.STORAGE_KEY,
            CacheConstants.ENABLE_STORAGE,
            CacheConstants.STATISTICS
    }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveStorageInfoVoList(List<StorageInfoVo> storageInfoVoList) {
        for (StorageInfoVo storageInfoVo : storageInfoVoList) {
            Storage storage = StorageConverterMapper.INSTANCE.StorageInfoVoToStorage(storageInfoVo);
            if (Objects.isNull(storage)) {
                throw new DiyFileException("存储信息导入失败！");
            }
            storage.setId(null);
            this.save(storage);
            List<StorageConfig> configList = storageInfoVo.getConfigList();
            for (StorageConfig config : configList) {
                config.setId(null);
                config.setStorageId(storage.getId());
            }
            storageConfigService.saveBatch(configList);
        }
    }

    @Override
    public List<String> getAwsRegions() {
        return Region.regions().stream().map(Region::id).collect(Collectors.toList());
    }

}
