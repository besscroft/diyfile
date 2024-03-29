package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.AliYunOssParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractOSSBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description 阿里云 OSS 服务实现类
 * @Author Bess Croft
 * @Date 2023/2/12 22:13
 */
@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AliYunOssServiceImpl extends AbstractOSSBaseService<AliYunOssParam> {

    private OSS ossClient;

    /**
     * 阿里云 OSS https://help.aliyun.com/document_detail/32008.htm
     */
    @Override
    public void init() {
        // OSSClient 初始化
        this.ossClient = new OSSClientBuilder()
                .build(initParam.getEndpoint(),
                        initParam.getAccessKeyId(),
                        initParam.getAccessKeySecret());
        initialized = true;
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.ALIYUN_OSS.getValue();
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath, String fullPath) {
        return getObjectUrl(initParam.getBucketName(), filePath);
    }

    @Override
    public ResponseEntity<Resource> getFileResource(String fileName, String filePath) {
        return null;
    }

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(initParam.getBucketName());
        // 设置正斜线（/）为文件夹的分隔符。
        listObjectsRequest.setDelimiter("/");
        // 列出 folderPath 目录下的所有文件和文件夹。
        int index = folderPath.indexOf("/");
        if (Objects.equals("/", folderPath)) {
            listObjectsRequest.setPrefix("");
        } else {
            String path = folderPath.substring(index + 1) + "/";
            listObjectsRequest.setPrefix(path);
        }
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> summaryList = listing.getObjectSummaries();
        List<String> commonPrefixes = listing.getCommonPrefixes();
        return handleFileList(summaryList, commonPrefixes, folderPath);
    }

    @Override
    public FileInfoVo getFileInfo(String filePath, String fileName) {
        FileInfoVo fileInfoVo = new FileInfoVo();
        fileInfoVo.setName(fileName);
        fileInfoVo.setSize(0L);
        fileInfoVo.setLastModifiedDateTime(null);
        fileInfoVo.setType(FileConstants.FILE);
        fileInfoVo.setPath(filePath);
        fileInfoVo.setFile(null);
        // 设置文件下载地址
        fileInfoVo.setUrl(getObjectUrl(initParam.getBucketName(), filePath));
        return fileInfoVo;
    }

    @Override
    public void createItem(String folderPath, String fileName) {
        throw new DiyFileException("阿里云 OSS 服务暂不支持创建对象！");
    }

    @Override
    public void updateItem(String filePath, String fileName) {
        throw new DiyFileException("阿里云 OSS 服务暂不支持更新对象！");
    }

    @Override
    public void deleteItem(String filePath) {
        // 删除文件或目录。如果要删除目录，目录必须为空。
        if ("//".equals(StrUtil.sub(filePath, 0, 2))) {
            ossClient.deleteObject(initParam.getBucketName(), StrUtil.sub(filePath, 2, filePath.length()));
        } else {
            ossClient.deleteObject(initParam.getBucketName(), StrUtil.sub(filePath, 1, filePath.length()));
        }
    }

    @Override
    public void uploadItem(String folderPath, String fileName) {
        // TODO 上传文件
        throw new DiyFileException("阿里云 OSS 服务暂不支持上传文件");
    }

    @Override
    public String getUploadSession(String folderPath) {
        throw new DiyFileException("阿里云 OSS 服务不支持上传会话");
    }

    /**
     * 获取文件列表
     * @param summaryList 文件列表
     * @param commonPrefixes 文件夹列表
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> handleFileList(@NonNull List<OSSObjectSummary> summaryList, @NonNull List<String> commonPrefixes, @NonNull String folderPath) {
        List<FileInfoVo> infoVoList = new ArrayList<>();
        for(OSSObjectSummary summary : summaryList) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            int lastSlashIndex = summary.getKey().lastIndexOf('/');
            if (Objects.equals("", summary.getKey().substring(lastSlashIndex + 1))) {
                continue;
            }
            if (summary.getKey().contains("/")) {
                fileInfoVo.setName(summary.getKey().substring(lastSlashIndex + 1));
            } else {
                fileInfoVo.setName(summary.getKey());
            }
            fileInfoVo.setFile(summary.getType());
            fileInfoVo.setLastModifiedDateTime(LocalDateTimeUtil.of(summary.getLastModified()));
            fileInfoVo.setSize(summary.getSize());
            fileInfoVo.setPath(folderPath);
            fileInfoVo.setType(FileConstants.FILE);
            fileInfoVo.setUrl(getObjectUrl(initParam.getBucketName(), summary.getKey()));
            infoVoList.add(fileInfoVo);
        }
        for(String prefix : commonPrefixes) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            int index = prefix.lastIndexOf("/");
            fileInfoVo.setName(prefix.substring(0, index));
            fileInfoVo.setFile(null);
            fileInfoVo.setLastModifiedDateTime(null);
            fileInfoVo.setSize(calculateFolderLength(ossClient, initParam.getBucketName(), prefix));
            fileInfoVo.setPath(folderPath);
            fileInfoVo.setType(FileConstants.FOLDER);
            infoVoList.add(fileInfoVo);
        }
        return infoVoList;
    }

    /**
     * 获取某个存储空间下指定目录（文件夹）下的文件大小。
     * @param ossClient OSSClient实例
     * @param bucketName 存储空间名称
     * @param folder 指定目录（文件夹）
     * @return 文件大小
     */
    private static long calculateFolderLength(OSS ossClient, String bucketName, String folder) {
        long size = 0L;
        ObjectListing objectListing = null;
        do {
            // MaxKey 默认值为 100，最大值为 1000。
            ListObjectsRequest request = new ListObjectsRequest(bucketName).withPrefix(folder).withMaxKeys(1000);
            if (objectListing != null) {
                request.setMarker(objectListing.getNextMarker());
            }
            objectListing = ossClient.listObjects(request);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                size += s.getSize();
            }
        } while (objectListing.isTruncated());
        return size;
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        // TODO 获取代理地址
        try {
            URL url = new URL(initParam.getEndpoint());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(url.getProtocol())
                    .append("://")
                    .append(bucketName)
                    .append(".")
                    .append(url.getHost());
            if ("/".equals(StrUtil.sub(objectName, 0, 1))) {
                stringBuilder.append(objectName);
            } else {
                stringBuilder.append("/")
                            .append(objectName);
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            log.error("地址获取失败:{}", e.getMessage());
            throw new DiyFileException("地址获取失败！");
        }
    }

    @Override
    public void moveItem(String startPath, String endPath) {
        // TODO 移动文件
    }

}
