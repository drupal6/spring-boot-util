package spring.boot.redis.springbootredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    public Boolean expire(final String key, final long expire) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) {
                return redisConnection.expire(key.getBytes(), expire);
            }
        });
    }


    /**
     * incr 获取自增长值
     * @param key
     * @return
     */
    public Long getSequence(final String key) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) {
                return redisConnection.incr(key.getBytes());
            }
        });
    }

    /**
     * set
     * @param key
     * @param value
     */
    public Boolean set(final String key, final byte[] value) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) {
                return redisConnection.set(key.getBytes(), value);
            }
        });
    }

    /**
     * get
     * @param key
     */
    public byte[] get(final String key) {
        return redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) {
                return redisConnection.get(key.getBytes());
            }
        });
    }

    /**
     * set
     * @param value
     */
    public Boolean hset(final String key1, final String key2, final byte[] value) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) {
                return redisConnection.hSet(key1.getBytes(), key2.getBytes(), value);
            }
        });
    }

    /**
     * get
     */
    public Map<byte[], byte[]> hgetAll(final String key) {
        return redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> doInRedis(RedisConnection redisConnection) {
                return redisConnection.hGetAll(key.getBytes());
            }
        });
    }

    public Long hdel(final String key1, String key2) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) {
                return redisConnection.hDel(key1.getBytes(), key2.getBytes());
            }
        });
    }

    public byte[] hget(final String key1, final String key2) {
        return redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) {
                return redisConnection.hGet(key1.getBytes(), key2.getBytes());
            }
        });
    }

    /**
     * del 删除key
     * @param key
     */
    public Long deleteKey(final String key) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) {
                return redisConnection.del(key.getBytes());
            }
        });
    }
}
