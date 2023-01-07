package com.besscroft.xanadu.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Description 存储配置实体
 * @Author Bess Croft
 * @Date 2022/12/18 21:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "storage_config")
@Schema(title = "存储配置实体")
public class StorageConfig {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(title = "存储配置 id", type = "Long")
    private Long id;

    /** 存储id */
    @TableField(value = "storage_id")
    @Schema(title = "存储id", type = "Long")
    private Long storageId;

    /** 存储配置名称 */
    @TableField(value = "name")
    @Schema(title = "存储配置名称", type = "String")
    private String name;

    /** 存储配置键 */
    @TableField(value = "config_key")
    @Schema(title = "存储配置键", type = "String")
    private String configKey;

    /** 存储配置值 */
    @TableField(value = "config_value")
    @Schema(title = "存储配置值", type = "String")
    private String configValue;

    /** 存储配置描述 */
    @TableField(value = "description")
    @Schema(title = "存储配置描述", type = "String")
    private String description;

}
