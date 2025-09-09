package com.bit2025.mysite.security;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 1. Handler의 종류 확인
		 * HandlerMethod or DefaultServletHttpRequestHandler
		 */
		if (!(handler instanceof HandlerMethod)) {
			/**
			 *  DefaultServletHttpRequestHandler 타입인 경우
			 *  DefaultServletHttpRequestHandler가 처리하는 경우
			 *  ex. 정적자원, /assets/**
			 */
			return true;
		}
		
		// 2. Casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 3. @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4-1. @Auth가 없는 경우
		if(auth == null) {
			return true;
		}
		
		// 4-2. @Auth가 있는 경우, 인증(Authentication) 여부 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if(authUser == null) {
			// 5-1. 인증되지 않은 사용자인 경우, login으로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 5-2. 인증된 사용자인 경우
		return true;
	}

}
