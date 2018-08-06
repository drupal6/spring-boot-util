package com.springbootcache.springbootcache.service;

import com.springbootcache.springbootcache.entity.User;
import com.springbootcache.springbootcache.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(String account, String password) {
        User user = getUser(account);
        if(user != null) {
            return null;
        }
        user = new User();
        user.setAccount(account);
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }

    public User getUser(String account) {
        return userRepository.findByAccount(account);
    }
}
