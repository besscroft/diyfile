package com.besscroft.diyfile.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 缓存配置
 * @Author Bess Croft
 * @Date 2023/1/22 20:49
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public Cache<String, Object> caffeineCache(CacheProperties cacheProperties) {
        String spec = cacheProperties.getCaffeine().getSpec();
        CaffeineSpec caffeineSpec = CaffeineSpec.parse(spec);
        Caffeine<Object, Object> caffeine = Caffeine.from(caffeineSpec);
        return caffeine.build();
    }

}
