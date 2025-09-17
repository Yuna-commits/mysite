package com.bit2025.mysite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bit2025.mysite.service.UserService;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		// 로그인 실패하는 경우
		UserVo authUser = userService.getUser(email, password);
		if(authUser == null) {
			// 실패한 로그인 정보가 페이지에 표시되도록
			request.setAttribute("email", email);
			request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
			
			return false;
		}
		
		// 로그인 성공하는 경우
		HttpSession session = request.getSession(true);
		session.setAttribute("authUser", authUser);
		response.sendRedirect(request.getContextPath());
		
		return false;
	}

}
