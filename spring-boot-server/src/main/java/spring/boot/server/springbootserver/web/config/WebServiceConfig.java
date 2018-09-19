package spring.boot.server.springbootserver.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.boot.server.springbootserver.web.component.WebStartRunner;
import spring.boot.server.springbootserver.web.component.WebStopRunner;

@Configuration
public class WebServiceConfig {

	/**
	 * webservice启动后调用
	 * @return
	 */
	@Bean
	public WebStartRunner start() {
		return new WebStartRunner();
	}
	
	/**
	 * webservice停止调用
	 * @return
	 */
	@Bean
	public WebStopRunner stop() {
		return new WebStopRunner();
	}
	
}
