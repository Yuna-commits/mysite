package com.bit2025.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit2025.mysite.service.UserService;
import com.bit2025.mysite.vo.UserVo;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// joinform
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "th/user/join";
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "th/user/join";
		}
		
		userService.join(userVo);
		
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping("/joinsuccess")
	public String joinsuccess() {
		return "th/user/joinsuccess";
	}

	@RequestMapping("/login")
	public String login() {
		return "th/user/login";
	}
	
	// updateform
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(/* HttpSession session */ Authentication authentication, Model model) {
		// 1. HttpSession을 사용하는 방법 -> 기술침투 문제
		// SecurityContext securityContext = (SecurityContext) session
		// 		.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		// Authentication authentication = securityContext.getAuthentication();
		// UserVo authUser = (UserVo) authentication.getPrincipal();

		// 2. SecurityContextHolder(Spring Security ThreadLocal Helper)
		//		-> Holder는 TheadLocal로 현재 스레드의 Security Context 관리
		// 		-> HttpSession 사용 x, 기술침투 해결
		// SecurityContext securityContext = SecurityContextHolder.getContext();
		// Authentication authentication = securityContext.getAuthentication();
		// UserVo authUser = (UserVo) authentication.getPrincipal();
		
		// 3. Spring Security ArgumentResolver 사용
		//		-> @AuthenticationPrincipal로 authentication 객체를 컨트롤러 메서드에 직접 주입
		//		-> HttpSession, SecurityContextHolder 사용 x
		UserVo authUser = (UserVo) authentication.getPrincipal();
		
		Long id = authUser.getId();
		UserVo userVo = userService.getUser(id);

		model.addAttribute("userVo", userVo);

		return "th/user/update";
	}
	
	/**
	 * authUser : 인증받은 사용자 정보
	 * userVo : form에 입력된 사용자 정보
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Authentication authentication, UserVo userVo) {
		UserVo authUser = (UserVo) authentication.getPrincipal();
		
		userVo.setId(authUser.getId());
		userService.updateUser(userVo);

		// session에 등록된 authUser의 이름 변경, header에 표시
		authUser.setName(userVo.getName());

		return "redirect:/user/update";
	}

}
