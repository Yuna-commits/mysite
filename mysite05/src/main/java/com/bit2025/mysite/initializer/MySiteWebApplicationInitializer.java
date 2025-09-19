package com.bit2025.mysite.initializer;

import java.util.ResourceBundle;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.bit2025.mysite.config.AppConfig;
import com.bit2025.mysite.config.WebConfig;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration.Dynamic;

/**
 * 		== web.xml -> 자바 기반 설정 == 
 * DispatcherServlet, ContextLoaderListener 등록
 */
public class MySiteWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	/**
	 * AppConfig
	 * -> Context Parameter, Context Loader Listener(root context 생성)
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	/**
	 * WebConfig
	 * -> DispatcherServlet
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	/**
	 * servlet-mapping : url-pattern
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	/**
	 * spring security : DeletegatingFilterProxy 설정
	 */
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new DelegatingFilterProxy("springSecurityFilterChain") };
	}

	/**
	 * File Upload : multipart-config
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		// fileupload.properties
		ResourceBundle bundle = ResourceBundle.getBundle("com.bit2025.mysite.config.web.fileupload");
		long maxFileSize = Long.parseLong(bundle.getString("fileupload.maxFileSize"));
		long maxRequestSize = Long.parseLong(bundle.getString("fileupload.maxRequestSize"));
		int fileSizeThreshold = Integer.parseInt(bundle.getString("fileupload.fileSizeThreshold"));
		
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(null, maxFileSize, maxRequestSize, fileSizeThreshold);
		registration.setMultipartConfig(multipartConfigElement);
	}

}
