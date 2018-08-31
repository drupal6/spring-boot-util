package spring.boot.redis.springbootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import spring.boot.redis.springbootredis.entity.Test;
import spring.boot.redis.springbootredis.service.TestService;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringBootRedisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootRedisApplication.class, args);

        TestService testService = (TestService) context.getBean("testService");

        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println(testService.getInt("h"));

        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println(testService.getString("helloworld"));

        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println(testService.getTest(1).toString());

        System.out.println(">>>>>>>>>>>>>>>>");
        List<Test> list = testService.getList();
        for(Test t : list) {
            System.out.println(t.toString());
        }
        System.out.println(">>>>>>>>>>>>>>>>");
        Map<Integer, Test> map = testService.getMap();
        for(int id : map.keySet()) {
            System.out.println(map.get(id).toString());
        }
        System.out.println(">>>>>>>>>>>>>>>>");
        Map<Integer, String> map1 = testService.getMap1();
        for(int id : map1.keySet()) {
            System.out.println(map1.get(id));
        }
    }
}
