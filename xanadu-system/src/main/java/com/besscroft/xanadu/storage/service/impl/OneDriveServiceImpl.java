package com.besscroft.xanadu.storage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.xanadu.common.constant.storage.OneDriveConstants;
import com.besscroft.xanadu.common.exception.XanaduException;
import com.besscroft.xanadu.common.param.storage.init.OneDriveParam;
import com.besscroft.xanadu.common.vo.FileInfoVo;
import com.besscroft.xanadu.storage.service.base.AbstractFileBaseService;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description OneDrive 服务实现类
 * @Author Bess Croft
 * @Date 2023/1/20 15:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OneDriveServiceImpl extends AbstractFileBaseService<OneDriveParam> {

    private final Cache<String, Object> caffeineCache;
    private final ObjectMapper objectMapper;

    @Override
    public void init() {

    }

    @Override
    public Integer getStorageType() {
        return 1;
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return null;
    }

    /**
     * 获取 OneDrive 驱动 id
     * @return OneDrive 驱动 id
     * @throws JsonProcessingException
     */
    private String getDriveId() throws JsonProcessingException {
        return Optional.ofNullable(caffeineCache.getIfPresent("storage:driveId:id:" + storageId))
                .orElse(getDriveIdRest()).toString();

    }

    /**
     * 通过 Rest 接口获取 OneDrive 驱动 id
     * @return OneDrive 驱动 id
     * @throws JsonProcessingException
     */
    private String getDriveIdRest() throws JsonProcessingException {
        String driveRootUrl = OneDriveConstants.DRIVE_ID_URL;
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(driveRootUrl)
                .addHeader("Authorization", getAccessToken())
                .get().getBody().toString());
        Map map = objectMapper.readValue(result.getStr("parentReference"), Map.class);
        String driveId = map.get("driveId").toString();
        caffeineCache.put("storage:driveId:id:" + storageId, driveId);
        return driveId;
    }

    @Override
    public List<FileInfoVo> getFileList(String folderPath) {
        if (StrUtil.isBlank(folderPath)) {
            folderPath = initParam.getMountPath();
        }
        try {
            String url = OneDriveConstants.DRIVE_ITEM_URL.replace("{drive-id}", getDriveId())
                    .replace("{path-relative-to-root}", folderPath);
            JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                    .addHeader("Authorization", getAccessToken())
                    .get().getBody().toString());
            log.info("获取 OneDrive 文件列表结果：{}", result);
            JSONArray jsonArray = result.getJSONArray("value");
            // TODO accessToken 过期处理
            if (Objects.isNull(jsonArray)) return CollUtil.newArrayList();
            return getCouvertFileList(jsonArray, folderPath);
        } catch (JsonProcessingException e) {
            throw new XanaduException("获取 OneDrive 文件列表失败！");
        }
    }

    @Override
    public FileInfoVo getFileInfo(String filePath, String fileName) {
        return null;
    }

    @Override
    public void createItem(String folderPath, String fileName) {

    }

    @Override
    public void updateItem(String folderPath, String fileName, String newName) {

    }

    @Override
    public void deleteItem(String folderPath, String fileName) {

    }

    @Override
    public void uploadItem(String folderPath, String fileName) {

    }

    /**
     * 刷新 token 并返回新的 token
     * @return 新的 token
     */
    private String getAccessToken() {
        Long storageId = getStorageId();
        // 先从缓存中获取 token，如果没有则从调用 REST API 获取
        return Optional.ofNullable(caffeineCache.getIfPresent("storage:token:id:" + storageId))
                .orElse(refreshAccessToken()).toString();
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
            throw new XanaduException(e.getMessage());
        }
    }

    /**
     * 处理 OneDrive 返回的文件列表 JSON 数据
     * @param jsonArray OneDrive 返回的文件列表 JSON 数据
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> getCouvertFileList(@NonNull JSONArray jsonArray, String folderPath) {
        List<FileInfoVo> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            FileInfoVo fileInfoVo = new FileInfoVo();
            fileInfoVo.setName(jsonObject.getStr("name"));
            fileInfoVo.setSize(jsonObject.getLong("size"));
            fileInfoVo.setLastModifiedDateTime(jsonObject.getLocalDateTime("lastModifiedDateTime", LocalDateTime.MIN));
            // 设置文件类型 文件/文件夹
            if (jsonObject.containsKey("file")) {
                fileInfoVo.setType("file");
                // 设置文件下载地址
                fileInfoVo.setUrl(jsonObject.getStr("@microsoft.graph.downloadUrl"));
            } else {
                fileInfoVo.setType("folder");
            }
            fileInfoVo.setPath(folderPath);
            list.add(fileInfoVo);
        }
        return list;
    }

}
