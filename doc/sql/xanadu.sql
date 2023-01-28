/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100904 (10.9.4-MariaDB-1:10.9.4+maria~ubu2204)
 Source Host           : localhost:3306
 Source Schema         : xanadu

 Target Server Type    : MySQL
 Target Server Version : 100904 (10.9.4-MariaDB-1:10.9.4+maria~ubu2204)
 File Encoding         : 65001

 Date: 28/01/2023 14:46:28
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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storage
-- ----------------------------
INSERT INTO `storage` VALUES (11, '本地存储', 'local', 0, 1, 0, '测试服务器本地存储用', NULL, NULL, '2023-01-28 02:24:12', '2023-01-15 18:39:49', 1);
INSERT INTO `storage` VALUES (12, 'OneDrive E5', 'od', 1, 1, 1, '测试 OneDrive 用', NULL, NULL, '2023-01-28 02:24:16', '2023-01-20 17:40:51', 1);
INSERT INTO `storage` VALUES (13, 'OneDrive E3', 'od3', 1, 1, 0, '测试 OneDrive 用', NULL, NULL, '2023-01-28 02:24:19', '2023-01-20 17:40:51', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storage_config
-- ----------------------------
INSERT INTO `storage_config` VALUES (14, 11, '挂载路径', 'mount_path', '/root', '本地存储挂载路径');
INSERT INTO `storage_config` VALUES (15, 12, '客户端ID', 'client_id', '', 'OneDrive 客户端ID');
INSERT INTO `storage_config` VALUES (16, 12, '客户端机密', 'client_secret', '', 'OneDrive 客户端机密');
INSERT INTO `storage_config` VALUES (17, 12, '刷新令牌', 'refresh_token', '', 'OneDrive 刷新令牌');
INSERT INTO `storage_config` VALUES (18, 12, '重定向 URI', 'redirect_uri', 'http://localhost', 'OneDrive 重定向 URI');
INSERT INTO `storage_config` VALUES (19, 12, '挂载路径', 'mount_path', '/genshin', 'OneDrive 挂载路径');

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
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '网站标题', 1, 'title', 'Xanadu', '动态加载的网站标题', 1, 1, '2023-01-15 13:49:47', '2023-01-15 13:49:47', 1);
INSERT INTO `system_config` VALUES (2, '测试key', 1, 'test', 'value', '测试用的', 1, 1, '2023-01-07 20:45:50', '2023-01-07 20:26:54', 1);
INSERT INTO `system_config` VALUES (3, '备案号', 1, 'beian', '', '备案号', 1, 1, '2023-01-15 15:11:23', '2023-01-15 15:10:40', 1);

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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'xanadu', '94edf28c6d6da38fd35d7ad53e485307f89fbeaf120485c8d17a43f323deee71', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-super-admin', 'admin@qq.com', '旅行者', '13612345678', '只要不失去你的崇高，整个世界都会向你敞开。', 1, 1, '2023-01-16 16:39:19', '2023-01-10 20:15:10', '2023-01-24 23:22:39', 1, 1);
INSERT INTO `user` VALUES (2, 'test1', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-admin', 'test1@qq.com', '测试', '13612345678', '我超，原！只要不失去你的崇高，整个世界都会向你敞开。只要不失去你的崇高，整个世界都会向你敞开。只要不失去你的崇高，整个世界都会向你敞开。', 1, 1, '2023-01-10 19:24:01', '2023-01-13 19:25:54', '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (3, 'test2', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-self-provisioner', 'test2@qq.com', '测试', '13612345678', '我超，原!', 1, 1, '2023-01-10 19:24:24', '2023-01-15 20:36:22', '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (4, 'test3', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-view', 'test3@qq.com', '测试', '13612345678', '我超，原...原来你也玩原神', 1, 1, '2023-01-10 19:24:36', NULL, '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (5, 'test4', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-visitor', 'test4@qq.com', '测试', '13612345678', '我超，终于可以玩原神了', 1, 1, '2023-01-13 19:04:35', '2023-01-13 19:25:08', '2022-12-19 17:14:39', 1, 1);
INSERT INTO `user` VALUES (6, 'test5', 'eqw', 'https://besscroft.com/uploads/avatar.jpeg', 'platform-visitor', 'test5@qq.com', '测试', '13612345678', '当然是原神了', 1, 1, '2023-01-10 19:25:12', NULL, '2022-12-19 17:14:39', 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
