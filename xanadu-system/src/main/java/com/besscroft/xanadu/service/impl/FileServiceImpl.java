package com.besscroft.xanadu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.besscroft.xanadu.common.param.FileInitParam;
import com.besscroft.xanadu.common.param.storage.init.OneDriveParam;
import com.besscroft.xanadu.common.vo.FileInfoVo;
import com.besscroft.xanadu.mapper.StorageMapper;
import com.besscroft.xanadu.service.FileService;
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
        // TODO 如果设定的挂载路径不是 "/"，那么传入的挂载路径为 "/" 时应该限制查询！
        OneDriveParam param = (OneDriveParam) service.getInitParam();
        String mountPath = param.getMountPath();
        if (!Objects.equals(mountPath, "/") && Objects.equals(folderPath, "/")) {
            return CollUtil.newArrayList();
        }
        return service.getFileList(folderPath);
    }

}
