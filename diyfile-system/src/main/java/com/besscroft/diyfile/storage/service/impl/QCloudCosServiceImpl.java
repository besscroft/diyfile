package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.QCloudCosParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractOSSBaseService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description 腾讯云 COS 服务实现类
 * @Author Bess Croft
 * @Date 2023/8/13 17:13
 */
@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QCloudCosServiceImpl extends AbstractOSSBaseService<QCloudCosParam> {

    private COSClient cosClient;

    /**
     * Q：你好，麻烦请问下腾讯云 COS 是否支持 AWS Java SDK 2.x 版本？查阅文档只看到 1.x 示例：https://cloud.tencent.com/document/product/436/37421#java，想确认下是否有支持 2.x
     * A：您好，非常抱歉，辛苦您久等了，关于您这边反馈的问题，这边为您核实了一下呢，目前COS仅支持AWS Java SDK 1.x版本，暂不支持2.x版本的呢
     */
    @Override
    public void init() {
        COSCredentials cred = new BasicCOSCredentials(initParam.getSecretId(), initParam.getSecretKey());
        // 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(initParam.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 生成 cos 客户端。
        this.cosClient = new COSClient(cred, clientConfig);
        initialized = true;
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath, String fullPath) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> getFileResource(String fileName, String filePath) {
        return null;
    }

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置 bucket 名称
        listObjectsRequest.setBucketName(initParam.getBucketName());
        // prefix 表示列出的 object 的 key 以 prefix 开始
        listObjectsRequest.setPrefix(initParam.getMountPath());
        // deliter 表示分隔符, 设置为/表示列出当前目录下的 object, 设置为空表示列出所有的 object
        listObjectsRequest.setDelimiter(folderPath);
        // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        List<COSObjectSummary> summaryList = new ArrayList<>();
        List<String> commonPrefixes = new ArrayList<>();
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (Exception e) {
                log.error("获取文件列表失败:{}", e.getMessage());
                throw new DiyFileException("获取文件列表失败!");
            }
            // common prefix 表示被 delimiter 截断的路径, 如 delimter 设置为/, common prefix 则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();
            commonPrefixes.addAll(commonPrefixs);
            // object summary 表示所有列出的 object 列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            summaryList.addAll(cosObjectSummaries);
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
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
        throw new DiyFileException("腾讯云 COS 不支持创建对象!");
    }

    @Override
    public void updateItem(String filePath, String fileName) {
        throw new DiyFileException("腾讯云 COS 不支持修改对象!");
    }

    @Override
    public void deleteItem(String filePath) {
        cosClient.deleteObject(initParam.getBucketName(), filePath);
    }

    @Override
    public void uploadItem(String folderPath, String fileName) {
        // TODO 上传文件
        throw new DiyFileException("腾讯云 COS 不支持上传对象!");
    }

    @Override
    public String getUploadSession(String folderPath) {
        return null;
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.QCLOUD_COS.getValue();
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        return cosClient.getObjectUrl(bucketName, objectName).toString();
    }

    @Override
    public void moveItem(String startPath, String endPath) {

    }

    /**
     * 获取文件列表
     * @param summaryList 文件列表
     * @param commonPrefixes 文件夹列表
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> handleFileList(@NonNull List<COSObjectSummary> summaryList, @NonNull List<String> commonPrefixes, @NonNull String folderPath) {
        List<FileInfoVo> infoVoList = new ArrayList<>();
        for(COSObjectSummary summary : summaryList) {
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
            fileInfoVo.setFile(summary.getETag());
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
            fileInfoVo.setSize(calculateFolderLength(cosClient, initParam.getBucketName(), prefix));
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
    private static long calculateFolderLength(COSClient ossClient, String bucketName, String folder) {
        long size = 0L;
        ObjectListing objectListing = null;
        do {
            // MaxKey 默认值为 100，最大值为 1000。
            ListObjectsRequest request = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(folder)
                    .withMaxKeys(1000);
            if (objectListing != null) {
                request.setMarker(objectListing.getNextMarker());
            }
            objectListing = ossClient.listObjects(request);
            List<COSObjectSummary> sums = objectListing.getObjectSummaries();
            for (COSObjectSummary s : sums) {
                size += s.getSize();
            }
        } while (objectListing.isTruncated());
        return size;
    }

}
