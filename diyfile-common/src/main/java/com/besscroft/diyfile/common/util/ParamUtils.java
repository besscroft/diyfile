package com.besscroft.diyfile.common.util;

import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.init.AliYunOssParam;
import com.besscroft.diyfile.common.param.storage.init.LocalParam;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;

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
            return OneDriveParam.builder()
                    .clientId(configMap.get("client_id"))
                    .clientSecret(configMap.get("client_secret"))
                    .redirectUri(configMap.get("redirect_uri"))
                    .refreshToken(configMap.get("refresh_token"))
                    .mountPath(configMap.get("mount_path"))
                    .proxyUrl(Optional.ofNullable(configMap.get("proxy_url")).orElse(""))
                    .build();
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.ALIYUN_OSS.getValue())) {
            return AliYunOssParam.builder()
                    .accessKeyId(configMap.get("accessKeyId"))
                    .accessKeySecret(configMap.get("accessKeySecret"))
                    .endpoint(configMap.get("endpoint"))
                    .bucketName(configMap.get("bucketName"))
                    .mountPath(configMap.get("mount_path"))
                    .build();
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.LOCAL.getValue())) {
            return LocalParam.builder()
                    .mountPath(configMap.get("mount_path"))
                    .build();
        }
        return null;
    }

}
