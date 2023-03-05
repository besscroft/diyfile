package com.besscroft.diyfile.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.diyfile.common.constant.CacheConstants;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.constant.SystemConstants;
import com.besscroft.diyfile.common.converter.UserConverterMapper;
import com.besscroft.diyfile.common.entity.User;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.user.UserAddParam;
import com.besscroft.diyfile.common.param.user.UserUpdateParam;
import com.besscroft.diyfile.mapper.UserMapper;
import com.besscroft.diyfile.message.PushService;
import com.besscroft.diyfile.service.SystemConfigService;
import com.besscroft.diyfile.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 用户服务实现类
 * @Author Bess Croft
 * @Date 2022/12/15 14:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PushService pushService;
    private final SystemConfigService systemConfigService;
    private final Cache<String, Object> caffeineCache;

    @Override
    public SaTokenInfo login(String username, String password) {
        ServletUriComponentsBuilder request = ServletUriComponentsBuilder.fromCurrentRequest();
        log.info("用户发起登录请求:{}，请求 uri 为：{}", username, request.toUriString());
        User user = this.baseMapper.selectByUsername(username);
        Assert.notNull(user, "账号或密码错误！");
        if (Objects.equals(user.getStatus(), SystemConstants.STATUS_NO))
            throw new DiyFileException(String.format("账号：%s 已被禁用，请联系管理员！", username));
        if (!Objects.equals(SecureUtil.sha256(password), user.getPassword()))
            throw new DiyFileException("账号或密码错误！");
        // 登录
        StpUtil.login(user.getId());
        // 设置最后登录时间
        user.setLoginTime(LocalDateTime.now());
        this.updateById(user);
        CompletableFuture.runAsync(() -> {
            log.info("登录成功:{}", username);
            pushService.pushBark(systemConfigService.getBarkId(), String.format("用户：%s 在：%s 登录 DiyFile",
                    user.getName(), LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN)));
        });
        return StpUtil.getTokenInfo();
    }

    @Override
    public Map<String, Object> info() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = getCacheUserById(userId);
        Assert.notNull(user, "暂未登录！");
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("role", user.getRole());
        return map;
    }

    @Override
    public List<User> userPage(Integer pageNum, Integer pageSize, String role) {
        PageHelper.startPage(pageNum, pageSize);
        return this.baseMapper.selectPage(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {
            CacheConstants.USER,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void deleteUser(Long userId) {
        User user = getCacheUserById(userId);
        Assert.notNull(user, "用户不存在！");
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new DiyFileException("超级管理员不允许被删除！");
        Assert.isTrue(this.baseMapper.deleteById(userId) > 0, "用户删除失败！");
    }

    @Override
    public User getUser(String username) {
        return this.baseMapper.selectByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return getCacheUserById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserAddParam param) {
        User user = UserConverterMapper.INSTANCE.AddParamToUser(param);
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new DiyFileException("违反规则！超级管理员角色不允许被添加！");
        user.setStatus(SystemConstants.STATUS_NO);
        user.setPassword(SecureUtil.sha256(param.getPassword().trim()));
        Assert.isTrue(this.baseMapper.insert(user) > 0, "新增用户失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {
            CacheConstants.USER,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void updateUser(UserUpdateParam param) {
        User user = UserConverterMapper.INSTANCE.UpdateParamToUser(param);
        User oldUser = this.baseMapper.selectById(user.getId());
        // 如果原来不是超级管理员，现在是超级管理员，或者原来是超级管理员，现在不是超级管理员，抛出异常
        if ((!Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN) && Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
                || (Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN) && !Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN)))
            throw new DiyFileException("违反规则！更新用户失败！");
        // 非管理员只能修改自己的信息
        if (!(Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN) || Objects.equals(oldUser.getRole(), RoleConstants.PLATFORM_ADMIN))
                && !Objects.equals(oldUser.getId(), user.getId()))
            throw new DiyFileException("违反规则！更新用户失败！");
        Assert.isTrue(this.baseMapper.updateById(user) > 0, "更新用户失败！");
    }

    @Override
    @CacheEvict(value = {
            CacheConstants.USER,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void updateStatus(Long id, Integer status) {
        User user = this.baseMapper.selectById(id);
        Assert.notNull(user, "用户不存在！");
        if (Objects.equals(user.getRole(), RoleConstants.PLATFORM_SUPER_ADMIN))
            throw new DiyFileException("超级管理员不允许被禁用！");
        user.setStatus(status);
        Assert.isTrue(this.baseMapper.updateById(user) > 0, "更新用户状态失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {
            CacheConstants.USER,
            CacheConstants.STATISTICS
    }, allEntries = true)
    public void updatePassword(Long userId, Boolean isSelf,String oldPassword, String newPassword) {
        if (isSelf) {
            // 如果是自己要修改密码，那么就从上下文中获取用户 id
            userId = StpUtil.getLoginIdAsLong();
        }
        String password = this.baseMapper.selectPasswordById(userId);
        if (!Objects.equals(SecureUtil.sha256(oldPassword), password))
            throw new DiyFileException("旧密码错误！");
        String sha256Pwd = SecureUtil.sha256(newPassword);
        this.baseMapper.updatePasswordById(userId, sha256Pwd);
    }

    /**
     * 从缓存中获取用户信息
     * @param userId 用户 id
     * @return 用户信息
     */
    private User getCacheUserById(Long userId) {
        return (User) Optional.ofNullable(caffeineCache.getIfPresent(CacheConstants.USER + userId))
                .orElseGet(() -> {
                    User user = this.baseMapper.selectById(userId);
                    caffeineCache.put(CacheConstants.USER + userId, user);
                    return user;
                });
    }

}
