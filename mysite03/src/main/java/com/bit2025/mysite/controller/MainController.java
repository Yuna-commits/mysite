package com.bit2025.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit2025.mysite.service.SiteService;
import com.bit2025.mysite.vo.SiteVo;

@Controller
public class MainController {

	private SiteService siteService;

	// Autowired 대신 생성자로 의존성 주입
	public MainController(SiteService siteService) {
		this.siteService = siteService;
	}

	@RequestMapping({ "/", "/main" })
	public String index(Model model) {
		SiteVo siteVo = siteService.getSite();
		model.addAttribute("siteVo", siteVo);

		return "main/index";
	}

}
