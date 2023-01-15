package com.besscroft.xanadu.controller;

import com.besscroft.xanadu.common.param.system.SystemUpdateConfigParam;
import com.besscroft.xanadu.common.result.AjaxResult;
import com.besscroft.xanadu.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getBeian")
    @Operation(summary = "获取备案信息")
    public AjaxResult getBeian() {
        String beian = systemConfigService.getBeian();
        return AjaxResult.success("操作成功！", beian);
    }

    @PutMapping("/updateConfig")
    @Operation(summary = "更新配置接口")
    public AjaxResult updateConfig(@RequestBody @Valid SystemUpdateConfigParam param) {
        systemConfigService.updateConfig(param.getConfigKey(), param.getConfigValue());
        return AjaxResult.success("操作成功！");
    }

}