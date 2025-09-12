package com.bit2025.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bit2025.mysite.security.Auth;
import com.bit2025.mysite.service.FileuploadService;
import com.bit2025.mysite.service.SiteService;
import com.bit2025.mysite.vo.SiteVo;

@Controller
@RequestMapping("/admin")
@Auth(role = "ADMIN")
public class AdminController {

	private FileuploadService fileuploadService;

	private SiteService siteService;

	public AdminController(SiteService siteService, FileuploadService fileuploadService) {
		this.siteService = siteService;
		this.fileuploadService = fileuploadService;
	}

	@RequestMapping({ "", "/" })
	public String main(Model model) {
		return "admin/main";
	}

	@RequestMapping(value = "/main/update", method = RequestMethod.POST)
	public String mainUpdate(
			SiteVo siteVo, 
			@RequestParam(value = "file") MultipartFile multipartFile) {
		
		String url = fileuploadService.restore(multipartFile);
		if (url != null) {
			siteVo.setProfileURL(url);
		} else { 
			siteVo.setProfileURL(siteService.getSite().getProfileURL());
		}		
		// form 입력 정보로 mysite 갱신
		siteService.updateSite(siteVo);

		return "redirect:/admin";
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
