package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.cache.DiyCache;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.constant.storage.OneDriveConstants;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractOneDriveBaseService;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description OneDrive 服务实现类
 * @Author Bess Croft
 * @Date 2023/1/20 15:54
 */
@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OneDriveServiceImpl extends AbstractOneDriveBaseService<OneDriveParam> {

    @Override
    public String getFileDownloadUrl(String fileName, String filePath, String fullPath) {
        // 判断第一个字符是否为 /
        if (!StrUtil.equals(StrUtil.sub(filePath, 0, 1), "/")) {
            filePath = "/" + filePath;
        }
        filePath = PathUtils.handlePath(initParam.getMountPath(), filePath);
        FileInfoVo fileInfo = getFileInfo(filePath, fileName);
        return fileInfo.getUrl();
    }

    @Override
    public ResponseEntity<Resource> getFileResource(String fileName, String filePath) {
        return null;
    }

    @Override
    public List<FileInfoVo> getFileList(@NonNull String folderPath) {
        RetryTemplate template = RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(2000)
                .retryOn(DiyFileException.class)
                .build();
        if (StrUtil.isBlank(folderPath)) {
            folderPath = initParam.getMountPath();
        }
        String finalFolderPath = folderPath;
        List<FileInfoVo> list = new ArrayList<>();
        return template.execute(ctx -> {
            int retryCount = ctx.getRetryCount();
            if (retryCount > 0) {
                log.info("获取 OneDrive 文件列表失败，正在进行第 {} 次重试", retryCount);
                DiyCache.removeDiyKey(CacheConstants.ONEDRIVE_TOKEN + storageId);
                refreshAccessToken();
            }
            try {
                return handleFileList(list, finalFolderPath);
            } catch (Exception e) {
                throw new DiyFileException("获取 OneDrive 文件列表失败！");
            }
        });
    }

    @Override
    public FileInfoVo getFileInfo(@NonNull String filePath, String fileName) {
        String url = OneDriveConstants.DRIVE_FILE_URL.replace("{path}", filePath);
        RetryTemplate template = RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(2000)
                .retryOn(DiyFileException.class)
                .build();
        return template.execute(ctx -> {
            int retryCount = ctx.getRetryCount();
            if (retryCount > 0) {
                log.info("获取 OneDrive 文件信息失败，正在进行第 {} 次重试", retryCount);
                DiyCache.removeDiyKey(CacheConstants.ONEDRIVE_TOKEN + storageId);
                refreshAccessToken();
            }
            try {
                JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                        .addHeader("Authorization", getAccessToken())
                        .get().getBody().toString());
                log.info("获取 OneDrive 文件信息结果：{}", result);
                return getConvertFileInfo(result, filePath);
            } catch (Exception e) {
                throw new DiyFileException("获取 OneDrive 文件信息失败！");
            }
        });
    }

    @Override
    public void createItem(String folderPath, String fileName) {
        throw new DiyFileException("暂不支持创建文件！");
    }

    @Override
    public void updateItem(String filePath, String fileName) {
        String url = OneDriveConstants.DRIVER_ITEM_OPERATOR_URL.replace("{path}", filePath);
        Map<String, String> map = new HashMap<>();
        map.put("name", fileName);
        HttpResult result = OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .setBodyPara(map)
                .patch();
        if (result.getStatus() != 200) {
            throw new DiyFileException("文件重命名失败！");
        }
    }

    @Override
    public void deleteItem(String filePath) {
        String url = OneDriveConstants.DRIVER_ITEM_OPERATOR_URL.replace("{path}", filePath);
        HttpResult result = OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .delete();
        if (result.getStatus() != 204) {
            throw new DiyFileException("删除文件失败！");
        }
    }

    @Override
    public void uploadItem(String folderPath, String fileName) {
        throw new DiyFileException("暂不支持代理上传文件，请通过直链上传！");
    }

    @Override
    public String getUploadSession(String path) {
        String url = OneDriveConstants.DRIVE_UPLOAD_SESSION_URL.replace("{path}", path);
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .post().getBody().toString());
        return result.getStr("uploadUrl");
    }

    @Override
    public void moveItem(String startPath, String endPath) {
        // 移动文件，需要先获取 item-id，@see https://learn.microsoft.com/zh-cn/graph/api/driveitem-move?view=graph-rest-1.0&tabs=http
        String startItemId = getItemId(startPath);
        String endItemId = getItemId(endPath);
        String moveUrl = OneDriveConstants.MOVE_URL.replace("{item-id}", startItemId);
        Map<String, Object> map = new HashMap<>();
        Map<String, String> parentReference = new HashMap<>();
        parentReference.put("id", endItemId);
        map.put("parentReference", parentReference);
        if (!PathUtils.isFolder(startPath)) {
            map.put("name", PathUtils.getFileName(PathUtils.decode(startPath)));
        }
        JSONObject result = JSONUtil.parseObj(OkHttps.sync(moveUrl)
                .addHeader("Authorization", getAccessToken())
                .bodyType("json")
                .addBodyPara(map)
                .patch().getBody().toString());
        log.info("移动文件结果：{}", result);
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
        fileInfoVo.setType(FileConstants.FILE);
        fileInfoVo.setPath(filePath);
        fileInfoVo.setFile(jsonObject.getJSONObject(FileConstants.FILE));
        String url = jsonObject.getStr("@microsoft.graph.downloadUrl");
        // 设置文件下载地址
        fileInfoVo.setUrl(url);
        // 设置代理下载地址
        fileInfoVo.setProxyUrl(getProxyUrl(url));
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
            if (jsonObject.containsKey(FileConstants.FILE)) {
                fileInfoVo.setType(FileConstants.FILE);
                String url = jsonObject.getStr("@microsoft.graph.downloadUrl");
                // 设置文件下载地址
                fileInfoVo.setUrl(url);
                // 设置代理下载地址
                fileInfoVo.setProxyUrl(getProxyUrl(url));
            } else {
                fileInfoVo.setType(FileConstants.FOLDER);
            }
            fileInfoVo.setPath(folderPath);
            fileInfoVo.setFile(jsonObject.getJSONObject(FileConstants.FILE));
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

    /**
     * 获取代理下载地址
     * @param url 原始下载地址
     * @return 代理下载地址
     */
    private String getProxyUrl(String url) {
        if (StrUtil.isBlank(initParam.getProxyUrl())) {
            return null;
        }
        try {
            URI host = URLUtil.getHost(new URL(url));
            return StrUtil.replace(url, host.toString(), initParam.getProxyUrl());
        } catch (MalformedURLException e) {
            log.error("获取代理下载地址失败！", e);
            return null;
        }
    }

}
