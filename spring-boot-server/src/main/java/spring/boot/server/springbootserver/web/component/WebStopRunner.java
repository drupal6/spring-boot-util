package spring.boot.server.springbootserver.web.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;

/**
 * 停服服务
 * @author Administrator
 *
 */
public class WebStopRunner implements DisposableBean, ExitCodeGenerator {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebStopRunner.class);
	
	@Override
	public int getExitCode() {
		return 0;
	}

	@Override
	public void destroy() throws Exception {
		LOGGER.info("===========服务停止=============");
	}

}
