package com.besscroft.xanadu.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 存储配置参数
 * @Author Bess Croft
 * @Date 2022/12/28 21:18
 */
@Data
@Schema(title = "存储配置参数")
public class StorageConfigDto {

    @Schema(title = "存储配置 id", type = "Long")
    private Long id;

    @Schema(title = "存储id", type = "Long")
    private Long storageId;

    @Schema(title = "存储配置名称", type = "String")
    private String name;

    @Schema(title = "存储配置键", type = "String")
    private String key;

    @Schema(title = "存储配置值", type = "String")
    private String value;

    @Schema(title = "存储配置描述", type = "String")
    private String description;

}
