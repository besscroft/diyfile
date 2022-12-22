package com.besscroft.xanadu.common.param.storage;

import com.besscroft.xanadu.common.entity.StorageConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Description 驱动更新请求参数
 * @Author Bess Croft
 * @Date 2022/12/21 14:53
 */
@Data
public class StorageUpdateParam {

    @Schema(title = "存储 id", type = "Long")
    @NotNull(message = "存储id不能为空！")
    private Long id;

    @Schema(title = "存储名称", type = "String")
    @NotBlank(message = "存储名称不能为空！")
    private String name;

    @Schema(title = "存储类型", type = "Integer")
    @NotNull(message = "存储类型不能为空！")
    private Integer type;

    @Schema(title = "存储启用状态：0->禁用；1->启用", type = "Integer")
    @NotNull(message = "启用状态不能为空！")
    private Integer enable;

    @Schema(title = "备注", type = "String")
    private String remark;

    private List<StorageConfig> configList;

}
