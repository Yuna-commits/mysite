<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>  
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<!-- 게시글 검색 -->
				<form id="search_form" action="${pageContext.request.contextPath }/board" method="get">
					<input type="text" id="kwd" name="kwd" value="${keyword }">
					<input type="submit" value="찾기">
				</form>
				
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<!-- 게시글 리스트 출력 -->
					<c:set var="index" value='${map["page"].totalBoard - map["page"].offset }'></c:set>
					<c:forEach var="vo" items='${map["list"] }' varStatus="status">
						<tr>
							<td>${index }</td>
							<td style="text-align:left; padding-left:${(vo.depth) * 20 }px">
								<!-- 답글만 reply 이미지 표시 -->
								<c:if test="${vo.depth > 0 }">
									<img src="${pageContext.request.contextPath }/assets/images/reply.png">
								</c:if>	
								<a href='${pageContext.request.contextPath }/board/view/${vo.id }?p=${map["page"].reqPage }&kwd=${map.keyword }'>${vo.title }</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<!-- 관리자이거나 게시글 작성자 == 로그인 사용자인 경우(글쓴이 본인)만 삭제 가능 -->
							<td><c:if test='${authUser.role == "ADMIN" || (not empty authUser && authUser.id == vo.userId) }'>
									<a href='${pageContext.request.contextPath }/board/delete/${vo.id }?p=${map["page"].reqPage }&kwd=${map.keyword }'
										class="del" style='background:url("${pageContext.request.contextPath }/assets/images/recycle.png") no-repeat 0 0'>
									</a>
							</c:if></td>
						</tr>
						<c:set var="index" value="${index - 1 }"></c:set>
					</c:forEach>
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<!-- prev 버튼은 이전 섹션의 첫 번째 페이지로 이동 -->
						<c:if test='${map["page"].prev }'>
							<li><a href='${pageContext.request.contextPath }/board?p=${map["page"].startPage - map["page"].pageSize}&kwd=${keyword }'>◀</a></li>
						</c:if>
						
						<c:forEach var="num" begin='${map["page"].startPage }' end='${map["page"].endPage }'>
							<li <c:if test='${map["page"].reqPage == num}'> class="selected"</c:if>>
								<a href="${pageContext.request.contextPath }/board?p=${num}&kwd=${keyword }">${num }</a>
							</li>
						</c:forEach>
						<!-- next 버튼은 다음 섹션의 첫 번째 페이지로 이동 -->
						<c:if test='${map["page"].next }'>
							<li><a href='${pageContext.request.contextPath }/board?p=${map["page"].endPage + 1 }&kwd=${keyword }'>▶</a></li>
						</c:if>
					</ul>
				</div>

				<div class="bottom">
					<c:if test="${not empty authUser }">
						<a href='${pageContext.request.contextPath }/board/write?p=${map["page"].reqPage }&kwd=${map.keyword }' id="new-book">글쓰기</a>
					</c:if>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>