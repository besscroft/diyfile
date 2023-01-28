package com.besscroft.xanadu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.besscroft.xanadu.common.converter.StorageConverterMapper;
import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.common.param.FileInitParam;
import com.besscroft.xanadu.common.param.storage.init.OneDriveParam;
import com.besscroft.xanadu.common.vo.FileInfoVo;
import com.besscroft.xanadu.common.vo.StorageInfoVo;
import com.besscroft.xanadu.mapper.StorageMapper;
import com.besscroft.xanadu.service.FileService;
import com.besscroft.xanadu.service.StorageService;
import com.besscroft.xanadu.storage.context.StorageApplicationContext;
import com.besscroft.xanadu.storage.service.base.AbstractFileBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Description 文件服务实现类
 * @Author Bess Croft
 * @Date 2023/1/22 20:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageApplicationContext storageApplicationContext;
    private final StorageMapper storageMapper;
    private final StorageService storageService;

    @Override
    public StorageInfoVo defaultStorage() {
        Storage storage = storageMapper.selectByDefault();
        if (Objects.isNull(storage)) return new StorageInfoVo();
        return StorageConverterMapper.INSTANCE.StorageToInfoVo(storage);
    }

    @Override
    public List<FileInfoVo> defaultItem() {
        Long storageId = storageMapper.selectIdByDefault();
        if (Objects.isNull(storageId)) return CollUtil.newArrayList();
        AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(storageId);
        return service.getFileList(null);
    }

    @Override
    public List<FileInfoVo> getItem(Long storageId, String folderPath) {
        AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(storageId);
        // 如果设定的挂载路径不是 "/"，那么传入的挂载路径为 "/" 时应该限制查询！
        OneDriveParam param = (OneDriveParam) service.getInitParam();
        String mountPath = param.getMountPath();
        // 如果传入的挂载路径为空，则使用默认挂载路径
        if (StrUtil.isBlank(folderPath)) {
            service.getFileList(mountPath);
        }
        if (!Objects.equals(mountPath, "/") && Objects.equals(folderPath, "/")) {
            return CollUtil.newArrayList();
        }
        // 如果设定的挂载路径不是 "/"，那么传入的挂载路径不为 "/" 时，应该将挂载路径和传入的挂载路径拼接起来！
        if (!Objects.equals(mountPath, "/") && !Objects.equals(folderPath, "/")) {
            folderPath = mountPath + folderPath;
        }
        // 如果设定的挂载路径是 "/"，那么传入的挂载路径不为 "/" 时，直接查询！
        // 如果设定的挂载路径是 "/"，那么传入的挂载路径为 "/" 时，直接查询！
        return service.getFileList(folderPath);
    }

    @Override
    public List<FileInfoVo> getItemByKey(String storageKey, String folderPath) {
        Long storageId = storageService.getStorageIdByStorageKey(storageKey);
        return getItem(storageId, folderPath);
    }

}
