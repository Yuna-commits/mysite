package com.bit2025.mysite.vo;

public class NodeVo {
	private int gNo; // 게시글 그룹 번호
	private int oNo; // 게시글 그룹별 답글 번호
	private int depth; // 답글의 깊이

	public int getgNo() {
		return gNo;
	}

	public void setgNo(int gNo) {
		this.gNo = gNo;
	}

	public int getoNo() {
		return oNo;
	}

	public void setoNo(int oNo) {
		this.oNo = oNo;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
