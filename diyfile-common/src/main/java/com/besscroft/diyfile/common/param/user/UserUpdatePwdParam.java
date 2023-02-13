package com.besscroft.diyfile.common.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 用户修改密码请求参数
 * @Author Bess Croft
 * @Date 2023/1/16 15:15
 */
@Data
@Schema(title = "用户修改密码请求参数")
public class UserUpdatePwdParam {

    /** 用户 id */
    @Schema(title = "用户 id", type = "Long")
    private Long userId;

    /** 是否为自己的密码修改 */
    @Schema(title = "是否为自己的密码修改", type = "Boolean")
    private Boolean isSelf;

    /** 旧密码 */
    @Schema(title = "旧密码", type = "String")
    @NotBlank(message = "旧密码不能为空!")
    private String oldPassword;

    /** 新密码 */
    @Schema(title = "新密码", type = "String")
    @NotBlank(message = "新密码不能为空!")
    private String newPassword;

}
