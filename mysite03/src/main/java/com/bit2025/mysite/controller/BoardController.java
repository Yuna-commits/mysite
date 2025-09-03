package com.bit2025.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.service.BoardService;
import com.bit2025.mysite.vo.BoardVo;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping({ "", "/{p}" })
	public String list(@PathVariable(value = "p", required = false) Integer p, Model model) {
		// reqPage : default 1
		if (p == null) {
			p = 1;
		}
		// BoardService에서 paging
		Page page = boardService.getPage(p);
		List<BoardVo> list = boardService.getBoardList(page);

		model.addAttribute("pageCount", Page.PAGE_COUNT);
		model.addAttribute("page", page);
		model.addAttribute("list", list);

		return "/board/list";
	}

	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") Long id, Model model) {
		BoardVo cView = boardService.getBoardView(id);

		model.addAttribute("cView", cView);

		return "/board/view";
	}

	// writeform
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(HttpSession session) {
		/**
		 * Access Control
		 * write(fail) -> login(GET) -> write
		 */
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			session.setAttribute("redirectUri", "redirect:/board/write");
			return "redirect:/user/login";
		}
		
		return "board/write";
	}
	
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String write(BoardVo boardVo, HttpSession session) {
		/**
		 * Access Control
		 * write(fail) -> login(GET) -> write
		 */
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			session.setAttribute("redirectUri", "redirect:/board/write");
			return "redirect:/user/login";
		}
		
		boardVo.setUserId(authUser.getId());
		boardService.writing(boardVo);
		
		return "redirect:/board";
	}
}
