package com.besscroft.diyfile.controller;

import com.besscroft.diyfile.common.dto.ServerInfo;
import com.besscroft.diyfile.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class MonitorController {

    @Operation(summary = "系统信息接口")
    @GetMapping("/serverInfo")
    public CommonResult<ServerInfo> serverInfo() {
        return CommonResult.success(ServerInfo.serverInfo());
    }

}
