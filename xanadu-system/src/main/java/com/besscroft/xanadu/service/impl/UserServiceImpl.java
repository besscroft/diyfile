package com.besscroft.xanadu.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.constant.RoleConstants;
import com.besscroft.xanadu.common.constant.SystemConstants;
import com.besscroft.xanadu.common.converter.UserConverterMapper;
import com.besscroft.xanadu.common.entity.User;
import com.besscroft.xanadu.common.exception.XanaduException;
import com.besscroft.xanadu.common.param.user.UserAddParam;
import com.besscroft.xanadu.common.param.user.UserUpdateParam;
import com.besscroft.xanadu.mapper.UserMapper;
import com.besscroft.xanadu.service.UserService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/15 14:34
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public SaTokenInfo login(String username, String password, Boolean isRememberMe) {
        User user = this.baseMapper.selectByUsername(username);
        Assert.notNull(user, "账号或密码错误！");
        log.info("用户发起登录请求:{}", username);
        if (!Objects.equals(SaSecureUtil.sha256(password), user.getPassword()))
            throw new XanaduException("账号或密码错误！");
        // 登录
        StpUtil.login(user.getId());
        // 设置最后登录时间
        user.setLoginTime(LocalDateTime.now());
        this.updateById(user);
        log.info("登录成功:{}", username);
        return StpUtil.getTokenInfo();
    }

    @Override
    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "旅行者");
        map.put("avatar", "https://besscroft.com/uploads/avatar.jpeg");
        return map;
    }

    @Override
    public List<User> userPage(Integer pageNum, Integer pageSize, String role) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // TODO 适配后需改成上下文中获取，而不是直接查库
        User user = this.baseMapper.selectById(userId);
        Assert.notNull(user, "用户不存在！");
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new XanaduException("超级管理员不允许被删除！");
        Assert.isTrue(this.baseMapper.deleteById(userId) > 0, "用户删除失败！");
    }

    @Override
    public User getUser(String username) {
        return this.baseMapper.selectByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserAddParam param) {
        User user = UserConverterMapper.INSTANCE.AddParamToUser(param);
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new XanaduException("违反规则！超级管理员角色不允许被添加！");
        user.setStatus(SystemConstants.STATUS_NO);
        user.setPassword(SaSecureUtil.sha256(param.getPassword().trim()));
        Assert.isTrue(this.baseMapper.insert(user) > 0, "新增用户失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateParam param) {
        User user = UserConverterMapper.INSTANCE.UpdateParamToUser(param);
        User oldUser = this.baseMapper.selectById(user.getId());
        if ((!Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN) && Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
                || (Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN) && !Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN)))
            throw new XanaduException("违反规则！更新用户失败！");
        Assert.isTrue(this.baseMapper.updateById(user) > 0, "更新用户失败！");
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        User user = this.baseMapper.selectById(id);
        Assert.notNull(user, "用户不存在！");
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new XanaduException("超级管理员不允许被禁用！");
        user.setStatus(status);
        Assert.isTrue(this.baseMapper.updateById(user) > 0, "更新用户状态失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String oldPassword, String newPassword) {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        String password = this.baseMapper.selectPasswordById(userId);
        if (!Objects.equals(SaSecureUtil.sha256(oldPassword), password))
            throw new XanaduException("旧密码错误！");
        String sha256Pwd = SaSecureUtil.sha256(newPassword);
        this.baseMapper.updatePasswordById(userId, sha256Pwd);
    }

}
