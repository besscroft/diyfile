package com.besscroft.xanadu.common.param.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 用户修改密码请求参数
 * @Author Bess Croft
 * @Date 2023/1/16 15:15
 */
@Data
public class UserUpdatePwdParam {

    /** 用户id */
    private Long userId;

    /** 是否为自己的密码修改 */
    private Boolean isSelf;

    /** 旧密码 */
    @NotBlank(message = "旧密码不能为空!")
    private String oldPassword;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空!")
    private String newPassword;

}
