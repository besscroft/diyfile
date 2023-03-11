package com.besscroft.diyfile.storage.service.base;

import com.besscroft.diyfile.common.param.storage.init.S3Param;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

/**
 * @Description S3 基础服务抽象类
 * @Author Bess Croft
 * @Date 2023/3/10 23:27
 */
@Slf4j
public abstract class AbstractS3BaseService<T extends S3Param> extends AbstractFileBaseService<T> {

    /** S3 客户端 */
    protected S3Client s3Client;

    public abstract void init();

    @Override
    public abstract String getFileDownloadUrl(String fileName, String filePath);

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(initParam.getBucketName())
                    .build();

            ListObjectsResponse res = s3Client.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                log.info("The name of the key is " + myValue.key());
                log.info("The object is " + calKb(myValue.size()) + " KBs");
                log.info("The owner is " + myValue.owner());
            }

        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
        return null;
    }

    public abstract Integer getStorageType();

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

    /**
     * convert bytes to kbs.
     * @param val bytes
     * @return kbs
     */
    private static long calKb(Long val) {
        return val/1024;
    }

}
