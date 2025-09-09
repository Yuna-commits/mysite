package com.bit2025.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit2025.mysite.security.Auth;
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
	 * 로그인 인증, 세션처리 잘못된 방법(Controller에서 처리하면 안됨) : 기술 침투 
	 * Spring@MVC에서는 해결할 방법이 없음
	 */
	/**
	 * ModelAttribute에 userVo가 존재하는데 authUser == null
	 * -> 로그인 시도했지만 실패한 경우
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
		
		String redirectUri = (String) session.getAttribute("redirectUri");
		if(userService.isValidAccess(session)) {
			return redirectUri;
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		// 세션 제거
		session.removeAttribute("authUser");
		session.invalidate();

		return "redirect:/";
	}
	
	// updateform
	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Model model, HttpSession session) {
		/**
		 * Access Control
		 * redirect는 항상 GET 요청
		 * update(fail) -> login(GET) -> update
		 * 파라미터로 session이 주입되어 널 체크 필요 x, 자동으로 새 세션이 생성됨
		 */
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			session.setAttribute("redirectUri", "redirect:/user/update");
			return "redirect:/user/login";
		}

		Long id = authUser.getId();
		UserVo userVo = userService.getUser(id);

		model.addAttribute("userVo", userVo);

		return "user/update";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(UserVo userVo, HttpSession session) {
		/**
		 * Access Control
		 */
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			session.setAttribute("redirectUri", "redirect:/user/update");
			return "redirect:/user/login";
		}

		userVo.setId(authUser.getId());
		userService.updateUser(userVo);

		// session에 등록된 authUser의 이름 변경, header에 표시
		authUser.setName(userVo.getName());

		return "redirect:/user/update";
	}
	
}
