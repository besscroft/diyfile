package com.besscroft.xanadu.controller;

import com.besscroft.xanadu.common.result.AjaxResult;
import com.besscroft.xanadu.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 系统配置接口
 * @Author Bess Croft
 * @Date 2023/1/7 19:10
 */
@RequestMapping("/systemConfig")
@RestController
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping("/getConfig")
    @Operation(summary = "获取系统配置")
    public AjaxResult getConfig(){
        return AjaxResult.success(systemConfigService.getConfig());
    }

    @GetMapping("/getSiteTitle")
    @Operation(summary = "获取网站标题")
    public AjaxResult getSiteTitle() {
        String siteTitle = systemConfigService.getSiteTitle();
        return AjaxResult.success("操作成功！", siteTitle);
    }

    @GetMapping("/getSiteConfig")
    @Operation(summary = "获取网站配置")
    public AjaxResult getSiteConfig() {
        return AjaxResult.success(systemConfigService.getSiteConfig());
    }

}
