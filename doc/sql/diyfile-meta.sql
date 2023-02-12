/*
 基础结构表

 Date: 12/02/2023 14:09:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储名称',
  `storage_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储 key，用于标识存储路径头',
  `type` tinyint NULL DEFAULT NULL COMMENT '存储类型：0->本地存储；1->OneDrive',
  `enable` tinyint NULL DEFAULT 0 COMMENT '存储启用状态：0->禁用；1->启用',
  `default_status` tinyint NULL DEFAULT 0 COMMENT '存储默认值状态：0->非默认；1->默认',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除：0->删除状态；1->可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for storage_config
-- ----------------------------
DROP TABLE IF EXISTS `storage_config`;
CREATE TABLE `storage_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `storage_id` bigint NOT NULL COMMENT '存储id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置名称',
  `config_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置键',
  `config_value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统配置名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '配置类型：0->基础配置；1->网站配置',
  `config_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统配置键',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统配置值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统配置描述',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除：0->删除状态；1->可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号(用户名)',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像(地址)',
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '帐号启用状态：0->禁用；1->启用',
  `del` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除：0->删除状态；1->可用状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
