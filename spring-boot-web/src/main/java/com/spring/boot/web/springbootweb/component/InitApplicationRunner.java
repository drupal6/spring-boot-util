package com.spring.boot.web.springbootweb.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;


/**
 * 程序启动后执行该类
 *  @Order注解的执行优先级是按value值从小到大顺序。
 * @author Administrator
 *
 */
public class InitApplicationRunner implements ApplicationRunner {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(InitApplicationRunner.class);

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOGGER.info("初始化开始!");
		LOGGER.info("初始化完成!");
	}
}
