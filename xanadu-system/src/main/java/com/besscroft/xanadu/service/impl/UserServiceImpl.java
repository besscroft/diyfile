package com.besscroft.xanadu.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.entity.User;
import com.besscroft.xanadu.common.exception.XanaduException;
import com.besscroft.xanadu.mapper.UserMapper;
import com.besscroft.xanadu.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/15 14:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public SaTokenInfo login(String username, String password) {
        User user = this.baseMapper.selectByUsername(username);
        Assert.notNull(user, "账号或密码错误！");
        if (!Objects.equals(SaSecureUtil.sha256(password), user.getPassword()))
            throw new XanaduException("账号或密码错误！");
        // 登录
//        StpUtil.login(user.getId());
//        // 获取 Token 相关参数
//        return StpUtil.getTokenInfo();
        // TODO satoken 暂未适配 springboot3，先整个假的
        SaTokenInfo info = new SaTokenInfo();
        info.setTokenValue("");
        return info;
    }

}
