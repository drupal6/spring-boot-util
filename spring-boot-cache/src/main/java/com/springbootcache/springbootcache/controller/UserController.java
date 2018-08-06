package com.springbootcache.springbootcache.controller;

import com.springbootcache.springbootcache.entity.User;
import com.springbootcache.springbootcache.repository.UserRepository;
import com.springbootcache.springbootcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/addUser")
    public User addUser(String account, String password) {
        return userService.addUser(account, password);
    }

    @RequestMapping("/getUser")
    public User getUser(String account) {
        return userService.getUser(account);
    }
}
