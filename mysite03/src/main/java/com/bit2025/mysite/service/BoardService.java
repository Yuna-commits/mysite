package com.bit2025.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.repository.BoardRepository;
import com.bit2025.mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	public Page getPage(int reqPage) {
		// 총 게시글 수
		int totalBoard = boardRepository.count();

		// 1. 페이지 계산
		return new Page(reqPage, totalBoard);
	}

	public List<BoardVo> getBoardList(Page page) {
		// 2. 계산 결과로 얻은 offset으로 select 쿼리 수행 -> list
		return boardRepository.findAll(page.getOffset());
	}

	public BoardVo getBoardView(Long id) {
		// 게시글 조회수 증가
		boardRepository.updateHitCount(id);

		return boardRepository.findById(id);
	}

	public void writing(BoardVo boardVo) {
		boardRepository.insert(boardVo);
	}
}
