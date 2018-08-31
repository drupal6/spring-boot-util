package spring.boot.redis.springbootredis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.redis.springbootredis.annotation.RedisCache;
import spring.boot.redis.springbootredis.entity.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("testService")
public class TestService {
    @Autowired
    RedisService redisService;

    @RedisCache(name = "test", key = "{#key}", expire = 10)
    public int getInt(String key) {
        return 1;
    }

    @RedisCache(name = "test", key = "{#key}", expire = 10)
    public String getString(String key) {
        return "hello";
    }

    @RedisCache(name = "test", key = "{#id}", expire = 10)
    public Test getTest(int id) {
        Test test = new Test();
        test.setId(id);
        test.setName("test");
        test.setPassword("hello");
        return test;
    }

    @RedisCache(name = "testlist", expire = 20)
    public List<Test> getList() {
        List<Test> list = new ArrayList<>();
        Test test = new Test();
        test.setId(2);
        test.setName("list");
        test.setPassword("list");
        list.add(test);
        return list;
    }


    @RedisCache(name = "testMap", expire = 60)
    public Map<Integer, Test> getMap() {
        Map<Integer, Test> map = new HashMap<>();
        Test test = new Test();
        test.setId(3);
        test.setName("map");
        test.setPassword("list");
        map.put(test.getId(), test);
        return map;
    }

    @RedisCache(name = "testMap1", expire = 60)
    public Map<Integer, String> getMap1() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "map");
        return map;
    }

}
