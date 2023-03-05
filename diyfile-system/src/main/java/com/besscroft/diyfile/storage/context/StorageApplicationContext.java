package com.besscroft.diyfile.storage.context;

import cn.hutool.extra.spring.SpringUtil;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.util.ParamUtils;
import com.besscroft.diyfile.mapper.StorageConfigMapper;
import com.besscroft.diyfile.mapper.StorageMapper;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 文件服务上下文
 * @Author Bess Croft
 * @Date 2023/1/20 16:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StorageApplicationContext implements ApplicationContextAware {

    private final StorageMapper storageMapper;
    private final StorageConfigMapper storageConfigMapper;

    /**
     * Map<Long, AbstractBaseFileService>
     * Map<存储 id, 存储 Service>
     */
    private static final Map<Long, AbstractFileBaseService<FileInitParam>> storageServiceMap = new ConcurrentHashMap<>();

    /** Map<存储源类型, 存储源 Service> */
    private static Map<String, AbstractFileBaseService> primitiveStorageServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("获取原始的存储服务 Bean！");
        primitiveStorageServiceMap = applicationContext.getBeansOfType(AbstractFileBaseService.class);
        log.debug("开始初始化文件服务上下文！");
        List<Storage> storageList = storageMapper.selectAllByEnable();
        for (Storage storage: storageList) {
            try {
                init(storage);
                log.info("存储初始化成功！存储 id 为={}，存储名称为={}", storage.getId(), storage.getName());
            } catch (Exception e) {
                log.error("存储初始化失败！存储 id 为={}，存储名称为={}", storage.getId(), storage.getName());
            }
        }
        log.debug("文件服务上下文初始化完成！");
    }

    /**
     * 获取对应的存储服务实现类
     * @param storageId 存储 id
     * @return 存储服务实现类
     */
    public AbstractFileBaseService<FileInitParam> getServiceByStorageId(Long storageId) {
        log.info("存储serviceMap: {}", storageServiceMap);
        return storageServiceMap.get(storageId);
    }

    /**
     * 初始化存储 Service, 添加到 Spring 上下文
     * @param storage 存储对象
     */
    public void init(Storage storage) {
        Long storageId = storage.getId();
        String storageName = storage.getName();

        AbstractFileBaseService<FileInitParam> baseFileService = getInitStorageBeanByStorageId(storageId);
        if (Objects.isNull(baseFileService)) {
            throw new DiyFileException("存储初始化失败！存储 id 为：" + storageId + "，存储名称为：" + storageName);
        }

        // 初始化处理
        baseFileService.setStorageId(storageId);
        baseFileService.setName(storageName);
        baseFileService.setStorageKey(storage.getStorageKey());
        FileInitParam initParam = getFileInitParam(storageId);
        baseFileService.setInitParam(initParam);
        baseFileService.init();

        storageServiceMap.put(storageId, baseFileService);
    }

    /**
     * 销毁存储 Service
     * @param storageId 存储 id
     */
    public void destroy(Long storageId) {
        storageServiceMap.remove(storageId);
    }

    /**
     * 获取指定存储源初始状态的 Service Bean
     * @param storageId 存储 id
     * @return 存储源对应未初始化的 Service
     */
    private AbstractFileBaseService<FileInitParam> getInitStorageBeanByStorageId(Long storageId) {
        Storage storage = storageMapper.selectById(storageId);
        for (AbstractFileBaseService<?> value : primitiveStorageServiceMap.values()) {
            if (Objects.equals(value.getStorageType(), storage.getType())) {
                return SpringUtil.getBean(value.getClass());
            }
        }
        return null;
    }

    /**
     * 获取存储初始化参数
     * @param storageId 存储 id
     * @return 存储初始化参数
     */
    private FileInitParam getFileInitParam(Long storageId) {
        Storage storage = storageMapper.selectById(storageId);
        Assert.notNull(storage, "存储不存在！");
        List<StorageConfig> configList = storageConfigMapper.selectByStorageId(storageId);
        return ParamUtils.getFileInitParam(storage, configList);
    }

}
