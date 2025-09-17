package com.bit2025.mysite.exception;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bit2025.mysite.dto.JsonResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public void handler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		// 1. Logging(stackTrace 내용)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		logger.error(errors.toString());

		// 2. 요청 구분
		String accept = request.getHeader("accept");

		// 2-1. JSON 요청: request header accept: application/json
		if (accept.matches(".*application/json.*")) {
			// JSON 응답 만들기 (MessageConvertor의 동작)
			String jsonString = new ObjectMapper().writeValueAsString(JsonResult.fail(errors.toString()));

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json; charset=utf-8");
			
			OutputStream os = response.getOutputStream();
			os.write(jsonString.getBytes("utf-8"));
			os.close();
			
			return;
		}

		// 2-2. HTML 요청: request header accept: text/html
		request.setAttribute("errors", errors);
		request
			.getRequestDispatcher("/WEB-INF/views/errors/exception.jsp")
			.forward(request, response);
	}
}
