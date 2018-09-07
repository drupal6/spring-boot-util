package spring.boot.redis.springbootredis.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisLockService {
	
	@Autowired
	RedisLockService redis;
	
	@Test
	public void testLockAndUnLock() {
		Assert.assertTrue(redis.lock("testl", "1", 5000));
		Assert.assertFalse(redis.lock("testl", "1", 5000));
		Assert.assertTrue(redis.releaseLock("testl", "1"));
		Assert.assertTrue(redis.lock("testl", "1", 5000));
	}
}
