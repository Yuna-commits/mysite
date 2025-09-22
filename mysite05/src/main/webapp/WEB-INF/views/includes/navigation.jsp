<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<li>
			<sec:authorize access="isAuthenticated()">
				<sec:authentication property="principal" var="authUser"/>
				<c:if test='${authUser.role == "ADMIN" }'>
					<a href="${pageContext.request.contextPath }/admin"><spring:message code="navigation.li.admin"/></a>
				</c:if>
			</sec:authorize>
		</li>
		<li>
			<a href="${pageContext.request.contextPath }"><spring:message code="navigation.li.main"/></a>
		</li>
		<li>
			<a href="${pageContext.request.contextPath }/guestbook"><spring:message code="navigation.li.guestbook"/></a>
		</li>
		<li>
			<a href="${pageContext.request.contextPath }/board"><spring:message code="navigation.li.board"/></a>
		</li>
	</ul>
</div>