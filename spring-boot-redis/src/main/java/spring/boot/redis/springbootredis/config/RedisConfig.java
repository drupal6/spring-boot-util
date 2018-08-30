package spring.boot.redis.springbootredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // set key serializer
//        template.setKeySerializer(STRING_SERIALIZER);
//        template.setHashKeySerializer(STRING_SERIALIZER);
        // fastjson serializer
//        template.setValueSerializer(FASTJSON_SERIALIZER);
//        template.setHashValueSerializer(FASTJSON_SERIALIZER);
        // 如果 KeySerializer 或者 ValueSerializer 没有配置，则对应的
        // KeySerializer、ValueSerializer 才使用这个 Serializer
//        template.setDefaultSerializer(FASTJSON_SERIALIZER);
        LettuceConnectionFactory factory = (LettuceConnectionFactory) redisConnectionFactory;
        // factory
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }
}
