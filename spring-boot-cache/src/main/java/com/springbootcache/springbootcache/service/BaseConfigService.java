package com.springbootcache.springbootcache.service;

import com.springbootcache.springbootcache.entity.BaseConfig;
import com.springbootcache.springbootcache.repository.BaseConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class BaseConfigService {

    @Autowired
    private BaseConfigRepository baseConfigRepository;

    @Cacheable(cacheNames = "baseconfig", key = "#name")
    public BaseConfig addConfig(String name, String value) {
        BaseConfig baseConfig = getBaseConfig(name);
        if(baseConfig == null) {
            return null;
        }
        baseConfig = new BaseConfig();
        baseConfig.setName(name);
        baseConfig.setValue(value);
        baseConfigRepository.save(baseConfig);
        return baseConfig;
    }

    @Cacheable(cacheNames = "baseconfig", key = "#name")
    public BaseConfig getBaseConfig(String name) {
        return baseConfigRepository.findByName(name);
    }

    @CacheEvict(cacheNames = "baseconfig", key = "#name")
    public BaseConfig deleteBaseConfig(String name) {
        BaseConfig baseConfig = getBaseConfig(name);
        if(baseConfig == null) {
            return baseConfig;
        }
        baseConfigRepository.delete(baseConfig);
        return baseConfig;
    }
}
