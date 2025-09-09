<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<c:set var="name" value='${not empty authUser ?  authUser.name : "익명" }'></c:set>
		<li><c:if test='${authUser.role == "ADMIN" }'>
				<a href="${pageContext.request.contextPath }/admin">${name }</a>
		</c:if></li>
		<li><a href="${pageContext.request.contextPath }">메인</a></li>
		<li><a href="${pageContext.request.contextPath }/guestbook">방명록</a></li>
		<li><a href="${pageContext.request.contextPath }/board">게시판</a></li>
	</ul>
</div>