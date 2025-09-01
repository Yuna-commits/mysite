<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>  
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%pageContext.setAttribute("newLine", "\n"); %>
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
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="4">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${cView.title }</td>
					</tr>
					<tr>
						<td class="label">글쓴이</td>
						<td>${cView.userName }</td>
						<td class="label">조회수</td>
						<td>${cView.hit }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">
								${fn:replace(cView.content, newLine, "<br>") }
							</div>
						</td>
					</tr>
				</table>
				
				<form class="board-form" method="post" action="${pageContext.request.contextPath }/board">
					<input type="hidden" name="a" value="writeform">
					<input type="hidden" name="reply" value=${true }>
					<input type="hidden" name="gNo" value="${cView.gNo }">
					<input type="hidden" name="oNo" value="${cView.oNo }">
					<input type="hidden" name="depth" value="${cView.depth }">
					
					<div class="bottom">
						<a href="${pageContext.request.contextPath }/board">글목록</a>
						<c:if test="${authUser.id == cView.userId }">
							<a href="${pageContext.request.contextPath }/board?a=modifyform&id=${cView.id }">글수정</a>
						</c:if>
						<input type="submit" value="답글등록">
					</div>
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>