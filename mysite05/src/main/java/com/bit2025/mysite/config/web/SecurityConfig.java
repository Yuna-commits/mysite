package com.bit2025.mysite.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bit2025.mysite.security.AuthInterceptor;
import com.bit2025.mysite.security.AuthUserHandlerMehtodArgumentResolver;
import com.bit2025.mysite.security.LoginInterceptor;
import com.bit2025.mysite.security.LogoutInterceptor;

/**
 * dispatcher-servlet.xml 기반 Bean 등록
 * 1. Argument Resolvers
 * 2. Security Interceptors
 */
@Configuration
@EnableWebMvc
public class SecurityConfig implements WebMvcConfigurer {

	@Bean
	public HandlerMethodArgumentResolver handlerMehtodArgumentResolver() {
		return new AuthUserHandlerMehtodArgumentResolver();
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(handlerMehtodArgumentResolver());
	}

	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}
	
	@Bean
	public LogoutInterceptor logoutInterceptor() {
		return new LogoutInterceptor();
	}
	
	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(loginInterceptor())
			.addPathPatterns("/user/auth");
			
		registry
			.addInterceptor(logoutInterceptor())
			.addPathPatterns("/user/logout");

		registry
			.addInterceptor(authInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/assets/**", "/user/auth", "/user/logout");
	}

}
