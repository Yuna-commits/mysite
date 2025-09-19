package com.bit2025.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2025.mysite.security.AuthUser;
import com.bit2025.mysite.service.BoardService;
import com.bit2025.mysite.vo.BoardVo;
import com.bit2025.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String index(
			Model model, 
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer reqPage,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String keyword) {
		// BoardService에서 페이지, 키워드 검색 수행
		Map<String, Object> map = boardService.getContentsList(reqPage, keyword);

		model.addAttribute("map", map);
		model.addAttribute("keyword", keyword);

		return "/board/index";
	}

	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") Long id, Model model) {
		BoardVo boardVo = boardService.getContents(id);
		model.addAttribute("boardVo", boardVo);

		return "/board/view";
	}

	// writeform
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write() {
		// @Auth로 Access Control
		return "board/write";
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(
			@AuthUser UserVo authUser,
			@ModelAttribute BoardVo boardVo,
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer reqPage,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String keyword) {

		boardVo.setUserId(authUser.getId());
		boardService.addContents(boardVo);
		
		return "redirect:/board";
	}
	
	@RequestMapping(value = "/reply/{id}", method = RequestMethod.GET)
	public String reply(@PathVariable("id") Long id, Model model) {
		BoardVo boardVo = boardService.getContents(id);
		model.addAttribute("boardVo", boardVo);
		
		return "/board/reply";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(
			@AuthUser UserVo authUser, 
			@PathVariable("id") Long id, 
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer reqPage, 
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword) {
		boardService.deleteContents(id, authUser.getId());
		
		return "redirect:/board";
	}
	
	// modifyform
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	public String modify(@PathVariable("id") Long id, @AuthUser UserVo authUser, Model model) {
		BoardVo boardVo = boardService.getContents(id, authUser.getId());
		model.addAttribute("boardVo", boardVo);
		
		return "/board/modify";
	}

	@RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
	public String modify(
			BoardVo boardVo,
			@AuthUser UserVo authUser,
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer reqPage, 
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword) {
		boardVo.setUserId(authUser.getId());
		boardService.modifyContents(boardVo);

		return "redirect:/board/view/" + boardVo.getId();
	}
}
