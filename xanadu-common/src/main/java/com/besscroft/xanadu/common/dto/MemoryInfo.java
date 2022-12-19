package com.besscroft.xanadu.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 内存信息
 * @Author Bess Croft
 * @Date 2022/12/19 14:08
 */
@Data
@Schema(title = "内存信息")
public class MemoryInfo {

    /** 内存总量 */
    @Schema(title = "内存总量", type = "Long")
    private Long total;

    /** 已用内存 */
    @Schema(title = "已用内存", type = "Long")
    private Long used;

    /** 剩余内存 */
    @Schema(title = "剩余内存", type = "Long")
    private Long free;

}
