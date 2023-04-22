package com.besscroft.diyfile.common.param.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 获取下载链接参数
 * @Author Bess Croft
 * @Date 2023/4/22 13:35
 */
@Data
@Schema(name = "获取下载链接参数")
public class GetDownloadUrlParam {

    /** 存储 key */
    @Schema(title = "存储 key", type = "String")
    @NotBlank(message = "存储 key 不能为空")
    private String storageKey;

    /** 地址 */
    @Schema(title = "地址", type = "String")
    @NotBlank(message = "地址不能为空")
    private String path;

    /** 完整地址 */
    @Schema(title = "完整地址", type = "String")
    @NotBlank(message = "完整地址不能为空")
    private String fullPath;

}
