package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.dto.ServerInfo;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.result.AjaxResult;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.service.SystemConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 系统监控
 * @Author Bess Croft
 * @Date 2022/12/19 14:25
 */
@Tag(name = "系统监控")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final SystemConfigService systemConfigService;

    @Operation(summary = "系统信息接口")
    @GetMapping("/serverInfo")
    public CommonResult<ServerInfo> serverInfo() {
        return CommonResult.success(ServerInfo.serverInfo());
    }

    @Operation(summary = "统计信息接口")
    @GetMapping("/getTotalInfo")
    public AjaxResult getTotalInfo() {
        return AjaxResult.success(systemConfigService.getTotalInfo());
    }

    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
            },
            mode = SaMode.OR
    )
    @Operation(summary = "获取备份数据")
    @GetMapping("/getBackupFile")
    public AjaxResult getBackupFile() {
        try {
            return AjaxResult.success("success", systemConfigService.getBackupJsonString());
        } catch (JsonProcessingException e) {
            throw new DiyFileException("获取备份数据失败！");
        }
    }

}
