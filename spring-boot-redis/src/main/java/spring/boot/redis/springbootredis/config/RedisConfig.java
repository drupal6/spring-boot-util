package spring.boot.redis.springbootredis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
	
	StringRedisTemplate template;
	
	@Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean("redisLockTemplate")
    @ConditionalOnProperty(name="spring.redis.lock.host")
    public StringRedisTemplate redisTemplateTwo(@Value("${spring.redis.lock.host}") String hostName,
            @Value("${spring.redis.lock.port}") int port,
            @Value("${spring.redis.lock.password}") String password) {
    	template = new StringRedisTemplate();
        template.setConnectionFactory(
                connectionFactory(hostName, port, password));
        template.afterPropertiesSet();
        return template;
    }

	private JedisConnectionFactory connectionFactory(String hostName, int port, String password) {
		RedisStandaloneConfiguration redis = new RedisStandaloneConfiguration ();
		redis.setHostName(hostName);
		redis.setPort(port);
		redis.setPassword(RedisPassword.of(password));
        // 初始化连接pool
		JedisConnectionFactory  factory = new JedisConnectionFactory(redis);
        return factory;
	}

	public StringRedisTemplate getTemplate() {
		return template;
	}
}
