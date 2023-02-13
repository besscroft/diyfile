package com.besscroft.diyfile.common.param.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 获取文件信息参数
 * @Author Bess Croft
 * @Date 2023/1/28 21:06
 */
@Data
@Schema(title = "获取文件信息参数")
public class GetFileInfoParam {

    @NotBlank(message = "存储 key 不能为空！")
    @Schema(title = "存储 key", type = "String")
    private String storageKey;

    @Schema(title = "文件路径", type = "String")
    private String filePath;

    @Schema(title = "文件名称", type = "String")
    private String fileName;

}
