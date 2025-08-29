package com.bit2025.mysite.controller;

import java.io.IOException;
import java.util.List;

import com.bit2025.mysite.dao.BoardDao;
import com.bit2025.mysite.vo.BoardVo;
import com.bit2025.mysite.vo.NodeVo;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("a");

		if ("view".equals(action)) {// 게시글 보기
			Long id = Long.parseLong(request.getParameter("id"));

			BoardDao dao = new BoardDao();
			dao.updateHitCount(id);
			BoardVo cView = dao.findById(id);

			request.setAttribute("cView", cView);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
			rd.forward(request, response);
		} else if ("writeform".equals(action)) {// 새 글 작성하기
			/**
			 * Access Control writeform(fail) -> login -> writeform
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

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/writeform.jsp");
			rd.forward(request, response);
		} else if ("write".equals(action) || "reply".equals(action)) {
			/**
			 * Access Control writeform(fail) -> login -> writeform
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

			Long userId = authUser.getId();
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			BoardVo vo = new BoardVo();
			vo.setUserId(userId);
			vo.setTitle(title);
			vo.setContent(content);

			BoardDao dao = new BoardDao();
			
			if ("write".equals(action)) {
				dao.insert(vo);
			} else {
				NodeVo nVo = new NodeVo();
				nVo.setgNo(Integer.parseInt(request.getParameter("gNo")));
				nVo.setoNo(Integer.parseInt(request.getParameter("oNo")));
				nVo.setDepth(Integer.parseInt(request.getParameter("depth")));
				
				// 1. Update
				dao.updateHierarchy(nVo);
				// 2. nVo
				dao.insertReply(vo, nVo);
			}
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board");
		} else if ("delete".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));

			new BoardDao().deleteById(id);
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board");
		} else if ("modifyform".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));

			BoardVo cModify = new BoardDao().findById(id);

			request.setAttribute("cModify", cModify);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/modifyform.jsp");
			rd.forward(request, response);
		} else if ("modify".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));

			String title = request.getParameter("title");
			String content = request.getParameter("content");

			BoardVo vo = new BoardVo();
			vo.setId(id);
			vo.setTitle(title);
			vo.setContent(content);

			new BoardDao().update(vo);
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board?a=view&id=" + id);
		} else {// Default action : 게시글 리스트 출력
			List<BoardVo> list = new BoardDao().findAll();
			request.setAttribute("list", list);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
			rd.forward(request, response);
		}
	}

	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String targetUrl = request.getContextPath() + "/board?a=writeform";
		request.getSession(true).setAttribute("redirectUri", targetUrl);

		response.sendRedirect(request.getContextPath() + "/user?a=loginform");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
