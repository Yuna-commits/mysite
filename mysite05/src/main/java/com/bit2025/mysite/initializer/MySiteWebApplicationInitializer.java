package com.bit2025.mysite.initializer;

import java.util.ResourceBundle;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.bit2025.mysite.config.AppConfig;
import com.bit2025.mysite.config.WebConfig;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration.Dynamic;

/**
 * 		== web.xml -> 자바 기반 설정 == 
 * DispatcherServlet, ContextLoaderListener 등록
 * 1. AppConfig(Context Parameter, Context Loader Listener)
 * 2. WebConfig(DispatcherServlet)
 * 3. servlet-mapping : url-pattern
 * 4. File Upload : multipart-config
 */
public class MySiteWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

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
