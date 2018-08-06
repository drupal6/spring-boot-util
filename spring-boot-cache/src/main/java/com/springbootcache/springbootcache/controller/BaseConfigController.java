package com.springbootcache.springbootcache.controller;

import com.springbootcache.springbootcache.entity.BaseConfig;
import com.springbootcache.springbootcache.service.BaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base")
public class BaseConfigController {

    @Autowired
    private BaseConfigService baseConfigService;

    @RequestMapping("/addBaseConfig")
    public BaseConfig addBaseConfig(String name, String account) {
        return baseConfigService.addConfig(name, account);
    }

    @RequestMapping("/getBaseConfig")
    public BaseConfig getBaseConfig(String name) {
        return baseConfigService.getBaseConfig(name);
    }

    public BaseConfig deleteBaseConfig(String name) {
        return baseConfigService.deleteBaseConfig(name);
    }
}
