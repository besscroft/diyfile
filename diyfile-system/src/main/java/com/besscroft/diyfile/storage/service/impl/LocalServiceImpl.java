package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.LocalParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description 本地文件存储服务实现类
 * @Author Bess Croft
 * @Date 2023/2/15 10:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalServiceImpl extends AbstractFileBaseService<LocalParam> {

    @Override
    public void init() {
        // 本地初始化，判断文件夹是否存在
        boolean exist = FileUtil.exist(initParam.getMountPath());
        if (!exist) {
            throw new DiyFileException("本地文件存储初始化失败，目录不存在！");
        }
        initialized = true;
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return null;
    }

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        File file = new File(folderPath);
        File[] fileList = file.listFiles();
        return handleFileList(fileList);
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.LOCAL.getValue();
    }

    @Override
    public FileInfoVo getFileInfo(String filePath, String fileName) {
        FileInfoVo fileInfoVo = new FileInfoVo();
        File file = new File(filePath);
        if (file.exists() && file.isFile() && Objects.equals(file.getName(), fileName)) {
            fileInfoVo.setName(file.getName());
            fileInfoVo.setSize(file.length());
            fileInfoVo.setPath(file.getPath());
            fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(file.lastModified()));
            // TODO 代理链接生成
            fileInfoVo.setUrl(file.getAbsolutePath());
            fileInfoVo.setType(FileConstants.FILE);
            fileInfoVo.setFile(null);
        }
        return fileInfoVo;
    }

    @Override
    public void createItem(String folderPath, String fileName) {

    }

    @Override
    public void updateItem(String filePath, String fileName) {

    }

    @Override
    public void deleteItem(String filePath) {

    }

    @Override
    public void uploadItem(String folderPath, String fileName) {

    }

    @Override
    public String getUploadSession(String folderPath) {
        return null;
    }

    /**
     * 处理文件列表
     * @param fileList 文件列表
     * @return 文件信息列表
     */
    private List<FileInfoVo> handleFileList(File[] fileList) {
        log.info("文件列表：{}", JSONUtil.toJsonStr(fileList));
        List<FileInfoVo> fileInfoVoList = new ArrayList<>();
        for (File file : fileList) {
            if (file.isDirectory()) {
                // TODO 处理文件夹
                FileInfoVo fileInfoVo = new FileInfoVo();
                fileInfoVo.setName(file.getName());
                fileInfoVo.setSize(file.length());
                fileInfoVo.setPath(file.getPath());
                fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(file.lastModified()));
                fileInfoVo.setUrl(file.getAbsolutePath());
                fileInfoVo.setType(FileConstants.FOLDER);
                fileInfoVo.setFile(null);
                fileInfoVoList.add(fileInfoVo);
            } else {
                // TODO 处理文件
                FileInfoVo fileInfoVo = new FileInfoVo();
                fileInfoVo.setName(file.getName());
                fileInfoVo.setSize(file.length());
                fileInfoVo.setPath(file.getPath());
                fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(file.lastModified()));
                fileInfoVo.setUrl(file.getAbsolutePath());
                fileInfoVo.setType(FileConstants.FILE);
                fileInfoVo.setFile(null);
                fileInfoVoList.add(fileInfoVo);
            }
        }
        return fileInfoVoList;
    }

}
