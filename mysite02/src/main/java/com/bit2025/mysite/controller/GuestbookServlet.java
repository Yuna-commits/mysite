package com.bit2025.mysite.controller;

import java.io.IOException;
import java.util.List;

import com.bit2025.mysite.dao.GuestbookDao;
import com.bit2025.mysite.vo.GuestbookVo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("a");
		
		if ("add".equals(action)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String message = request.getParameter("message");

			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setMessage(message);

			new GuestbookDao().insert(vo);

			// Response
			// redirect to /mysite02/guestbook
			response.sendRedirect(request.getContextPath()+"/guestbook");
		} else if("deleteform".equals(action)) {
			request.getRequestDispatcher("/WEB-INF/views/guestbook/deleteform.jsp").forward(request, response);
		} else if("delete".equals(action)) {
			Long id = Long.parseLong(request.getParameter("id"));
			String password = request.getParameter("password");
			
			new GuestbookDao().deleteByIdAndPassword(id, password);
			// redirect to /guestbook
			response.sendRedirect(request.getContextPath()+"/guestbook");
		} else {// Default action
			List<GuestbookVo> list = new GuestbookDao().findAll();
			
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/guestbook/list.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
