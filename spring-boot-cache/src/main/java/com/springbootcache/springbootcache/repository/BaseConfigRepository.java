package com.springbootcache.springbootcache.repository;

import com.springbootcache.springbootcache.entity.BaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseConfigRepository extends JpaRepository<BaseConfig, Integer> {

    public BaseConfig findByName(String name);
}
