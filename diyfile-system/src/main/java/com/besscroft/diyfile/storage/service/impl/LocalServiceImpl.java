package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.LocalParam;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public String getFileDownloadUrl(String fileName, String filePath, String fullPath) {
        return StrUtil.replace(fullPath, "raw", "proxy");
    }

    @Override
    public ResponseEntity<Resource> getFileResource(String fileName, String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile() && Objects.equals(file.getName(), fileName)) {
            MediaType mimeType = MediaType.APPLICATION_OCTET_STREAM;
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncodeUtil.encodeAll(file.getName()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(mimeType)
                    .body(new FileSystemResource(file));
        }
        throw new DiyFileException("文件不存在！");
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
            // 代理链接生成
            fileInfoVo.setUrl(getFileDomainUrl(file.getPath()));
            fileInfoVo.setType(FileConstants.FILE);
            fileInfoVo.setFile(null);
        }
        return fileInfoVo;
    }

    @Override
    public void createItem(String folderPath, String fileName) {
        throw new DiyFileException("本地文件存储不支持创建文件！");
    }

    @Override
    public void updateItem(String filePath, String fileName) {
        throw new DiyFileException("本地文件存储不支持更新文件！");
    }

    @Override
    public void deleteItem(String filePath) {
        throw new DiyFileException("本地文件存储不支持删除文件！");
    }

    @Override
    public void uploadItem(String folderPath, String fileName) {
        throw new DiyFileException("本地文件存储不支持上传文件！");
    }

    @Override
    public String getUploadSession(String folderPath) {
        return null;
    }

    @Override
    public void moveItem(String startPath, String endPath) {
        // TODO 本地文件移动
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
                // 处理文件夹
                FileInfoVo fileInfoVo = new FileInfoVo();
                fileInfoVo.setName(file.getName());
                fileInfoVo.setSize(file.length());
                fileInfoVo.setPath(file.getPath());
                fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(file.lastModified()));
                fileInfoVo.setUrl(getFileDomainUrl(file.getPath()));
                fileInfoVo.setType(FileConstants.FOLDER);
                fileInfoVo.setFile(null);
                fileInfoVoList.add(fileInfoVo);
            } else {
                // 处理文件
                FileInfoVo fileInfoVo = new FileInfoVo();
                fileInfoVo.setName(file.getName());
                fileInfoVo.setSize(file.length());
                fileInfoVo.setPath(file.getPath());
                fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(file.lastModified()));
                fileInfoVo.setUrl(getFileDomainUrl(file.getPath()));
                fileInfoVo.setType(FileConstants.FILE);
                fileInfoVo.setFile(null);
                fileInfoVoList.add(fileInfoVo);
            }
        }
        return fileInfoVoList;
    }

    /**
     * 获取文件预览路径
     * @param pathAndName 文件路径和名称
     * @return 文件预览路径
     */
    private String getFileDomainUrl(String pathAndName) {
        if (StrUtil.isBlank(initParam.getDomain())) {
            return "/@api" + "/" + PathUtils.removeLeadingSlash(pathAndName);
        }
        return PathUtils.removeTrailingSlash(initParam.getDomain()) + "/" + PathUtils.removeLeadingSlash(pathAndName.replace(initParam.getMountPath(), ""));
    }

}
