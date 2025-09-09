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
			 * DefaultServletHttpRequestHandler 타입인 경우
			 * DefaultServletHttpRequestHandler가 처리하는 경우
			 * ex. 정적자원, /assets/**
			 */
			return true;
		}
		
		// 2. Casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 3. handlerMethod에서 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. handlerMethod에 @Auth가 없는 경우, Controller Class에서 가져오기
		if(auth == null) {
			auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
		}
		
		// 5. handlerMethod와 Class(Type) 둘 다 @Auth가 없는 경우(정적 자원 요청), 인증/권한 검사 필요 x
		if(auth == null) {
			return true;
		}

		// 6. @Auth가 있는 경우, 인증(Authentication) 여부 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if (authUser == null) {
			// 6-1. 인증되지 않은 사용자인 경우, login으로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 6-2. 인증된 사용자인 경우
		// 7. 권한(Authorization) 체크를 위해, @Auth의 role("USER", "ADMIN") 가져오기
		String role = auth.role();
		
		/**
		 * role : 핸들러가 요구하는 권한
		 * roleAuthUser : 로그인한 사용자의 권한
		 * authUser의 role과 권한 비교
		 */
		String roleAuthUser = authUser.getRole();

		// authUser(role = "ADMIN") : 모든 핸들러에 접근 가능
		if ("ADMIN".equals(roleAuthUser)) {
			return true;
		}

		// authUser(role = "USER") : @Auth가 없거나
		if (role == null) {
			return true;
		}

		// @Auth(role="USER")인 핸들러에만 접근 가능
		return role.equals(roleAuthUser);
	}

}
