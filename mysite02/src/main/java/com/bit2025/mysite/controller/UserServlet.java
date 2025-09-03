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

			// 로그인 실패 -> authUser == null
			// 다시 loginform으로, 입력한 email 브라우저에 저장
			UserVo authUser = new UserDao().findByEmailAndPassword(email, password);
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

			// redirectUri != null -> 다른 페이지로부터 리다이렉트 되어 온 경우
			String redirectUri = (String) session.getAttribute("redirectUri");
			if (redirectUri != null) {
				session.removeAttribute("redirectUri");
				response.sendRedirect(redirectUri);
			} else {
				// default main
				response.sendRedirect(request.getContextPath());
			}
		} else if ("logout".equals(action)) {
			HttpSession session = request.getSession(true);

			if (session != null) {
				// 로그아웃 처리
				session.removeAttribute("authUser");
				// JSESSIONID 변경
				session.invalidate();
			}

			// redirect to localhost/mysite02
			response.sendRedirect(request.getContextPath());
		} else if ("updateform".equals(action)) {
			/**
			 * Access Control 
			 * updateform(fail) -> login -> updateform
			 */
			HttpSession session = request.getSession(false);
			if (session == null) {// 로그인 세션이 없는 사용자
				redirectToLogin(request, response);
				return;
			}

			UserVo authUser = (UserVo) session.getAttribute("authUser");
			if (authUser == null) {// 로그인 안 한 사용자
				redirectToLogin(request, response);
				return;
			}
			
			/**
			 * updateform에 회원정보 표시
			 */
			Long id = authUser.getId();
			UserVo userVo = new UserDao().findById(id);

			session.setAttribute("userVo", userVo);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/updateform.jsp");
			rd.forward(request, response);
		} else if ("update".equals(action)) {
			/**
			 * Access Control 
			 * updateform(fail) -> login -> updateform
			 */
			HttpSession session = request.getSession(false);
			if (session == null) {// 로그인 세션이 없는 사용자
				redirectToLogin(request, response);
				return;
			}

			UserVo authUser = (UserVo) session.getAttribute("authUser");
			if (authUser == null) {// 로그인 안 한 사용자
				redirectToLogin(request, response);
				return;
			}
			
			/**
			 * 회원정보수정
			 */
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");

			UserVo vo = new UserVo();
			vo.setId(authUser.getId());
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);

			new UserDao().update(vo);

			// authUser를 수정한 내용으로 변경
			authUser.setName(vo.getName());

			// Redirect to localhost/mysite02/user?a=updateform
			response.sendRedirect(request.getContextPath() + "/user?a=updateform");
		} else {// Default main
			response.sendRedirect(request.getContextPath());
		}
	}

	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String targetUrl = request.getContextPath() + "/user?a=updateform";
		request.getSession(true).setAttribute("redirectUri", targetUrl);

		response.sendRedirect(request.getContextPath() + "/user?a=loginform");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
