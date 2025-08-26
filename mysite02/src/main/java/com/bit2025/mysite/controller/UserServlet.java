package com.bit2025.mysite.controller;

import java.io.IOException;

import com.bit2025.mysite.dao.UserDao;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("a");

		if ("joinform".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
			rd.forward(request, response);
		} else if ("join".equals(action)) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");

			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setEmail(email);
			vo.setPassword(password);
			vo.setGender(gender);

			new UserDao().insert(vo);

			// Response
			// redirect to localhost/mysite02/user?a=joinsuccess
			response.sendRedirect(request.getContextPath() + "/user?a=joinsuccess");
		} else if ("joinsuccess".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp");
			rd.forward(request, response);
		} else if ("loginform".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
		} else if ("login".equals(action)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			UserVo authUser = new UserDao().findByEmailAndPassword(email, password);
			// 로그인 실패 -> 다시 loginform으로, 입력한 email 브라우저에 저장
			if (authUser == null) {
				request.setAttribute("email", email);
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
				rd.forward(request, response);
				return;
			}

			// 로그인 성공 (세션처리)
			// getSession(true) -> 세션이 없으면 생성
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);

			// redirect to localhost/mysite02
			response.sendRedirect(request.getContextPath());
		} else {
			response.sendRedirect(request.getContextPath());// redirect to /main
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
