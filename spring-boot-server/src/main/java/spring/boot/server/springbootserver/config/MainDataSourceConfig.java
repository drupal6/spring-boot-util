package spring.boot.server.springbootserver.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 主数据源
 * @author Administrator
 *
 */
@Configuration
@MapperScan(basePackages="spring.boot.server.springbootserver.mapper.main", sqlSessionTemplateRef="mainSqlSessionTemplate")
public class MainDataSourceConfig {
	
	@Autowired
    private Environment env;
	
	@Bean(name="mainDataSource")
	@ConfigurationProperties(prefix="spring.datasource.main")
	@Primary
	public DataSource mainDataSource() {
		return DataSourceBuilder.create()
				.driverClassName(env.getProperty("spring.datasource.main.driver-class-name"))
				.url(env.getProperty("spring.datasource.main.url"))
				.username(env.getProperty("spring.datasource.main.username"))
				.password(env.getProperty("spring.datasource.main.password"))
				.build();
	}

	
	@Bean(name="mainSqlSessionFactory")
	@Primary
	public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/main/*Mapper.xml"));
		return bean.getObject();
	}
	
	@Bean(name="mainTransactionManager")
	@Primary
	public DataSourceTransactionManager mainSourceTransactionManager(@Qualifier("mainDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name="mainSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate mainSqlSessionTemplate(@Qualifier("mainSqlSessionFactory") SqlSessionFactory mainSqlSessionFactory) {
		return new SqlSessionTemplate(mainSqlSessionFactory);
	}
}
