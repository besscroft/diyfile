package com.besscroft.xanadu.common.vo;

import com.besscroft.xanadu.common.dto.StorageConfigDto;
import com.besscroft.xanadu.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description 存储详情返回参数
 * @Author Bess Croft
 * @Date 2022/12/28 21:17
 */
@Data
@Schema(title = "存储详情返回参数")
@EqualsAndHashCode(callSuper = true)
public class StorageInfoVo extends BaseEntity {

    @Schema(title = "存储 id", type = "Long")
    private Long id;

    @Schema(title = "存储名称", type = "String")
    private String name;

    @Schema(title = "存储类型", type = "Integer")
    private Integer type;

    @Schema(title = "存储启用状态：0->禁用；1->启用", type = "Integer")
    private Integer enable;

    @Schema(title = "备注", type = "String")
    private String remark;

    @Schema(title = "存储配置参数列表", type = "List")
    private List<StorageConfigDto> configList;

}
