package com.bit2025.mysite.common;

public class Page {
	public static final int PAGE_COUNT = 5; // 한 페이지에 보일 페이지의 수
	public static final int BOARD_COUNT = 5;// 한 페이지에 보일 게시글의 수
	
	private int reqPage; // 사용자 요청 페이지
	private int totalBoard; //총 게시글 수
	private int totalPage; // 총 페이지 수
	private int offset;// 쿼리로 가져올 게시글의 시작점
	private int section; // 페이지 번호 범위 구분
	private int startPage; // 요청 페이지 section의 시작 번호
	private int endPage; // 요청 페이지 section의 끝 번호
	private boolean prev, next; // 이전/다음 section 존재 여부 -> 존재하면 화살표 표시

	/**
	 * ex)
	 * reqPage : 3 
	 * totalBoard : 32 
	 * totalPage : (32 / 5) + (0 || 1) = 7 페이지
	 * offset : (3 - 1) * 5 = 10 -> select ... limit 5 offset 10 : 10번째부터 그 다음 5개의 결과 조회
	 * section : (3 - 1) / 5 = 0 -> reqPage는 0번째에 위치 
	 * startPage : (0 * 5) + 1 = 1 -> 한 페이지의 시작은 1 페이지 
	 * endPage : (0 + 1) * 5 = 5 -> 한 페이지의 끝은 5 페이지 (총 페이지 수가 5가 안되면 endPage = totalPage) 
	 * prev : 현재 섹션의 이전 페이지 없음 = false 
	 * next : 현재 섹션의 다음 페이지 있음 = true 
	 * section  [0] 	    [1] 
	 * page [1 2 3 4 5] [6 7 8 9 10]
	 */
	public Page(int reqPage, int totalBoard) {
		this.reqPage = reqPage;
		this.totalBoard = totalBoard;
		
		// 1. 총 페이지 수 계산
		totalPage = totalBoard / BOARD_COUNT;
		totalPage += (totalBoard % BOARD_COUNT == 0 ? 0 : 1);

		// 2. offset 계산, reqPage 위치에 존재하는 게시글 추출에 필요
		offset = (reqPage - 1) * BOARD_COUNT;

		// 3. reqPage 범위 계산
		section = (reqPage - 1) / PAGE_COUNT;
		startPage = (section * PAGE_COUNT) + 1;
		endPage = (section + 1) * PAGE_COUNT;
		endPage = (endPage > totalPage) ? totalPage : endPage;

		// 4. 이전/다음 섹션이 있는지 확인
		prev = (section > 0);
		next = (totalPage > endPage);
	}

	public int getReqPage() {
		return reqPage;
	}

	public void setReqPage(int reqPage) {
		this.reqPage = reqPage;
	}

	public int getTotalBoard() {
		return totalBoard;
	}

	public void setTotalBoard(int totalBoard) {
		this.totalBoard = totalBoard;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

}