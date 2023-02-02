package com.besscroft.xanadu.storage.service.base;

import cn.hutool.core.util.StrUtil;
import com.besscroft.xanadu.common.param.FileInitParam;
import com.besscroft.xanadu.common.vo.FileInfoVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 文件基础服务抽象类
 * @Author Bess Croft
 * @Date 2023/1/20 11:16
 */
public abstract class AbstractFileBaseService<T extends FileInitParam> implements FileBaseService {

    /** 初始化参数 */
    public T initParam;

    /** 存储 id */
    protected Long storageId;

    /** 存储 key */
    protected String storageKey;

    /** 存储名称 */
    private String name;

    /** 初始化状态 */
    protected boolean initialized = false;

    /** 基础服务初始化方法 */
    public abstract void init();

    @Override
    public abstract String getFileDownloadUrl(String fileName, String filePath);

    @Override
    public abstract List<FileInfoVo> getFileList(String folderPath);

    /**
     * 获取文件信息
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public abstract FileInfoVo getFileInfo(String filePath, String fileName);

    /**
     * 创建项目(在指定的驱动器中创建一个 driveItem)
     * @param folderPath 文件夹路径
     * @param fileName 文件名
     */
    public abstract void createItem(String folderPath, String fileName);

    /**
     * 更新项目(更新驱动器中的 driveItem)
     * @param folderPath 文件夹路径
     * @param fileName 文件名
     * @param newName 新文件名
     */
    public abstract void updateItem(String folderPath, String fileName, String newName);

    /**
     * 删除项目(删除驱动器中的 driveItem)
     * @param folderPath 文件夹路径
     * @param fileName 文件名
     */
    public abstract void deleteItem(String folderPath, String fileName);

    /**
     * 上传项目(将内容上传到指定的驱动器中)
     * @param folderPath 文件夹路径
     * @param fileName 文件名
     */
    public abstract void uploadItem(String folderPath, String fileName);

    public void setStorageId(Long storageId) {
        if (Objects.nonNull(this.storageId))
            throw new IllegalStateException("当前存储服务不允许重复初始化！");
        this.storageId = storageId;
    }

    public void setStorageKey(String storageKey) {
        if (Objects.nonNull(this.storageKey))
            throw new IllegalStateException("当前存储服务不允许重复初始化！");
        this.storageKey = storageKey;
    }

    public void setName(String name) {
        if (StrUtil.isNotBlank(this.name))
            throw new IllegalStateException("当前存储服务不允许重复初始化！");
        this.name = name;
    }

    public void setInitParam(T initParam) {
        if (Objects.nonNull(this.initParam))
            throw new IllegalStateException("当前存储服务不允许重复初始化！");
        this.initParam = initParam;
    }

    /**
     * 获取初始化状态
     * @return 初始化状态
     */
    public boolean getInitialized() {
        return initialized;
    }

    /**
     * 获取初始化配置
     * @return 初始化配置
     */
    public T getInitParam() {
        return initParam;
    }

    /**
     * 获取存储 id
     * @return 存储 id
     */
    public Long getStorageId() {
        return storageId;
    }

    /**
     * 获取存储 key
     * @return 存储 key
     */
    public String getStorageKey() {
        return storageKey;
    }

    /**
     * 获取存储名称
     * @return 存储名称
     */
    public String getName() {
        return name;
    }

}
