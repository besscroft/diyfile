package com.besscroft.xanadu.common.constant;

/**
 * @Description 角色常量
 * @Author Bess Croft
 * @Date 2023/1/7 16:28
 */
public interface RoleConstants {

    /** 超级管理员：管理系统平台中的所有资源，但不允许删除自己 */
    String PLATFORM_SUPER_ADMIN = "platform-super-admin";

    /** 平台管理员：管理系统平台中的大部分资源，但不允许删除用户 */
    String PLATFORM_ADMIN = "platform-admin";

    /** 平台运维员：管理平台中的运维资源，查看系统平台中的大部分资源 */
    String PLATFORM_SELF_PROVISIONER = "platform-self-provisioner";

    /** 平台观察员：查看系统平台中的所有非机密资源 */
    String PLATFORM_VIEW = "platform-view";

    /** 游客：查看系统平台中除后台外所有非机密资源 */
    String PLATFORM_VISITOR = "platform-visitor";

}
