package com.besscroft.diyfile.common.util;

import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.init.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description 初始化参数处理工具类
 * @Author Bess Croft
 * @Date 2023/2/12 13:49
 */
public class ParamUtils {

    /**
     * 获取文件初始化参数
     * @param storage 存储
     * @param configList 存储配置列表
     * @return 文件初始化参数
     */
    public static FileInitParam getFileInitParam(Storage storage, List<StorageConfig> configList) {
        Map<String, String> configMap = configList.stream().collect(Collectors.toMap(StorageConfig::getConfigKey, StorageConfig::getConfigValue));
        if (Objects.equals(storage.getType(), StorageTypeEnum.ONE_DRIVE.getValue())) {
            // 如果代理地址最后一个字符为 / 则移除掉
            boolean endWith = StrUtil.endWith(configMap.get("proxy_url"), "/");
            if (endWith) {
                configMap.put("proxy_url", StrUtil.subBefore(configMap.get("proxy_url"), "/", true));
            }
            OneDriveParam param = OneDriveParam.builder()
                    .clientId(configMap.get("client_id"))
                    .clientSecret(configMap.get("client_secret"))
                    .redirectUri(configMap.get("redirect_uri"))
                    .refreshToken(configMap.get("refresh_token"))
                    .proxyUrl(Optional.ofNullable(configMap.get("proxy_url")).orElse(""))
                    .build();
            param.setMountPath(configMap.get("mount_path"));
            return param;
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.ALIYUN_OSS.getValue())) {
            AliYunOssParam param = AliYunOssParam.builder()
                    .accessKeyId(configMap.get("accessKeyId"))
                    .accessKeySecret(configMap.get("accessKeySecret"))
                    .endpoint(configMap.get("endpoint"))
                    .build();
            param.setMountPath(configMap.get("mount_path"));
            param.setBucketName(configMap.get("bucketName"));
            return param;
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.LOCAL.getValue())) {
            LocalParam param = LocalParam.builder()
                    .build();
            param.setDomain(Optional.ofNullable(configMap.get("domain")).orElse(""));
            param.setMountPath(configMap.get("mount_path"));
            return param;
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.AMAZON_S3.getValue())) {
            AmazonS3Param param = AmazonS3Param.builder().build();
            param.setAccessKey(configMap.get("accessKey"));
            param.setSecretKey(configMap.get("secretKey"));
            param.setRegion(configMap.get("region"));
            param.setEndpoint(configMap.get("endpoint"));
            param.setMountPath(configMap.get("mount_path"));
            param.setBucketName(configMap.get("bucketName"));
            return param;
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.QCLOUD_COS.getValue())) {
            // 腾讯云COS初始化参数
            QCloudCosParam param = QCloudCosParam.builder().build();
            param.setSecretId(configMap.get("secretId"));
            param.setSecretKey(configMap.get("secretKey"));
            param.setRegion(configMap.get("region"));
            param.setEndpoint(configMap.get("endpoint"));
            param.setMountPath(configMap.get("mount_path"));
            param.setBucketName(configMap.get("bucketName"));
            return param;
        }
        return null;
    }

}
