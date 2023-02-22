package com.besscroft.diyfile.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.common.constant.FileConstants;
import com.besscroft.diyfile.common.constant.storage.OneDriveConstants;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.init.OneDriveParam;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.storage.service.base.AbstractOneDriveBaseService;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.retry.support.RetryTemplate;
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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OneDriveServiceImpl extends AbstractOneDriveBaseService<OneDriveParam> {

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
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
        if (result.getStatus() != 200)
            throw new DiyFileException("文件重命名失败！");
    }

    @Override
    public void deleteItem(String filePath) {
        String url = OneDriveConstants.DRIVER_ITEM_OPERATOR_URL.replace("{path}", filePath);
        HttpResult result = OkHttps.sync(url)
                .addHeader("Authorization", getAccessToken())
                .delete();
        if (result.getStatus() != 204)
            throw new DiyFileException("删除文件失败！");
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
            if (jsonObject.containsKey(FileConstants.FILE)) {
                fileInfoVo.setType(FileConstants.FILE);
                // 设置文件下载地址
                fileInfoVo.setUrl(jsonObject.getStr("@microsoft.graph.downloadUrl"));
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

}
