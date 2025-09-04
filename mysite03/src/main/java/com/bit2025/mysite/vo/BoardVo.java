package com.bit2025.mysite.vo;

import java.util.Date;

public class BoardVo {

	private Long id;
	private String title;
	private String content;
	private Integer hit;
	private Date regDate;

	// 계층형 테이블 속성
	private Integer gNo; // 게시글 그룹 번호
	private Integer oNo; // 게시글 그룹별 답글 번호
	private Integer depth; // 답글의 깊이

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

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Integer getgNo() {
		return gNo;
	}

	public void setgNo(Integer gNo) {
		this.gNo = gNo;
	}

	public Integer getoNo() {
		return oNo;
	}

	public void setoNo(Integer oNo) {
		this.oNo = oNo;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
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

	@Override
	public String toString() {
		return "BoardVo [id=" + id + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", gNo=" + gNo + ", oNo=" + oNo + ", depth=" + depth + ", userId=" + userId + ", userName="
				+ userName + "]";
	}

}
