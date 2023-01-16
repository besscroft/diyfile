package com.besscroft.xanadu.common.param.user;

import lombok.Data;

/**
 * @Description 用户修改密码请求参数
 * @Author Bess Croft
 * @Date 2023/1/16 15:15
 */
@Data
public class UserUpdatePwdParam {

    /** 旧密码 */
    private String oldPassword;

    /** 新密码 */
    private String newPassword;

}
