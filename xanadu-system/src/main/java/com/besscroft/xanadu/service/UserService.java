package com.besscroft.xanadu.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.besscroft.xanadu.common.entity.User;
import com.besscroft.xanadu.common.param.user.UserAddParam;
import com.besscroft.xanadu.common.param.user.UserUpdateParam;

import java.util.List;
import java.util.Map;

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
    SaTokenInfo login(String username, String password, Boolean isRememberMe);

    /**
     * 获取已登录用户信息
     * @return 组装的用户信息
     */
    Map<String, Object> info();

    /**
     * 用户分页列表
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param role 角色标识
     * @return 用户分页列表数据
     */
    List<User> userPage(Integer pageNum, Integer pageSize, String role);

    /**
     * 用户删除
     * @param userId 用户 id
     */
    void deleteUser(Long userId);

    /**
     * 获取用户信息
     * @param username 用户名
     * @return 用户实体
     */
    User getUser(String username);

    /**
     * 获取用户
     * @param id 用户 id
     * @return
     */
    User getUserById(Long id);

    /**
     * 新增用户
     * @param param 请求参数
     */
    void addUser(UserAddParam param);

    /**
     * 更新用户信息
     * @param param 请求参数
     */
    void updateUser(UserUpdateParam param);

}
