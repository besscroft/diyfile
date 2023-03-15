package com.besscroft.diyfile.storage.service.base;

import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.S3Param;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public List<FileInfoVo> getFileList(String folderPath) {
        int index = folderPath.indexOf("/");
        if (Objects.equals("/", folderPath)) {
            folderPath = "";
        } else {
            folderPath = folderPath.substring(index + 1) + "/";
        }
        // 构造ListObjectsRequest请求。
        ListObjectsV2Request listObjects = ListObjectsV2Request
                .builder()
                // 列出 folderPath 目录下的所有文件和文件夹。
                .prefix(folderPath)
                // 设置桶。
                .bucket(initParam.getBucketName())
                // 设置正斜线（/）为文件夹的分隔符。
                .delimiter("/")
                // StartAfter 是您希望 Amazon S3 开始列出的位置。Amazon S3 在这个指定的键之后开始列出。StartAfter 可以是存储桶中的任何键。
                .startAfter(folderPath)
                .build();

        ListObjectsV2Response res = s3Client.listObjectsV2(listObjects);
        List<S3Object> objects = res.contents();
        List<CommonPrefix> commonPrefixes = res.commonPrefixes();
        return handleFileList(objects, commonPrefixes, folderPath);
    }

    /**
     * 获取文件列表
     * @param objects S3 对象列表
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> handleFileList(@NonNull List<S3Object> objects, @NonNull List<CommonPrefix> commonPrefixes, String folderPath) {
        List<FileInfoVo> fileInfoVoList = new ArrayList<>();
        for (S3Object object : objects) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            if (object.key().contains("/")) {
                int lastSlashIndex = object.key().lastIndexOf('/');
                if (Objects.equals("", object.key().substring(lastSlashIndex + 1)))
                    continue;
                fileInfoVo.setName(object.key().substring(lastSlashIndex + 1));
                fileInfoVo.setPath(object.key().substring(0, lastSlashIndex));
            } else {
                fileInfoVo.setName(object.key());
                fileInfoVo.setPath("/");
            }
            fileInfoVo.setType(FileConstants.FILE);
            fileInfoVo.setSize(calKb(object.size()));
            fileInfoVo.setLastModifiedDateTime(LocalDateTime.ofInstant(object.lastModified(), ZoneId.systemDefault()));
            fileInfoVo.setFile(object.storageClass());
            fileInfoVoList.add(fileInfoVo);
        }
        for (CommonPrefix commonPrefix : commonPrefixes) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            int index = commonPrefix.prefix().lastIndexOf("/");
            fileInfoVo.setName(commonPrefix.prefix().substring(0, index));
            fileInfoVo.setType(FileConstants.FOLDER);
            fileInfoVo.setPath(folderPath + "/" + commonPrefix.prefix());
            fileInfoVoList.add(fileInfoVo);
        }
        return fileInfoVoList;
    }

    public abstract Integer getStorageType();

    @Override
    public FileInfoVo getFileInfo(String filePath, String fileName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(initParam.getBucketName())
                .key(PathUtils.decode(PathUtils.removeLeadingSlash(filePath)))
                .build();
        HeadObjectResponse response = s3Client.headObject(headObjectRequest);
        FileInfoVo fileInfoVo = new FileInfoVo();
        fileInfoVo.setName(fileName);
        fileInfoVo.setPath(filePath);
        fileInfoVo.setType(FileConstants.FILE);
        fileInfoVo.setSize(calKb(response.contentLength()));
        fileInfoVo.setLastModifiedDateTime(LocalDateTime.ofInstant(response.lastModified(), ZoneId.systemDefault()));
        fileInfoVo.setFile(response.storageClassAsString());
        fileInfoVo.setUrl(getFileDownloadUrl(fileName, filePath));
        return fileInfoVo;
    }

    @Override
    public void createItem(String folderPath, String fileName) {
        throw new DiyFileException("S3 API 暂不支持创建对象");
    }

    @Override
    public void updateItem(String filePath, String fileName) {
        throw new DiyFileException("S3 API 暂不支持更新对象");
    }

    @Override
    public void deleteItem(@NonNull String filePath) {
        // 去除第一个 / 符号，并将 % 开头的 16 进制表示的内容解码。
        String decode = PathUtils.decode(PathUtils.removeLeadingSlash(filePath));
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(initParam.getBucketName())
                .key(decode)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public void uploadItem(String folderPath, String fileName) {
        throw new DiyFileException("S3 API 暂不支持上传文件");
    }

    @Override
    public String getUploadSession(String folderPath) {
        throw new DiyFileException("S3 API 不支持上传会话");
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return getObjectUrl(initParam.getBucketName(), PathUtils.removeLeadingSlash(filePath));
    }

    /**
     * 获取文件下载地址
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 文件下载地址
     */
    private String getObjectUrl(String bucketName, String objectName) {
        // TODO 获取代理地址
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        URL url = s3Client.utilities().getUrl(request);
        return url.toString();
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
