/*
 基础结构表

 Date: 12/02/2023 14:09:05
*/

-- ----------------------------
-- Table structure for storage
-- ----------------------------
CREATE TABLE "storage" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" text(32),
    "storage_key" text(32) NOT NULL,
    "type" integer(4),
    "enable" integer(4) DEFAULT 0,
    "default_status" integer(4) DEFAULT 0,
    "remark" text(255),
    "creator" integer(20),
    "updater" integer(20),
    "create_time" TEXT,
    "update_time" TEXT,
    "del" integer(1) NOT NULL DEFAULT 1
);

-- ----------------------------
-- Table structure for storage_config
-- ----------------------------
CREATE TABLE "storage_config" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "storage_id" integer(20) NOT NULL,
    "name" text(64),
    "config_key" text(64),
    "config_value" text(1000),
    "description" text(255)
);

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
CREATE TABLE "system_config" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" text(64),
    "type" text(4),
    "config_key" text(64) NOT NULL,
    "config_value" text(255) NOT NULL,
    "description" text(255),
    "creator" integer(20),
    "updater" integer(20),
    "create_time" datetime,
    "update_time" datetime,
    "del" integer(1) NOT NULL DEFAULT 1
);

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE "user" (
    "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "username" text(32) NOT NULL,
    "password" text(200) NOT NULL,
    "avatar" text(200),
    "role" text(32),
    "email" text(64),
    "name" text(20),
    "telephone" text(11),
    "remark" text(255),
    "creator" integer(20),
    "updater" integer(20),
    "create_time" datetime,
    "update_time" datetime,
    "login_time" datetime,
    "status" integer(1) NOT NULL DEFAULT 0,
    "del" integer(1) NOT NULL DEFAULT 1
);


