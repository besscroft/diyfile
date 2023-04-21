package com.besscroft.diyfile.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2023/4/21 15:36
 */
public class DiyCache {

    private static final Cache<String, Object> DIY_CACHE = CacheUtil.newFIFOCache(100);

    /**
     * 新增缓存，默认 15 分钟过期
     * @param key 键
     * @param object 值
     */
    public static void putDiyKey(String key, Object object) {
        // 默认 15 分钟
        putDiyKeyTimeOut(key, object, 60 * 15);
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Object getDiyKey(String key) {
        return DIY_CACHE.get(key);
    }

    /**
     * 新增缓存
     * @param key 键
     * @param object 值
     * @param timeout 过期时间，单位秒
     */
    public static void putDiyKeyTimeOut(String key, Object object, long timeout) {
        DIY_CACHE.put(key, object, DateUnit.SECOND.getMillis() * timeout);
    }

    /**
     * 删除缓存
     * @param key 键
     */
    public static void removeDiyKey(String key) {
        DIY_CACHE.remove(key);
    }

}
