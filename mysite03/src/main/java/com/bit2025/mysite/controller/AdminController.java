package com.bit2025.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit2025.mysite.security.Auth;
import com.bit2025.mysite.service.FileuploadService;
import com.bit2025.mysite.service.SiteService;

@Controller
@RequestMapping("/admin")
@Auth(role="ADMIN")
public class AdminController {
	
	@Autowired
	private FileuploadService fileuploadService;
	
	@Autowired
	private SiteService siteService;

	@RequestMapping({ "", "/" })
	public String main() {
		return "admin/main";
	}
	
	public String mainUpdate() {
		return null;
	}

	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}

}
