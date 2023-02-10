package com.besscroft.diyfile.common.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description 新增用户请求参数
 * @Author Bess Croft
 * @Date 2022/12/20 14:25
 */
@Data
@Schema(title = "新增用户请求参数")
public class UserAddParam {

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

    @Schema(title = "手机", type = "String")
    private String telephone;

    @Schema(title = "备注", type = "String")
    private String remark;

}
