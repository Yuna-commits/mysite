package com.bit2025.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit2025.mysite.service.UserService;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// joinform
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "user/join";
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(UserVo userVo) {
		userService.join(userVo);

		return "redirect:/user/joinsuccess";
	}

	@RequestMapping("/joinsuccess")
	public String joinsuccess() {
		return "user/joinsuccess";
	}

	// loginform
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}

	/**
	 * 로그인 인증, 세션처리 잘못된 방법(Controller에서 처리하면 안됨) 
	 * Spring@MVC에서는 해결할 방법이 없음
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute UserVo userVo, HttpSession session) {
		UserVo authUser = userService.getUser(userVo);

		// 로그인 실패
		if (authUser == null) {
			// @ModelAttribute == model.addAttribute("userVo", userVo)
			return "user/login";
		}

		// 로그인 성공
		session.setAttribute("authUser", authUser);

		return "redirect:/";
	}
}
