package com.besscroft.diyfile.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.converter.StorageConverterMapper;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.mapper.StorageMapper;
import com.besscroft.diyfile.service.FileService;
import com.besscroft.diyfile.service.StorageService;
import com.besscroft.diyfile.storage.context.StorageApplicationContext;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
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
        FileInitParam initParam = service.getInitParam();
        String path = handlePath(initParam.getMountPath(), folderPath);
        return service.getFileList(path);
    }

    @Override
    public List<FileInfoVo> getItemByKey(String storageKey, String folderPath) {
        Long storageId = storageService.getStorageIdByStorageKey(storageKey);
        return getItem(storageId, folderPath);
    }

    @Override
    public FileInfoVo getFileInfo(Long storageId, String filePath, String fileName) {
        AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(storageId);
        OneDriveParam param = (OneDriveParam) service.getInitParam();
        String path = handlePath(param.getMountPath(), filePath);
        return service.getFileInfo(path, fileName);
    }

    @Override
    public FileInfoVo getFileInfo(String storageKey, String filePath, String fileName) {
        Long storageId = storageService.getStorageIdByStorageKey(storageKey);
        return getFileInfo(storageId, filePath, fileName);
    }

    @Override
    public String getUploadUrl(String storageKey, String path) {
        Long storageId = storageService.getStorageIdByStorageKey(storageKey);
        AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(storageId);
        OneDriveParam param = (OneDriveParam) service.getInitParam();
        path = handlePath(param.getMountPath(), path);
        return service.getUploadSession(path);
    }

    @Override
    public void deleteFile(String storageKey, String filePath) {
        Long storageId = storageService.getStorageIdByStorageKey(storageKey);
        AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(storageId);
        service.deleteItem(filePath);
    }

    /**
     * 文件/夹路径处理
     * @param path 文件/夹路径
     * @return 处理后的文件/夹路径
     */
    private String handlePath(String mountPath, String path) {
        // 如果设定的挂载路径为 "/"
        if (Objects.equals("/", mountPath)) {
            // 如果传入的挂载路径为空，则使用默认挂载路径
            if (StrUtil.isBlank(path)) {
                return mountPath;
            } else if (!Objects.equals("/", path)) {
                // 如果传入的挂载路径不为空，且不是 "/"
                return mountPath + path;
            } else {
                // 如果传入的挂载路径不为空，且是 "/"，则使用默认挂载路径
                return mountPath;
            }
        } else {
            // 如果设定的挂载路径不是 "/"
            // 如果传入的挂载路径为空，则使用默认挂载路径
            if (StrUtil.isBlank(path)) {
                return mountPath;
            } else if (!Objects.equals("/", path)) {
                // 如果传入的挂载路径不为空，且不是 "/"
                return mountPath + path;
            } else {
                // 如果传入的挂载路径不为空，且是 "/"，则使用默认挂载路径
                return mountPath;
            }
        }
    }

}
