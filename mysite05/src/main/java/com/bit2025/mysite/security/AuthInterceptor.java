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
		
		// 3. handlerMethod가 호출하는 메서드에 걸린 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. handlerMethod에 @Auth가 없는 경우, Controller Class에서 가져오기
		if (auth == null) {
			// handlerMethod가 호출하는 실제 메서드의 컨트롤러 클래스에 걸린 @Auth
			auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		}
		
		// 5. handlerMethod와 Class(Type) 둘 다 @Auth가 없는 경우, 인증/권한 검사 필요 x
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
		/**
		 * role : 핸들러가 요구하는 권한
		 * authUser.getRole() : 로그인한 사용자의 권한
		 * ---
		 * authUser의 role과 권한 비교
		 * authUser(role = "ADMIN") : 모든 핸들러에 접근 가능
		 * authUser(role = "USER") : @Auth가 없거나 @Auth(role="USER")인 핸들러에만 접근 가능
		 */
		String role = auth.role();
		
		// 7-1. @Auth의 role이 "USER"인 경우, authUser의 role에 관계없음
		if ("USER".equals(role)) {
			return true;
		}

		// 7-2. @Auth의 role이 "ADMIN"인 경우, authUser의 role도 "ADMIN"이어야 함
		if (!"ADMIN".equals(authUser.getRole())) {
			response.sendRedirect(request.getContextPath());
			return false;
		}

		// 7-3. 옳은 관리자 권한 [@Auth(role = "ADMIN") && authUser.role == "ADMIN"]
		return true;
	}

}
