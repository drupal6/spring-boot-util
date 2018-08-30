package spring.boot.redis.springbootredis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.redis.springbootredis.annotation.RedisCache;

@Service("testService")
public class TestService {
    @Autowired
    RedisService redisService;

    @RedisCache(name = "test", key = "{#key}", expire = 10)
    public String getString(String key, String value) {
        return getString(value);
    }

    public String getString(String value) {
        return value;
    }

}
