package com.besscroft.xanadu.common.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 登录请求参数
 * @Author Bess Croft
 * @Date 2022/12/15 15:12
 */
@Data
public class LoginParam {

    /** 用户名 */
    @NotBlank(message = "用户名未填！")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码未填！")
    private String password;

    /** 记住我 */
    private Boolean isRememberMe;

}
