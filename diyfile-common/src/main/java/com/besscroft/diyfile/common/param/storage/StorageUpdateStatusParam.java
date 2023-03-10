package com.besscroft.diyfile.common.param.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * @Description 更新存储状态请求参数
 * @Author Bess Croft
 * @Date 2023/1/13 19:35
 */
@Data
@Schema(title = "更新存储状态请求参数")
public class StorageUpdateStatusParam {

    /** 存储 id */
    @Schema(title = "存储 id", type = "Long")
    @NotNull(message = "存储 id 不能为空")
    private Long storageId;

    /** 存储启用状态：0->禁用；1->启用 */
    @Schema(title = "存储启用状态：0->禁用；1->启用", type = "Integer")
    @NotNull(message = "存储启用状态不能为空")
    private Integer status;

}
