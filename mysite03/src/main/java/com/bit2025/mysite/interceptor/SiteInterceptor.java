package com.bit2025.mysite.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.bit2025.mysite.service.SiteService;
import com.bit2025.mysite.vo.SiteVo;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SiteInterceptor implements HandlerInterceptor {
	
	private ServletContext servletContext;
	
	private SiteService siteService;

	public SiteInterceptor(ServletContext servletContext, SiteService siteService) {
		this.servletContext = servletContext;
		this.siteService = siteService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 모든 페이지에 Header title 표시
		 * -> Request Scope에 저장하면 페이지 이동 요청이 올 때마다 매번 getSite() 호출
		 * -> 쿼리 사용이 너무 잦음!
		 * -> application이 끝날 때까지 유지되도록 변경
		 * 
		 * 1. SiteVo를 동적으로 Bean 등록
		 * 2. Servlet의 Application Scope에 SiteVo 저장
		 */
		SiteVo siteVo = (SiteVo) servletContext.getAttribute("siteVo");

		// 최초 요청인 경우
		if (siteVo == null) {
			siteVo = siteService.getSite();
			servletContext.setAttribute("siteVo", siteVo);
		}

		return true;
	}

}
