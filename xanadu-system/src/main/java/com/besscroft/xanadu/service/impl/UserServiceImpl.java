package com.besscroft.xanadu.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.entity.User;
import com.besscroft.xanadu.common.exception.XanaduException;
import com.besscroft.xanadu.mapper.UserMapper;
import com.besscroft.xanadu.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

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
        info.setTokenValue(IdUtil.randomUUID());
        return info;
    }

    @Override
    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "旅行者");
        map.put("avatar", "https://besscroft.com/uploads/avatar.jpeg");
        return map;
    }

    @Override
    public List<User> userPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage();
    }

}
