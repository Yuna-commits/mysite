package com.bit2025.mysite.vo;

import java.util.Date;

public class BoardVo {

	private Long id;
	private String title;
	private String content;
	private int hit;
	private Date regDate;

	// 계층형 테이블 속성
	private NodeVo nVo;

	// user table 속성
	private Long userId;
	private String userName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public NodeVo getnVo() {
		return nVo;
	}

	public void setnVo(NodeVo nVo) {
		this.nVo = nVo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
