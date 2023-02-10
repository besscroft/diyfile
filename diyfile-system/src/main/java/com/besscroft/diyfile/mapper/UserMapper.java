package com.besscroft.diyfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.diyfile.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 用户 Mapper 接口
 * @Author Bess Croft
 * @Date 2022/12/15 14:32
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 列表查询
     * @param role 角色标识
     * @return 用户列表
     */
    List<User> selectPage(@Param("role") String role);

    /**
     * 查询用户的密码
     * @param id 用户 id
     * @return 用户密码
     */
    String selectPasswordById(@Param("id") Long id);

    /**
     * 更新用户密码
     * @param id 用户 id
     * @param password 加密后的密码
     * @return 变更行数
     */
    int updatePasswordById(@Param("id") Long id,
                           @Param("password") String password);

}
