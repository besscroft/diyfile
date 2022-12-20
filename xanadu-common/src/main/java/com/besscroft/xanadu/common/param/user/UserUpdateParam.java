package com.besscroft.xanadu.common.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 更新用户请求参数
 * @Author Bess Croft
 * @Date 2022/12/20 14:32
 */
@Data
@Schema(title = "更新用户请求参数")
public class UserUpdateParam {

    @Schema(title = "用户 id", type = "Long", required = true)
    @NotNull(message = "用户 id 不能为空！")
    private Long id;

    @Schema(title = "用户名", type = "String", required = true)
    @NotBlank(message = "用户名不能为空！")
    private String username;

    @Schema(title = "密码", type = "String", required = true)
    @NotBlank(message = "密码不能为空！")
    private String password;

    @Schema(title = "头像(地址)", type = "String")
    private String avatar;

    @Schema(title = "角色", type = "String")
    private String role;

    @Schema(title = "邮箱", type = "String")
    private String email;

    @Schema(title = "昵称", type = "String")
    private String name;

    @Schema(title = "真实姓名", type = "String")
    private String realName;

    @Schema(title = "手机", type = "String")
    private String telephone;

    @Schema(title = "生日", type = "Date")
    private LocalDateTime birthday;

    @Schema(title = "性别", type = "Integer")
    private Integer sex;

    @Schema(title = "备注", type = "String")
    private String remark;

    @Schema(title = "帐号启用状态：0->禁用；1->启用", type = "Integer")
    @NotBlank(message = "账号启用状态不能为空！")
    private Integer status;

}
