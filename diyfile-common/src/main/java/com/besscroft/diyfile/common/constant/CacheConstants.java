package com.besscroft.diyfile.common.constant;

/**
 * @Description 缓存常量
 * @Author Bess Croft
 * @Date 2023/2/22 21:09
 */
public interface CacheConstants {

    /** 默认存储信息 */
    String DEFAULT_STORAGE = "storage:defaultStorage";

    /** 已启用存储信息 */
    String ENABLE_STORAGE = "storage:enableStorage";

    /** 存储 id(根据 key 返回 id) */
    String STORAGE_ID_BY_KEY = "storageId:storageKey:";

    /** 存储信息(id) */
    String STORAGE_ID = "storage:storageId:";

    /** 存储信息(storageKey) */
    String STORAGE_KEY = "storage:storageKey:";

    /** OneDrive driveId */
    String ONEDRIVE_DRIVE_ID = "storage:onedrive:driveId:storageId:";

    /** OneDrive token */
    String ONEDRIVE_TOKEN = "storage:onedrive:token:storageId:";

    /** 用户信息 */
    String USER = "user:userId:";

    /** 系统配置 */
    String SYSTEM_CONFIG = "system:config";

    /** 网站标题 */
    String SITE_TITLE = "system:siteTitle";

    /** 网站配置 */
    String SITE_CONFIG = "system:siteConfig";

    /** 备案信息 */
    String SITE_BEIAN = "system:beian";

    /** barkId */
    String BARK_ID = "system:barkId";

}
