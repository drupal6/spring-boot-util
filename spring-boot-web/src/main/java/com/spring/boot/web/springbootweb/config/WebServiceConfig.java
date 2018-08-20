package com.spring.boot.web.springbootweb.config;

import com.spring.boot.web.springbootweb.component.InitApplicationRunner;
import com.spring.boot.web.springbootweb.component.StopApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebServiceConfig {

	/**
	 * webservice启动后调用
	 * @return
	 */
	@Bean
	public InitApplicationRunner init() {
		return new InitApplicationRunner();
	}
	
	/**
	 * webservice停止调用
	 * @return
	 */
	@Bean
	public StopApplicationRunner stop() {
		return new StopApplicationRunner();
	}
	
}
