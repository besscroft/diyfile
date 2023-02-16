package com.besscroft.diyfile.storage.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.AliYunOssParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 阿里云 OSS 服务实现类
 * @Author Bess Croft
 * @Date 2023/2/12 22:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AliYunOssServiceImpl extends AbstractFileBaseService<AliYunOssParam> {

    private final Cache<String, Object> caffeineCache;
    private final ObjectMapper objectMapper;
    private OSS ossClient;

    /**
     * 阿里云 OSS https://help.aliyun.com/document_detail/32008.htm
     */
    @Override
    public void init() {
        // TODO OSSClient 初始化
        this.ossClient = new OSSClientBuilder()
                .build(initParam.getEndpoint(),
                        initParam.getAccessKeyId(),
                        initParam.getAccessKeySecret());
        initialized = true;
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return null;
    }

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = ossClient.listObjects(initParam.getBucketName());
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        List<OSSObjectSummary> summaryList = objectListing.getObjectSummaries();
        try {
            log.info("阿里云 OSS 返回结果:{}", objectMapper.writeValueAsString(summaryList));
        } catch (JsonProcessingException e) {
            throw new DiyFileException("出错啦！");
        }
        return null;
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.ALIYUN_OSS.getValue();
    }

    @Override
    public FileInfoVo getFileInfo(String filePath, String fileName) {
        return null;
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

}
