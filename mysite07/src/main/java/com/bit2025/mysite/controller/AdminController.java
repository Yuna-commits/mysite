package com.bit2025.mysite.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bit2025.mysite.service.FileuploadService;
import com.bit2025.mysite.service.SiteService;
import com.bit2025.mysite.vo.SiteVo;

import jakarta.servlet.ServletContext;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private ServletContext servletContext;
	private ApplicationContext applicationContext;

	private FileuploadService fileuploadService;
	private SiteService siteService;

	public AdminController(ServletContext servletContext, ApplicationContext applicationContext,
			SiteService siteService, FileuploadService fileuploadService) {
		this.servletContext = servletContext;
		this.siteService = siteService;
		this.fileuploadService = fileuploadService;
		this.applicationContext = applicationContext;
	}

	@RequestMapping({ "", "/" })
	public String main(Model model) {
		model.addAttribute("siteVo", servletContext.getAttribute("siteVo"));
		return "th/admin/main";
	}

	@RequestMapping(value = "/main/update", method = RequestMethod.POST)
	public String mainUpdate(SiteVo siteVo, @RequestParam(value = "file") MultipartFile multipartFile) {
		String profileURL = fileuploadService.restore(multipartFile);
		
		// 이미지 등록을 안 하면 이전 이미지 유지
		if (profileURL == null) {
			profileURL = siteService.getSite().getProfileURL();
		}

		siteVo.setProfileURL(profileURL);

		// form 입력 정보로 mysite 갱신
		siteService.updateSite(siteVo);

		// update SiteVo bean in Servlet Context
		servletContext.setAttribute("siteVo", siteVo);

		// update SiteVo bean in Application Context
		SiteVo site = applicationContext.getBean(SiteVo.class);
		// siteVo -> site copy
		BeanUtils.copyProperties(siteVo, site);
		
		return "redirect:/admin";
	}

	@RequestMapping("/guestbook")
	public String guestbook() {
		return "th/admin/guestbook";
	}

	@RequestMapping("/board")
	public String board() {
		return "th/admin/board";
	}

	@RequestMapping("/user")
	public String user() {
		return "th/admin/user";
	}

}
