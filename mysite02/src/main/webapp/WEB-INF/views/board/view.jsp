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
						<td>${boardVo.title }</td>
					</tr>
					<tr>
						<td class="label">글쓴이</td>
						<td>${boardVo.userName }</td>
						<td class="label">조회수</td>
						<td>${boardVo.hit }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">
								${fn:replace(boardVo.contents, newLine, "<br>") }
							</div>
						</td>
					</tr>
				</table>
				
				<div class="bottom">
					<c:if test="${not empty authUser }">
						<c:if test="${authUser.id == boardVo.userId }">
							<a href="${pageContext.request.contextPath }/board?a=modifyform&id=${boardVo.id }">글수정</a>
						</c:if>
					</c:if>
					<a href="${pageContext.request.contextPath }/board?a=replyform&id=${boardVo.id }">답글쓰기</a>
					<a href="${pageContext.request.contextPath }/board">글목록</a>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>