package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.param.system.SystemUpdateConfigParam;
import com.besscroft.diyfile.common.result.AjaxResult;
import com.besscroft.diyfile.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 系统配置接口
 * @Author Bess Croft
 * @Date 2023/1/7 19:10
 */
@Tag(name = "系统配置接口")
@RequestMapping("/systemConfig")
@RestController
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping("/getConfig")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER,
                    RoleConstants.PLATFORM_VIEW
            },
            mode = SaMode.OR
    )
    @Operation(summary = "获取系统配置")
    public AjaxResult getConfig(){
        return AjaxResult.success(systemConfigService.getConfig());
    }

    @GetMapping("/getSiteTitle")
    @SaIgnore
    @Operation(summary = "获取网站标题")
    public AjaxResult getSiteTitle() {
        String siteTitle = systemConfigService.getSiteTitle();
        return AjaxResult.success("操作成功！", siteTitle);
    }

    @GetMapping("/getSiteConfig")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER,
                    RoleConstants.PLATFORM_VIEW
            },
            mode = SaMode.OR
    )
    @Operation(summary = "获取网站配置")
    public AjaxResult getSiteConfig() {
        return AjaxResult.success(systemConfigService.getSiteConfig());
    }

    @GetMapping("/getBeian")
    @SaIgnore
    @Operation(summary = "获取备案信息")
    public AjaxResult getBeian() {
        String beian = systemConfigService.getBeian();
        return AjaxResult.success("操作成功！", beian);
    }

    @PutMapping("/updateConfig")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @Operation(summary = "更新配置接口")
    public AjaxResult updateConfig(@RequestBody @Valid SystemUpdateConfigParam param) {
        systemConfigService.updateConfig(param.getConfigKey(), param.getConfigValue());
        return AjaxResult.success("操作成功！");
    }

    @GetMapping("/getBarkId")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @Operation(summary = "获取 Bark 推送 id")
    public AjaxResult getBarkId() {
        String barkId = systemConfigService.getBarkId();
        return AjaxResult.success("操作成功！", barkId);
    }

}
