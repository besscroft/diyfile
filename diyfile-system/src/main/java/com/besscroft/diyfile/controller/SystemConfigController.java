package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.diyfile.common.constant.MessageConstants;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.entity.SystemConfig;
import com.besscroft.diyfile.common.param.system.SystemUpdateConfigParam;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public CommonResult<List<SystemConfig>> getConfig(){
        return CommonResult.success(systemConfigService.getConfig());
    }

    @GetMapping("/getSiteTitle")
    @SaIgnore
    @Operation(summary = "获取网站标题")
    public CommonResult<String> getSiteTitle() {
        String siteTitle = systemConfigService.getSiteTitle();
        return CommonResult.success(MessageConstants.SUCCESS, siteTitle);
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
    public CommonResult<Map<String, String>> getSiteConfig() {
        return CommonResult.success(systemConfigService.getSiteConfig());
    }

    @GetMapping("/getBeian")
    @SaIgnore
    @Operation(summary = "获取备案信息")
    public CommonResult<String> getBeian() {
        String beian = systemConfigService.getBeian();
        return CommonResult.success(MessageConstants.SUCCESS, beian);
    }

    @PutMapping("/updateConfig")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @Operation(summary = "更新配置接口")
    public CommonResult<Void> updateConfig(@RequestBody @Valid SystemUpdateConfigParam param) {
        systemConfigService.updateConfig(param.getConfigKey(), param.getConfigValue());
        return CommonResult.success(MessageConstants.SUCCESS);
    }

    @GetMapping("/getBarkId")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @Operation(summary = "获取 Bark 推送 id")
    public CommonResult<String> getBarkId() {
        String barkId = systemConfigService.getBarkId();
        return CommonResult.success(MessageConstants.SUCCESS, barkId);
    }

    @GetMapping("/getBarkStatus")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @Operation(summary = "获取 Bark 推送状态")
    public CommonResult<Integer> getBarkStatus() {
        Integer barkStatus = systemConfigService.getBarkStatus();
        return CommonResult.success(MessageConstants.SUCCESS, barkStatus);
    }

}
