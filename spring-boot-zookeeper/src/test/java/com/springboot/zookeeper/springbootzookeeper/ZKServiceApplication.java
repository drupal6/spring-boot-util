package com.springboot.zookeeper.springbootzookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZKServiceApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ZKServiceApplication.class, args);
        Thread.sleep(10000);
    }
}
