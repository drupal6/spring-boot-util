package com.spring.boot.web.springbootweb.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;


/**
 * 停止应用停止相关服务
 * @author Administrator
 *
 */
public class StopApplicationRunner implements DisposableBean, ExitCodeGenerator {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(StopApplicationRunner.class);
	

	@Override
	public int getExitCode() {
		return 0;
	}

	@Override
	public void destroy() throws Exception {
		LOGGER.info("开始停止应用");
		LOGGER.info("完成停止应用");
	}

}
