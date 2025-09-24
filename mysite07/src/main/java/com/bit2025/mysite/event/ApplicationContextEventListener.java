package com.bit2025.mysite.event;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.bit2025.mysite.service.SiteService;
import com.bit2025.mysite.vo.SiteVo;

public class ApplicationContextEventListener {

	// Spring Container의 구현체 ApplicationContext
	private ApplicationContext applicationContext;

	public ApplicationContextEventListener(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * SiteVo 동적으로 빈 등록하기
	 */
	// Context Refreshed Event가 발생하면 메서드 실행
	@EventListener({ ContextRefreshedEvent.class })
	public void handlerContextRefreshedEvent() {
		System.out.println("-- Context Refreshed Event Received--");

		SiteService siteService = applicationContext.getBean(SiteService.class);
		SiteVo vo = siteService.getSite();

		// 1. MutablePropertyValues에 스프링 빈을 등록할 때 사용할 프로퍼티 값 저장
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("id", vo.getId());
		propertyValues.add("title", vo.getTitle());
		propertyValues.add("welcomeMessage", vo.getWelcomeMessage());
		propertyValues.add("description", vo.getDescription());
		propertyValues.add("profileURL", vo.getProfileURL());

		// 2. GenericBeanDefinition로 런타임에 동적으로 빈 정의
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		// 등록할 빈 타입 : SiteVo
		beanDefinition.setBeanClass(SiteVo.class);
		// 세팅한 프로퍼티 값을 빈의 프로퍼티로 주입
		beanDefinition.setPropertyValues(propertyValues);

		// 3. "site"라는 이름의 SiteVo 빈을 스프링 컨테이너에 등록
		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
		registry.registerBeanDefinition("site", beanDefinition);
	}

}
