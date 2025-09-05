package com.bit2025.mysite.controller;

import java.io.IOException;
import java.util.List;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.dao.BoardDao;
import com.bit2025.mysite.vo.BoardVo;
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
			dao.updateHit(id);
			BoardVo boardVo = dao.findById(id);

			request.setAttribute("boardVo", boardVo);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
			rd.forward(request, response);
		} else if ("writeform".equals(action)) {// 새 글 작성하기
			/**
			 * Access Control
			 * writeform(fail) -> login -> writeform
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
			 * Access Control
			 * writeform(fail) -> login -> writeform
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
			vo.setContents(content);
			
			BoardDao dao = new BoardDao();
			
			if("reply".equals(action)) {
				Integer groupNo = Integer.parseInt(request.getParameter("groupNo"));
				Integer orderNo = Integer.parseInt(request.getParameter("orderNo"));
				Integer depth = Integer.parseInt(request.getParameter("depth"));
				
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				
				dao.updateOrderNo(vo);
			}

			dao.insert(vo);
			
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board");
		} else if("replyform".equals(action)) {
			/**
			 * Access Control
			 * writeform(fail) -> login -> writeform
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
			
			Long id = Long.parseLong(request.getParameter("id"));
			
			BoardVo boardVo = new BoardDao().findById(id);
			request.setAttribute("boardVo", boardVo);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/replyform.jsp");
			rd.forward(request, response);
		} else if ("delete".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));

			new BoardDao().deleteById(id);
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board");
		} else if ("modifyform".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));
			BoardVo boardVo = new BoardDao().findById(id);

			request.setAttribute("boardVo", boardVo);

			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/modifyform.jsp");
			rd.forward(request, response);
		} else if ("modify".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));
			String title = request.getParameter("title");
			String contents = request.getParameter("contents");

			BoardVo vo = new BoardVo();
			vo.setId(id);
			vo.setTitle(title);
			vo.setContents(contents);

			new BoardDao().update(vo);
			// redirect to mysite02/board
			response.sendRedirect(request.getContextPath() + "/board?a=view&id=" + id);
		} else if ("search".equals(action)) {
			/**
			 * 제목으로 게시글 검색
			 */
		} else {// Default action : 게시글 리스트 출력 + paging algorithm
				// reqPage : default 1
			int reqPage = 1;// Default
			String sPage = request.getParameter("p");
			if (sPage != null) {
				reqPage = Integer.parseInt(sPage);
			}

			BoardDao dao = new BoardDao();// 전체 게시글 수
			int totalBoard = dao.count();

			// 1. 페이지 계산
			Page page = new Page(reqPage, totalBoard);

			// 2. 계산 결과로 얻은 offset으로 select 쿼리 수행 -> list
			List<BoardVo> list = dao.findAll(page.getOffset());

			// 3. 쿼리 결과(데이터 테이블)와 페이징 결과 view에 매핑
			request.setAttribute("pageCount", Page.PAGE_COUNT);
			request.setAttribute("page", page);
			request.setAttribute("list", list);

			// 4. 뷰 포워딩
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
