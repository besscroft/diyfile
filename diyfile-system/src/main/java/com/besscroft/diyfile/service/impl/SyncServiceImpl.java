package com.besscroft.diyfile.service.impl;

import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.service.StorageService;
import com.besscroft.diyfile.service.SyncService;
import com.besscroft.diyfile.storage.context.StorageApplicationContext;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description 存储数据同步服务实现
 * @Author Bess Croft
 * @Date 2023/3/21 21:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {

    private final StorageApplicationContext storageApplicationContext;
    private final StorageService storageService;

    @Override
    public void taskAdd(String beforeStorageKey, String beforePath, String afterStorageKey, String afterPath) {
        // 判断被同步路径是否为文件夹路径
        if (!PathUtils.isFolder(afterPath)) throw new DiyFileException("被同步路径必须为文件夹路径！");
        Long beforeStorageId = storageService.getStorageIdByStorageKey(afterStorageKey);
        Long afterStorageId = storageService.getStorageIdByStorageKey(afterStorageKey);
        log.info("同步任务参数：{} {} {} {}", beforeStorageKey, beforePath, afterStorageKey, afterPath);
        // 同步操作
        if (StrUtil.equals(beforeStorageKey, afterStorageKey)) {
            // 相同存储内移动文件
            // 移动文件，调用对应存储 API 移动文件接口
            AbstractFileBaseService<FileInitParam> service = storageApplicationContext.getServiceByStorageId(beforeStorageId);
            service.moveItem(beforePath, afterPath);
        } else {
            // 不同存储内移动文件
            // TODO 先下载文件，然后上传文件；如果存储 API 支持传递文件流，则直接复用。
            throw new DiyFileException("暂不支持跨存储同步文件！");
        }
    }

}
