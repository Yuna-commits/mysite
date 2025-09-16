package com.bit2025.mysite.config.app;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

/**
 * applicationContext.xml 기반 Bean 등록
 * 1. DataSource (Connection Pool)
 * 2. Transaction Manager
 * ===
 * jdbc.properties의 값을 Environment 객체에 저장 후 사용
 */
@Configuration
@PropertySource("classpath:com/bit2025/mysite/config/app/jdbc.properties")
public class DBConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}

	@Bean
	public TransactionManager transactionManager(DataSource dataSource) {
		// 생성자 주입
		return new DataSourceTransactionManager(dataSource);
	}
}
