package spring.boot.gen.springbootgen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import spring.boot.gen.springbootgen.service.impl.SysGeneratorServiceImpl;

@SpringBootApplication(exclude={WebMvcAutoConfiguration.class})
public class GenApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext conetxt = SpringApplication.run(GenApplication.class, args);
		SysGeneratorServiceImpl service = (SysGeneratorServiceImpl) conetxt.getBean("sysGeneratorService");
		service.generatorCode();
		conetxt.close();
	}
}
