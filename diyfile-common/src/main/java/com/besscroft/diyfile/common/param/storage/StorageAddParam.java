package com.besscroft.diyfile.common.param.storage;

import com.besscroft.diyfile.common.entity.StorageConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Description 存储新增请求参数
 * @Author Bess Croft
 * @Date 2022/12/21 14:53
 */
@Data
@Schema(title = "存储新增请求参数")
public class StorageAddParam {

    @Schema(title = "存储名称", type = "String")
    @NotBlank(message = "存储名称不能为空！")
    private String name;

    @Schema(title = "存储类型", type = "Integer")
    @NotNull(message = "存储类型不能为空！")
    private Integer type;

    @Schema(title = "存储 key", type = "String")
    @NotBlank(message = "存储 key 不能为空！")
    private String storageKey;

    @Schema(title = "备注", type = "String")
    private String remark;

    @Schema(title = "存储配置", type = "List")
    private List<StorageConfig> configList;

}
