package com.springbootcache.springbootcache.repository;

import com.springbootcache.springbootcache.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByAccount(String account);
}
