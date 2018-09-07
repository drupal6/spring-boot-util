package spring.boot.redis.springbootredis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import spring.boot.redis.springbootredis.config.RedisConfig;

@Component
@ConditionalOnBean(RedisConfig.class)
public class RedisLockService {
	
	private static final String SETCOMMAND = "SET";
	private static final byte[] NXBYTE = "NX".getBytes();
	private static final byte[] PXBYTE = "PX".getBytes();
	private static final String OK = "OK";
	
	@Autowired
	RedisConfig redisConfig;

	private StringRedisTemplate getTemplate() {
		StringRedisTemplate st = redisConfig.getTemplate();
		if(st == null) {
			throw new NullPointerException("StringRedisTemplate is null. no config lock service!");
		}
		return st;
	}
	
	public Boolean lock(String lockKey, String clientKey, int expireTime) {
		return getTemplate().execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) {
				Object result = redisConnection.execute(SETCOMMAND, lockKey.getBytes(), clientKey.getBytes(),
						NXBYTE, PXBYTE, String.valueOf(expireTime).getBytes());
				if (result != null) {
					String r = new String((byte[])result);
					if(r.equalsIgnoreCase(OK)) {
						return true;
					}
				}
				return false;
			}
		});
	}

	/**
	 * 解锁 首先获取锁对应的value值，检查是否与key相等，如果相等则删除锁（解锁）
	 * 
	 * @param lockKey
	 * @return
	 */
	public Boolean releaseLock(String lockKey, String clientKey) {
		return getTemplate().execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) {
				String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 1 end";
				Long result = redisConnection.eval(script.getBytes(), ReturnType.fromJavaType(Long.class), 1,
						lockKey.getBytes(), clientKey.getBytes());
				if (result == 1) {
					return true;
				}
				return false;
			}
		});
	}
}
