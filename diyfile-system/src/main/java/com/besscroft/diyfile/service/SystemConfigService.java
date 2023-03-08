package com.besscroft.diyfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.diyfile.common.entity.SystemConfig;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

/**
 * @Description 系统配置服务
 * @Author Bess Croft
 * @Date 2023/1/7 19:05
 */
public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 获取系统配置
     * @return 系统配置
     */
    List<SystemConfig> getConfig();

    /**
     * 获取网站标题
     * @return 网站标题
     */
    String getSiteTitle();

    /**
     * 获取网站配置
     * @return 网站配置
     */
    Map<String, String> getSiteConfig();

    /**
     * 获取备案信息
     * @return 备案信息
     */
    String getBeian();

    /**
     * 更新配置
     * @param configKey 配置键
     * @param configValue 配置值
     */
    void updateConfig(String configKey, String configValue);

    /**
     * 获取 Bark 推送 id
     * @return Bark 推送 id
     */
    String getBarkId();

    /**
     * 获取统计信息
     * @return 统计信息
     */
    Map<String, Object> getTotalInfo();

    /**
     * 获取备份 json 字符串
     * @return 备份 json 字符串
     * @throws JsonProcessingException json 处理异常
     */
    String getBackupJsonString() throws JsonProcessingException;

}
