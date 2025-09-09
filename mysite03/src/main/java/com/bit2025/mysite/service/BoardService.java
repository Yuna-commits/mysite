package com.bit2025.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.repository.BoardRepository;
import com.bit2025.mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	public Map<String, Object> getContentsList(Integer reqPage, String keyword) {
		int totalBoard = boardRepository.count(keyword);
		Page page = new Page(reqPage, totalBoard);
		
		// 키워드에 해당하는 게시글 리스트 조회(키워드가 없으면 전체 게시글)
		List<BoardVo> list = boardRepository.findAllByKeyword(keyword, page);
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 리스트 정보를 맵에 저장
		map.put("list", list);
		map.put("page", page);

		return map;
	}

	public BoardVo getContents(Long boardId) {
		BoardVo boardVo = boardRepository.findById(boardId);
		
		if (boardVo != null) {
			// 게시글 조회수 증가
			boardRepository.updateHit(boardId);
		}

		return boardVo;
	}

	public BoardVo getContents(Long boardId, Long userId) {
		return boardRepository.findByIdAndUserId(boardId, userId);
	}
	
	@Transactional
	public void addContents(BoardVo boardVo) {
		if (boardVo.getGroupNo() != null) {
			boardRepository.updateOrderNo(boardVo);
		} 
		
		boardRepository.insert(boardVo);
	}

	public void deleteContents(Long boardId, Long userId) {
		boardRepository.delete(boardId, userId);
	}

	public void modifyContents(BoardVo boardVo) {
		boardRepository.update(boardVo);
	}
}
