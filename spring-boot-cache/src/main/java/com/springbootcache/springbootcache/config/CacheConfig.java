package com.springbootcache.springbootcache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class CacheConfig {

    @Value("${springext.cache.redis.topic:cache}")
    private String topicName;

    @Bean
    public TowLevelCacheMananger cacheMananger(RedisTemplate redisTemplate) {
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisSerializationContext.SerializationPair pair = RedisSerializationContext.SerializationPair
                .fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader()));
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(pair);
        TowLevelCacheMananger cacheMananger = new TowLevelCacheMananger(redisTemplate, writer, config);
        return cacheMananger;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(topicName));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(final TowLevelCacheMananger cacheManager) {
        return new MessageListenerAdapter(new MessageListener() {

            public void onMessage(Message message, byte[] pattern) {
                byte[] bs = message.getChannel();
                try {
                    // Sub 一个消息，通知缓存管理器
                    String type = new String(bs, "UTF-8");
                    cacheManager.receive(type);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    // 不可能出错，忽略
                }
            }
        });
    }

    class TowLevelCacheMananger extends RedisCacheManager {

        RedisTemplate redisTemplate;

        public TowLevelCacheMananger(RedisTemplate redisTemplate, RedisCacheWriter redisCacheWriter, RedisCacheConfiguration redisCacheConfiguration) {
            super(redisCacheWriter, redisCacheConfiguration);
            this.redisTemplate = redisTemplate;
        }

        protected Cache decorateCahce(Cache cache) {
            return new RedisAndLocalCache(this, (RedisCache) cache);
        }

        public void publishMessage(String cacheName) {
            this.redisTemplate.convertAndSend(topicName, cacheName);
        }

        public void receive(String name) {
            RedisAndLocalCache cache = (RedisAndLocalCache)this.getCache(name);
            if(cache != null) {
                cache.clearLocal();
            }
        }

    }

    class RedisAndLocalCache implements Cache {
        ConcurrentHashMap<Object, Object> local = new ConcurrentHashMap<>();
        RedisCache redisCache;
        TowLevelCacheMananger cacheManager;

        public RedisAndLocalCache(TowLevelCacheMananger cacheManager, RedisCache redisCache) {
            this.redisCache = redisCache;
            this.cacheManager = cacheManager;
        }

        @Override
        public String getName() {
            return redisCache.getName();
        }

        @Override
        public Object getNativeCache() {
            return redisCache.getNativeCache();
        }

        @Override
        public ValueWrapper get(Object key) {
            ValueWrapper wrapper = (ValueWrapper)local.get(key);
            if(wrapper != null) {
                return wrapper;
            } else {
                wrapper = redisCache.get(key);
                if(wrapper != null) {
                    local.put(key, wrapper);
                }
            }
            return wrapper;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            return redisCache.get(key, type);
        }

        @Override
        public <T> T get(Object key, Callable<T> callable) {
            return redisCache.get(key, callable);
        }

        @Override
        public void put(Object o, Object o1) {
            redisCache.put(o, o1);
            local.put(o, o1);
            clearOtherJVM();
        }

        @Override
        public ValueWrapper putIfAbsent(Object o, Object o1) {
            ValueWrapper v = redisCache.putIfAbsent(o, o1);
            clearOtherJVM();
            return v;
        }

        @Override
        public void evict(Object o) {
            redisCache.evict(o);
            clearOtherJVM();
        }

        @Override
        public void clear() {
            redisCache.clear();
        }

        public void clearLocal() {
            local.clear();
        }

        protected void clearOtherJVM() {
            cacheManager.publishMessage(redisCache.getName());
        }
    }
}
