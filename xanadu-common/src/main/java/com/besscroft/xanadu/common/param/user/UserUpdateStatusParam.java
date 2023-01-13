package com.besscroft.xanadu.common.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * @Description 用户状态更新请求参数
 * @Author Bess Croft
 * @Date 2023/1/13 19:17
 */
@Data
public class UserUpdateStatusParam {

    @Schema(title = "用户 id", type = "Long")
    @NotNull(message = "用户 id 不能为空")
    private Long userId;

    @Schema(title = "帐号启用状态：0->禁用；1->启用", type = "Integer")
    @NotNull(message = "帐号启用状态不能为空")
    private Integer status;

}
