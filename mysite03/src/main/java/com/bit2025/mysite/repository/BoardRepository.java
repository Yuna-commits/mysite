package com.bit2025.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.vo.BoardVo;

@Repository
public class BoardRepository {

	@Autowired
	private SqlSession sqlSession;

	public int count(String keyword) {
		return sqlSession.selectOne("board.count", keyword);
	}

	public int insert(BoardVo boardVo) {
		return sqlSession.insert("board.insert", boardVo);
	}

	public int update(BoardVo boardVo) {
		return sqlSession.update("board.update", boardVo);
	}

	// view로 조회한 게시글의 조회수 증가
	public int updateHit(Long id) {
		return sqlSession.update("board.updateHit", id);
	}

	public int updateOrderNo(BoardVo boardVo) {
		return sqlSession.update("board.updateOrderNo", boardVo);
	}

	public int delete(Long id, Long userId) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("id", id);
		map.put("userId", userId);
		
		return sqlSession.delete("board.delete", map);
	}

	// 게시글 id로 게시글의 내용 보기
	public BoardVo findById(Long id) {
		return sqlSession.selectOne("board.findById", id);
	}

	public BoardVo findByIdAndUserId(Long id, Long userId) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("id", id);
		map.put("userId", userId);
		
		return sqlSession.selectOne("board.findByIdAndUserId", map);
	}

	// 전체 게시글 리스트 출력
	public List<BoardVo> findAllByKeyword(String keyword, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keyword", keyword);
		map.put("boardSize", page.getBoardSize());
		map.put("offset", page.getOffset());
		
		return sqlSession.selectList("board.findAllByKeyword", map);
	}
}
