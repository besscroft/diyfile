package com.besscroft.diyfile.common.param.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 获取下载文件参数
 * @Author Bess Croft
 * @Date 2023/4/22 13:36
 */
@Data
@Schema(name = "获取下载文件参数")
public class GetDownloadFileParam {

    /** 存储 key */
    @Schema(title = "存储 key", type = "String")
    @NotBlank(message = "存储 key 不能为空")
    private String storageKey;

    /** 地址 */
    @Schema(title = "地址", type = "String")
    @NotBlank(message = "地址不能为空")
    private String path;

}
