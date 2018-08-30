package spring.boot.redis.springbootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import spring.boot.redis.springbootredis.service.TestService;

@SpringBootApplication
public class SpringBootRedisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootRedisApplication.class, args);

        TestService testService = (TestService) context.getBean("testService");

        System.out.println(testService.getString("helloworld", "helloworld2"));
    }
}
