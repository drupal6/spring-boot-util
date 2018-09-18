package spring.boot.redis.springbootredis.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.redis.springbootredis.annotation.RedisLock;
import spring.boot.redis.springbootredis.annotation.RedisSet;
import spring.boot.redis.springbootredis.entity.Test;


@Service("testSetService")
public class TestSetService {
    @Autowired
    RedisService redisService;
    
    @RedisSet(name = "test", key = "{#key}", expire = 10)
    public int getInt(String key) {
    	return 1;
    }
    
    @RedisSet(name = "test", key = "{#key}", expire = 10)
    public String getString(String key) {
    	 return "hello";
    }
    
    @RedisSet(name = "test", key = "{#id}", expire = 10)
    public Test getTest(int id) {
    	Test test = new Test();
    	test.setId(id);
    	test.setName("test");
    	test.setPassword("hello");
        return test;
    }
    
    @RedisSet(name = "testlist", expire = 20)
    public List<Test> getList() {
    	List<Test> list = new ArrayList<>();
    	Test test = new Test();
    	test.setId(2);
    	test.setName("list");
    	test.setPassword("list");
    	list.add(test);
        return list;
    }

    
    @RedisSet(name = "testMap", expire = 60)
    public Map<Integer, Test> getMap() {
    	Map<Integer, Test> map = new HashMap<>();
    	Test test = new Test();
    	test.setId(3);
    	test.setName("map");
    	test.setPassword("list");
    	map.put(test.getId(), test);
        return map;
    }
    
    @RedisSet(name = "testMap1", expire = 60)
    public Map<Integer, String> getMap1() {
    	Map<Integer, String> map = new HashMap<>();
    	map.put(1, "map");
        return map;
    }
    
    @RedisLock(lockName="test", lockKey="{#test.id}", comsumerKey="{#test.id}", expire=5000)
    public boolean handlerTest(Test test) {
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return true;

    }
    @RedisLock(lockName="test", lockKey="{#test.id}", comsumerKey="{#test.id}", expire=500)
    public boolean handlerTest1(Test test) {
    	try {
    		Thread.sleep(500);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	return true;
    }
}
