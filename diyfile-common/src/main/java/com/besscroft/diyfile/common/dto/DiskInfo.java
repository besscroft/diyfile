package com.besscroft.diyfile.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 文件系统信息
 * @Author Bess Croft
 * @Date 2022/12/19 14:05
 */
@Data
@Schema(title = "文件系统信息")
public class DiskInfo {

    /** 盘符路径 */
    @Schema(title = "盘符路径", type = "String")
    private String dirName;

    /** 盘符类型 */
    @Schema(title = "盘符类型", type = "String")
    private String sysTypeName;

    /** 文件类型 */
    @Schema(title = "文件类型", type = "String")
    private String typeName;

    /** 总空间 */
    @Schema(title = "总空间", type = "String")
    private String total;

    /** 剩余空间 */
    @Schema(title = "剩余空间", type = "String")
    private String free;

    /** 使用量 */
    @Schema(title = "使用量", type = "String")
    private String used;

    /** 资源使用率 */
    @Schema(title = "资源使用率", type = "Double")
    private Double usage;

}
