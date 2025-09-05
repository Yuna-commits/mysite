<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<c:set var="name" value='${not empty authUser ?  authUser.name : "익명" }'></c:set>
		
		<li><a href="${pageContext.request.contextPath }">${name }</a></li>
		<li><a href="${pageContext.request.contextPath }/guestbook">방명록</a></li>
		<li><a href="${pageContext.request.contextPath }/board?p=1">게시판</a></li>
	</ul>
</div>