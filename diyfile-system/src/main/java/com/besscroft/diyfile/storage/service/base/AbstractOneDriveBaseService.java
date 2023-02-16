package com.besscroft.diyfile.storage.service.base;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.common.constant.storage.OneDriveConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2023/2/15 10:40
 */
@Slf4j
public abstract class AbstractOneDriveBaseService<T extends OneDriveParam> extends AbstractFileBaseService<T> {

    private Cache<String, Object> caffeineCache;
    private ObjectMapper objectMapper;

    @Autowired
    public void setCaffeineCache(Cache<String, Object> caffeineCache) {
        this.caffeineCache = caffeineCache;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init() {
        refreshAccessToken();
        initialized = true;
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.ONE_DRIVE.getValue();
    }

    /**
     * 获取 OneDrive 上载会话
     * @param folderPath 文件路径
     * @return 会话
     */
    public abstract String getUploadSession(String folderPath);

    /**
     * 获取 OneDrive 驱动 id
     * @return OneDrive 驱动 id
     */
    protected String getDriveId() {
        return Optional.ofNullable(caffeineCache.getIfPresent("storage:driveId:id:" + storageId))
                .orElseGet(this::getDriveIdRest).toString();
    }

    /**
     * 通过 Rest 接口获取 OneDrive 驱动 id
     * @return OneDrive 驱动 id
     */
    private String getDriveIdRest() {
        String driveRootUrl = OneDriveConstants.DRIVE_ID_URL;
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(driveRootUrl)
                .addHeader("Authorization", getAccessToken())
                .get().getBody().toString());
        try {
            Map map = objectMapper.readValue(result.getStr("parentReference"), Map.class);
            String driveId = map.get("driveId").toString();
            caffeineCache.put("storage:driveId:id:" + storageId, driveId);
            return driveId;
        } catch (JsonProcessingException e) {
            log.error("获取 OneDrive 驱动 id 失败！");
            return "";
        }
    }

    /**
     * 刷新 token 并返回新的 token
     * @return 新的 token
     */
    protected String getAccessToken() {
        Long storageId = getStorageId();
        // 先从缓存中获取 token，如果没有则从调用 REST API 获取
        return Optional.ofNullable(caffeineCache.getIfPresent("storage:token:id:" + storageId))
                .orElseGet(this::refreshAccessToken).toString();
    }

    /**
     * 刷新 accessToken
     * @return 新的 accessToken
     */
    private String refreshAccessToken() {
        OneDriveParam param = getInitParam();
        Map<String, String> map = new HashMap<>();
        map.put("client_id", param.getClientId());
        map.put("scope", "user.read files.read.all offline_access");
        map.put("refresh_token", param.getRefreshToken());
        map.put("grant_type", "refresh_token");
        map.put("client_secret", param.getClientSecret());
        try {
            HttpResult result = OkHttps.sync(OneDriveConstants.AUTHENTICATE_URL)
                    .setBodyPara(map)
                    .post();
            Map tokenResult = objectMapper.readValue(result.getBody().toString(), Map.class);
            String accessToken = tokenResult.get("access_token").toString();
            caffeineCache.put("storage:token:id:" + getStorageId(), accessToken);
            return accessToken;
        } catch (Exception e) {
            throw new DiyFileException(e.getMessage());
        }
    }

}
