package com.besscroft.diyfile.common.param.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 根据 key 获取文件列表参数
 * @Author Bess Croft
 * @Date 2023/1/28 16:59
 */
@Data
public class GetItemByKeyParam {

    @NotBlank(message = "存储 key 不能为空！")
    @Schema(title = "存储 key", type = "String")
    private String storageKey;

    @Schema(title = "文件夹路径", type = "String")
    private String folderPath;

}
