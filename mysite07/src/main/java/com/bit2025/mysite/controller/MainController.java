package com.bit2025.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.ServletContext;

@Controller
public class MainController {

	private ServletContext servletContext;

	public MainController(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@RequestMapping({ "/", "/main" })
	public String index(Model model) {
		model.addAttribute("servletContext", servletContext);
		return "th/main/index";
	}

}
