package com.bit2025.mysite.config.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * dispatcher-servlet.xml 기반 Bean 등록
 * 1. Locale Resolver
 * 2. Message Source
 */
@Configuration
public class LocaleConfig {
	
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver("lang");
		localeResolver.setCookieHttpOnly(false);
		
		return localeResolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("com/bit2025/mysite/config/web/messages/message");
		messageSource.setDefaultEncoding("UTF-8");
		
		return messageSource;
	}
}
