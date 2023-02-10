package com.besscroft.diyfile.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 服务器信息
 * @Author Bess Croft
 * @Date 2022/12/19 14:10
 */
@Data
@Schema(title = "服务器信息")
public class SystemInfo {

    /** 服务器名称 */
    @Schema(title = "服务器名称", type = "String")
    private String computerName;

    /** 服务器 ip */
    @Schema(title = "服务器 ip", type = "String")
    private String computerIp;

    /** 项目路径 */
    @Schema(title = "项目路径", type = "String")
    private String userDir;

    /** 操作系统 */
    @Schema(title = "操作系统", type = "String")
    private String osName;

    /** 系统架构 */
    @Schema(title = "系统架构", type = "String")
    private String osArch;

}
