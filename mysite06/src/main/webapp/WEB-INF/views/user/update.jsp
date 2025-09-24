<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/user.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="user">
				<form id="join-form" name="updateForm" method="post" action="${pageContext.request.contextPath }/user/update">				
					<!-- 데이터 입력 -->
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="${userVo.name }">

					<!-- 이메일 수정 불가 -->
					<label class="block-label" for="email">이메일</label>
					<h4>${userVo.email }</h4>
					
					<label class="block-label">비밀번호</label>
					<input name="password" type="password" value="">

					<fieldset>
						<legend>성별</legend>
						<label>여</label> 
						<input type="radio" name="gender" value="Female" <c:if test='${userVo.gender == "Female" }'>checked</c:if>>
						<label>남</label> 
						<input type="radio" name="gender" value="Male" <c:if test='${userVo.gender == "Male" }'>checked</c:if>>
					</fieldset>
					
					<input type="submit" value="수정하기">
					
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>