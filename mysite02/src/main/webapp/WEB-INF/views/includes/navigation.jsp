<%@ page import="com.bit2025.mysite.vo.UserVo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserVo authUser = (UserVo)session.getAttribute("authUser");
%>
<div id="navigation">
	<ul>
		<li>
		<% if (authUser != null) { %>
		<a href="<%=request.getContextPath() %>"><%=authUser.getName() %></a>
		<% } else { %>
		<a href="<%=request.getContextPath() %>/user?a=loginform">익명</a>
		<% } %>
		</li>
		<li><a href="<%=request.getContextPath() %>/guestbook">방명록</a></li>
		<li><a href="<%=request.getContextPath() %>/board">게시판</a></li>
	</ul>
</div>