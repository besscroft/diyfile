package com.besscroft.diyfile.common.util;

import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.entity.StorageConfig;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.param.FileInitParam;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description
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
            return OneDriveParam.builder()
                    .clientId(configMap.get("client_id"))
                    .clientSecret(configMap.get("client_secret"))
                    .redirectUri(configMap.get("redirect_uri"))
                    .refreshToken(configMap.get("refresh_token"))
                    .mountPath(configMap.get("mount_path"))
                    .build();
        } else if (Objects.equals(storage.getType(), StorageTypeEnum.ALIYUN_OSS.getValue())) {
            // TODO 阿里云 OSS 初始化参数处理
            return null;
        }
        return null;
    }

}
