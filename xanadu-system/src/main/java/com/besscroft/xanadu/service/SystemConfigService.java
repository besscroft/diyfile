package com.besscroft.xanadu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.SystemConfig;

import java.util.List;
import java.util.Map;

/**
 * @Description
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

}
