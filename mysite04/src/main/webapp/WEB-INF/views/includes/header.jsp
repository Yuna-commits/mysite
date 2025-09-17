<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	window.addEventListener("load", function() {
		var anchors = document.querySelectorAll('#languages a');
		anchors.forEach(function(element) {
			// click event
			element.addEventListener("click", function(event) {
				// 기본 핸들러의 기능(link to href) 막기
				event.preventDefault();
				// "data-lang"을 쿠키에 저장
				document.cookie = 
					"lang=" + this.getAttribute("data-lang") + ";" +
					"path=" + "${pageContext.request.contextPath}" + ";" +
					"max-age=" + (30*24*60*60);
					
				location.reload();
			});
		});
	});
</script>
<div id="header">
	<h1>${site.title }</h1>
	<div id="languages">
		<c:choose>
			<c:when test='${lang == "ko" }'>
				<a href="" data-lang="ko" class="active">KO</a> <a href="" data-lang="en">EN</a>
			</c:when>
			<c:otherwise>
				<a href="" data-lang="ko">KO</a> <a href="" data-lang="en" class="active">EN</a>
			</c:otherwise>			
		</c:choose>
	</div>
	<ul>
		<c:choose>
			<c:when test="${not empty authUser }">
				<li><a href="${pageContext.request.contextPath }/user/update"><spring:message code="header.gnb.settings"/></a><li>
				<li><a href="${pageContext.request.contextPath }/user/logout"><spring:message code="header.gnb.logout"/></a><li>
				<li>${authUser.name }(${authUser.role }) <spring:message code="header.gnb.greeting"/></li>
			</c:when>
			<c:otherwise>
				<li><a href="${pageContext.request.contextPath }/user/login"><spring:message code="header.gnb.login"/></a><li>
				<li><a href="${pageContext.request.contextPath }/user/join"><spring:message code="header.gnb.join"/></a><li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>