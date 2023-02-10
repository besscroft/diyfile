package com.besscroft.diyfile.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description CPU 信息
 * @Author Bess Croft
 * @Date 2022/12/19 14:03
 */
@Data
@Schema(title = "CPU 信息")
public class CpuInfo {

    /** 核心数 */
    @Schema(title = "核心数", type = "Integer")
    private Integer cpuNum;

}
