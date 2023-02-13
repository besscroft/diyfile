package com.besscroft.diyfile.common.param.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 删除文件请求参数
 * @Author Bess Croft
 * @Date 2023/2/11 19:58
 */
@Data
@Schema(title = "删除文件请求参数")
public class DeleteFileParam {

    /** 存储 key */
    @Schema(title = "存储 key", type = "String")
    @NotBlank(message = "存储 key 不能为空")
    private String storageKey;

    /** 地址 */
    @Schema(title = "地址", type = "String")
    @NotBlank(message = "地址不能为空")
    private String path;

}
