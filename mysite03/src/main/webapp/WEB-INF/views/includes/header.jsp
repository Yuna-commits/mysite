<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="header">
	<h1>${siteVo.title }</h1>
	<ul>
		<c:choose>
			<c:when test='${not empty authUser }'>
				<li><a href="${pageContext.request.contextPath }/user/update">회원정보수정</a><li>
				<li><a href="${pageContext.request.contextPath }/user/logout">로그아웃</a><li>
				<li>${authUser.name }(${authUser.role })님 안녕하세요 ^^;</li>
			</c:when>
			<c:otherwise>
				<li><a href="${pageContext.request.contextPath }/user/login">로그인</a><li>
				<li><a href="${pageContext.request.contextPath }/user/join">회원가입</a><li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>