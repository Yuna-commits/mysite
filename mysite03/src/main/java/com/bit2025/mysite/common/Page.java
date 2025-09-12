package com.bit2025.mysite.common;

public class Page {
	private final static int PAGE_SIZE = 5; // 한 페이지에 보일 페이지의 수
	private final static int BOARD_SIZE = 5;// 한 페이지에 보일 게시글의 수
	
	private int reqPage; // 사용자 요청 페이지
	private int totalBoard; //총 게시글 수
	private int totalPage; // 총 페이지 수
	private int offset;// 쿼리로 가져올 게시글의 시작점
	private int section; // 페이지 번호 범위 구분
	private int startPage; // 요청 페이지 section의 시작 번호
	private int endPage; // 요청 페이지 section의 끝 번호
	private boolean prev, next; // 이전/다음 section 존재 여부 -> 존재하면 화살표 표시

	public Page(int reqPage, int totalBoard) {
		this.reqPage = reqPage;
		this.totalBoard = totalBoard;
		
		// 1. 총 페이지 수 계산
		totalPage = (int)Math.ceil((double) totalBoard / BOARD_SIZE);

		// 2. offset 계산, reqPage 위치에 존재하는 게시글 추출에 필요
		offset = (reqPage - 1) * BOARD_SIZE;

		// 3. reqPage 범위 계산
		section = (reqPage - 1) / PAGE_SIZE;
		startPage = (section * PAGE_SIZE) + 1;
		endPage = (section + 1) * PAGE_SIZE;
		endPage = (endPage > totalPage) ? totalPage : endPage;

		// 4. 이전/다음 섹션이 있는지 확인
		prev = (section > 0);
		next = (totalPage > endPage);
	}

	public int getReqPage() {
		return reqPage;
	}

	public int getTotalBoard() {
		return totalBoard;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getOffset() {
		return offset;
	}

	public int getSection() {
		return section;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public int getPageSize() {
		return PAGE_SIZE;
	}

	public int getBoardSize() {
		return BOARD_SIZE;
	}
}