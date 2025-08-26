<%@ page import="com.bit2025.mysite.vo.UserVo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	UserVo userVo = (UserVo) session.getAttribute("userVo");
	boolean isFemale = userVo.getGender().equals("Female");
%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/assets/css/user.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="user">
				<form id="join-form" name="updateForm" method="post" action="<%=request.getContextPath() %>/user">
					<input type="hidden" name="a" value="update">
					
					<!-- 데이터 입력 -->
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="<%=userVo.getName() %>">

					<!-- 이메일 수정 불가 -->
					<label class="block-label" for="email">이메일</label>
					<h4><%=userVo.getEmail() %></h4>
					
					<label class="block-label">비밀번호</label>
					<input name="password" type="password" value="">
					
					<fieldset>
						<legend>성별</legend>
						<label>여</label> 
						<input type="radio" name="gender" value="Female" <%=isFemale ? "checked" : "" %>>
						<label>남</label> 
						<input type="radio" name="gender" value="Male" <%=isFemale ? "" : "checked" %>>
					</fieldset>
					
					<input type="submit" value="수정하기">
					
				</form>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/includes/navigation.jsp"/>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>