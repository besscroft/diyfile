package com.besscroft.xanadu.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.User;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/15 14:33
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return Token 相关参数
     */
    SaTokenInfo login(String username, String password);

}
