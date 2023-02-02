package com.besscroft.xanadu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.SystemConfig;

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

}
