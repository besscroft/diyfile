package com.besscroft.diyfile.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Description 存储实体
 * @Author Bess Croft
 * @Date 2022/12/18 20:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "storage")
@Schema(title = "存储实体")
public class Storage extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(title = "存储 id", type = "Long")
    private Long id;

    /** 存储名称 */
    @TableField(value = "name")
    @Schema(title = "存储名称", type = "String")
    private String name;

    /** 存储 key，用于标识存储路径头 */
    @TableField(value = "storage_key")
    @Schema(title = "存储 key", type = "String")
    private String storageKey;

    /** 存储类型 */
    @TableField(value = "type")
    @Schema(title = "存储类型", type = "Integer")
    private Integer type;

    /** 存储启用状态：0->禁用；1->启用 */
    @TableField(value = "enable")
    @Schema(title = "存储启用状态：0->禁用；1->启用", type = "Integer")
    private Integer enable;

    /** 存储默认值状态：0->非默认；1->默认 */
    @TableField(value = "default_status")
    @Schema(title = "存储默认值状态：0->非默认；1->默认", type = "Integer")
    private Integer defaultStatus;

    /** 备注 */
    @TableField(value = "remark")
    @Schema(title = "备注", type = "String")
    private String remark;

}
