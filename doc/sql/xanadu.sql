/*
 Navicat Premium Data Transfer

 Source Server         : xanadu
 Source Server Type    : MySQL
 Source Server Version : 101002 (10.10.2-MariaDB-1:10.10.2+maria~ubu2204)
 Source Host           : 140.238.17.232:30003
 Source Schema         : xanadu

 Target Server Type    : MySQL
 Target Server Version : 101002 (10.10.2-MariaDB-1:10.10.2+maria~ubu2204)
 File Encoding         : 65001

 Date: 07/01/2023 21:20:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage`  (
  `id` bigint NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '存储类型：0->本地存储；1->OneDrive',
  `enable` tinyint NULL DEFAULT 0 COMMENT '存储启用状态：0->禁用；1->启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除：0->删除状态；1->可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storage
-- ----------------------------
INSERT INTO `storage` VALUES (1, '测试本地存储', 0, 1, '测试本地存储用的', 1, 1, '2022-12-29 23:07:48', '2022-12-29 23:08:00', 1);

-- ----------------------------
-- Table structure for storage_config
-- ----------------------------
DROP TABLE IF EXISTS `storage_config`;
CREATE TABLE `storage_config`  (
  `id` bigint NOT NULL,
  `storage_id` bigint NOT NULL COMMENT '存储id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置名称',
  `config_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置键',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储配置描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storage_config
-- ----------------------------
INSERT INTO `storage_config` VALUES (1, 1, '根路径', 'path', 'D:\\app-log', '访问本地存储的根路径');

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '网站标题', 1, 'title', 'Xanadu', '动态加载的网站标题', 1, 1, '2023-01-07 20:49:30', '2023-01-07 19:13:02', 1);
INSERT INTO `system_config` VALUES (2, '测试key', 1, 'test', 'value', '测试用的', 1, 1, '2023-01-07 20:45:50', '2023-01-07 20:26:54', 1);
INSERT INTO `system_config` VALUES (3, '备案号', 1, 'beian', '', '备案号', 1, 1, '2023-01-07 20:53:26', '2023-01-07 20:52:51', 1);

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
  `real_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生日',
  `sex` tinyint NULL DEFAULT NULL COMMENT '性别：1->男；2->女；3->未知',
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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'xanadu', '94edf28c6d6da38fd35d7ad53e485307f89fbeaf120485c8d17a43f323deee71', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-super-admin', NULL, '旅行者', NULL, NULL, NULL, 1, '只要不失去你的崇高，整个世界都会向你敞开。', 1, 1, '2023-01-07 16:38:38', NULL, '2022-12-18 17:07:07', 1, 1);
INSERT INTO `user` VALUES (2, 'test1', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-admin', NULL, '测试', NULL, NULL, NULL, 1, '我超，原！只要不失去你的崇高，整个世界都会向你敞开。只要不失去你的崇高，整个世界都会向你敞开。只要不失去你的崇高，整个世界都会向你敞开。', 1, 1, '2023-01-07 16:42:08', NULL, '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (3, 'test2', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-self-provisioner', NULL, '测试', NULL, NULL, NULL, 1, '我超，原！', 1, 1, '2023-01-07 16:42:10', NULL, '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (4, 'test3', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-view', NULL, '测试', NULL, NULL, NULL, 1, '我超，原！', 1, 1, '2023-01-07 16:38:52', NULL, '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (5, 'test4', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-visitor', NULL, '测试', NULL, NULL, NULL, 1, '我超，原！', 1, 1, '2023-01-07 16:42:17', NULL, '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (6, 'test5', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-visitor', NULL, '测试', NULL, NULL, NULL, 1, '我超，原！', 1, 1, '2023-01-07 16:42:18', NULL, '2022-12-19 17:14:39', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
