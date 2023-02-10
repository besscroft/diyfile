package com.besscroft.diyfile.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.besscroft.diyfile.common.entity.User;
import com.besscroft.diyfile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description Sa-Token 自定义权限验证接口扩展
 * @Author Bess Croft
 * @Date 2023/1/15 19:56
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     * @param loginId 登录用户 id
     * @param loginType 登录用户类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本项目暂时不需要，所以返回空集合
        return CollUtil.newArrayList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     * @param loginId 登录用户 id
     * @param loginType 登录用户类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // TODO 配置缓存
        User user = userService.getUserById(Long.parseLong(loginId.toString()));
        List<String> list = new ArrayList<>();
        list.add(user.getRole());
        return list;
    }

}
