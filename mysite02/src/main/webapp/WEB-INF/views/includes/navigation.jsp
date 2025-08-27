<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<li>
		<c:choose>
			<c:when test='${not empty authUser }'>
				<a href="${pageContext.request.contextPath }">${authUser.name }</a>
			</c:when>
			<c:otherwise>
				<a href="${pageContext.request.contextPath }/user?a=loginform">익명</a>
			</c:otherwise>
		</c:choose>
		</li>
		<li><a href="${pageContext.request.contextPath }/guestbook">방명록</a></li>
		<li><a href="${pageContext.request.contextPath }/board">게시판</a></li>
	</ul>
</div>