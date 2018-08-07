package com.springbootcache.springbootcache.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.HashMap;
import java.util.Map;

public class ReedisCacheManangerCustomizer {

    @Bean
    public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<RedisCacheManager>() {
            @Override
            public void customize(RedisCacheManager cacheManager) {
                Map<String, Long> expires = new HashMap<>();
                expires.put("user", 300l);
            }
        };
    }
}
