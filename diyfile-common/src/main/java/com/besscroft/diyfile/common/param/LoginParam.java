package com.besscroft.diyfile.common.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 登录请求参数
 * @Author Bess Croft
 * @Date 2022/12/15 15:12
 */
@Data
@Schema(title = "登录请求参数")
public class LoginParam {

    /** 用户名 */
    @NotBlank(message = "用户名未填！")
    @Schema(title = "用户名", type = "String", required = true)
    private String username;

    /** 密码 */
    @NotBlank(message = "密码未填！")
    @Schema(title = "密码", type = "String", required = true)
    private String password;

    /** 记住我 */
    @Schema(title = "记住我", type = "Boolean")
    private Boolean isRememberMe;

}
