package com.besscroft.xanadu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.xanadu.common.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/15 14:32
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户信息
     * @param username 用户名
     * @return
     */
    User selectByUsername(@Param("username") String username);

}
