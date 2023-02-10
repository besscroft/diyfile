package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.common.constant.storage.OneDriveConstants;
import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.exception.XanaduException;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractFileBaseService;
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
        return StorageTypeEnum.ONE_DRIVE.getValue();
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return null;
    }

    /**
     * 获取 OneDrive 驱动 id
     * @return OneDrive 驱动 id
     */
    private String getDriveId() {
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

    @Override
    public List<FileInfoVo> getFileList(@NonNull String folderPath) {
        if (StrUtil.isBlank(folderPath)) {
            folderPath = initParam.getMountPath();
        }
        try {
            List<FileInfoVo> list = new ArrayList<>();
            return handleFileList(list, folderPath);
        } catch (Exception e) {
            throw new XanaduException("获取 OneDrive 文件列表失败！");
        }
    }

    @Override
    public FileInfoVo getFileInfo(@NonNull String filePath, String fileName) {
        try {
            String url = OneDriveConstants.DRIVE_FILE_URL.replace("{path}", filePath);
            JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                    .addHeader("Authorization", getAccessToken())
                    .get().getBody().toString());
            log.info("获取 OneDrive 文件信息结果：{}", result);
            // TODO accessToken 过期处理
            return getConvertFileInfo(result, filePath);
        } catch (Exception e) {
            throw new XanaduException("获取 OneDrive 文件信息失败！");
        }
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

    @Override
    public String getUploadSession(String path) {
        String url = OneDriveConstants.DRIVE_UPLOAD_SESSION_URL.replace("{path}", path);
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .post().getBody().toString());
        return result.getStr("uploadUrl");
    }

    /**
     * 刷新 token 并返回新的 token
     * @return 新的 token
     */
    private String getAccessToken() {
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
            throw new XanaduException(e.getMessage());
        }
    }

    /**
     * 处理 OneDrive 返回的文件信息 JSON 数据
     * @param jsonObject OneDrive 返回的文件信息 JSON 数据
     * @param filePath 文件路径
     * @return 文件信息
     */
    private FileInfoVo getConvertFileInfo(JSONObject jsonObject, String filePath) {
        FileInfoVo fileInfoVo = new FileInfoVo();
        fileInfoVo.setName(jsonObject.getStr("name"));
        fileInfoVo.setSize(jsonObject.getLong("size"));
        fileInfoVo.setLastModifiedDateTime(jsonObject.getLocalDateTime("lastModifiedDateTime", LocalDateTime.MIN));
        fileInfoVo.setType("file");
        fileInfoVo.setPath(filePath);
        fileInfoVo.setFile(jsonObject.getJSONObject("file"));
        // 设置文件下载地址
        fileInfoVo.setUrl(jsonObject.getStr("@microsoft.graph.downloadUrl"));
        return fileInfoVo;
    }

    /**
     * 处理 OneDrive 返回的文件列表 JSON 数据
     * @param jsonArray OneDrive 返回的文件列表 JSON 数据
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> getConvertFileList(@NonNull JSONArray jsonArray, String folderPath) {
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
            fileInfoVo.setFile(jsonObject.getJSONObject("file"));
            list.add(fileInfoVo);
        }
        return list;
    }

    /**
     * 获取文件列表
     * @param list 文件列表
     * @param folderPath 文件夹路径
     * @return 文件列表
     */
    private List<FileInfoVo> handleFileList(List<FileInfoVo> list, @NonNull String folderPath) {
        String url = OneDriveConstants.DRIVE_ITEM_URL.replace("{drive-id}", getDriveId())
                .replace("{path-relative-to-root}", folderPath);
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .get().getBody().toString());
        log.info("获取 OneDrive 文件列表结果：{}", result);
        JSONArray jsonArray = result.getJSONArray("value");
        // TODO accessToken 过期处理
        if (Objects.nonNull(jsonArray)) {
            // 数据处理
            list.addAll(getConvertFileList(jsonArray, folderPath));
        }
        if (StrUtil.isNotBlank(result.getStr("@odata.nextLink"))) {
            // 如果集合超出默认页面大小（200 项），则在响应中返回 @odata.nextLink 属性以指示有更多项可用，并提供下一页项目的请求 URL。
            handleFileListNext(list, folderPath, result.getStr("@odata.nextLink"));
        }
        return list;
    }

    /**
     * 获取下一级文件列表
     * @param list 文件列表
     * @param folderPath 文件夹路径
     * @param nextLink 下一级文件列表请求地址
     * @return 文件列表
     */
    private List<FileInfoVo> handleFileListNext(List<FileInfoVo> list, @NonNull String folderPath, @NonNull String nextLink) {
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(nextLink)
                .addHeader("Authorization", getAccessToken())
                .get().getBody().toString());
        log.info("获取 OneDrive 文件列表结果：{}", result);
        JSONArray jsonArray = result.getJSONArray("value");
        // TODO accessToken 过期处理
        if (Objects.nonNull(jsonArray)) {
            // 数据处理
            list.addAll(getConvertFileList(jsonArray, folderPath));
        }
        if (StrUtil.isNotBlank(result.getStr("@odata.nextLink"))) {
            handleFileListNext(list, folderPath, result.getStr("@odata.nextLink"));
        }
        return list;
    }

}
