package com.bit2025.mysite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bit2025.mysite.config.app.DBConfig;
import com.bit2025.mysite.config.app.MyBatisConfig;

/**
 * Config Hub
 * 1. aop : auto proxy
 * 2. Transaction AOP Proxy
 * 3. Component Scan
 * 			 	   import
 * 4. AppConfig <---------- (DBConfig + MyBatisConfig)
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.bit2025.mysite.repository", "com.bit2025.mysite.service", "com.bit2025.mysite.aspect" })
@Import({ DBConfig.class, MyBatisConfig.class })
public class AppConfig {

}
