package spring.boot.server.springbootserver.web.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 开服服务
 * @author Administrator
 *
 */
public class WebStartRunner implements ApplicationRunner{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(WebStartRunner.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOGGER.info("===========服务启动初始化=============");
	}

}
