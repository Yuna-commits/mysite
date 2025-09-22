package com.bit2025.web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;

public class MyFilter02 extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;

	public void init(FilterConfig fConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute("test02", "bye");
		
		// Filter Chain
		chain.doFilter(request, response);

		System.out.println("MyFilter02.doFilter() called: response proceiing");
	}

	public void destroy() {

	}

}
