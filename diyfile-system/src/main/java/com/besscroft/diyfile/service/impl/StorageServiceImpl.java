package com.besscroft.diyfile.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.SystemConstants;
import com.besscroft.diyfile.common.converter.StorageConverterMapper;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.StorageAddParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateParam;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.mapper.StorageConfigMapper;
import com.besscroft.diyfile.mapper.StorageMapper;
import com.besscroft.diyfile.service.StorageConfigService;
import com.besscroft.diyfile.service.StorageService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<Storage> storagePage(Integer pageNum, Integer pageSize, Integer type) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage(type);
    }

    @Override
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
    public void updateStorage(StorageUpdateParam param) {
        Storage storage = StorageConverterMapper.INSTANCE.UpdateParamToStorage(param);
        Storage oldStorage = this.baseMapper.selectById(storage.getId());
        if (!Objects.equals(storage.getType(), oldStorage.getType()))
            throw new DiyFileException("存储类型不允许修改！");
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
        Assert.isTrue(this.baseMapper.updateById(storage) > 0, "更新状态失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        Map<String, String> configMap = configList.stream().collect(Collectors.toMap(StorageConfig::getConfigKey, StorageConfig::getConfigValue));
        if (Objects.equals(storage.getType(), StorageTypeEnum.ONE_DRIVE.getValue())) {
            return OneDriveParam.builder()
                    .clientId(configMap.get("client_id"))
                    .clientSecret(configMap.get("client_secret"))
                    .redirectUri(configMap.get("redirect_uri"))
                    .refreshToken(configMap.get("refresh_token"))
                    .mountPath(configMap.get("mount_path"))
                    .build();
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "storageKey" ,key = "#storageKey", unless = "#result == null")
    public Long getStorageIdByStorageKey(String storageKey) {
        return this.baseMapper.selectIdByStorageKey(storageKey);
    }

    @Override
    public List<Storage> getEnableStorage() {
        return this.baseMapper.selectAllByEnable();
    }

}
