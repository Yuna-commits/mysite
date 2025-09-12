package com.bit2025.mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2025.mysite.vo.SiteVo;

@Repository
public class SiteRepository {

	@Autowired
	private SqlSession sqlSession;

	public SiteRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int update(SiteVo vo) {
		return sqlSession.update("site.update", vo);
	}

	public SiteVo findById(Long id) {
		return sqlSession.selectOne("site.findById", id);
	}
}
