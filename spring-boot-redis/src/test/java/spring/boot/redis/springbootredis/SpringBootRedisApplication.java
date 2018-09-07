package spring.boot.redis.springbootredis;

import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import spring.boot.redis.springbootredis.entity.Test;
import spring.boot.redis.springbootredis.service.TestSetService;

@SpringBootApplication
public class SpringBootRedisApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootRedisApplication.class, args);
        
        
        TestSetService testService = (TestSetService) context.getBean("testSetService");

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
        
        Test t = new Test();
		t.setId(1);
		t.setName("111");
		t.setPassword("111");
		testService.handlerTest1(t);
    }
}
