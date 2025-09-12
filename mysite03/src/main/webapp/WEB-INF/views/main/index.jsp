<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<!-- <link href="/mysite02/assets/css/main.css" rel="stylesheet" type="text/css"> -->
<link href="${pageContext.request.contextPath }/assets/css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<!-- <jsp:include page="/WEB-INF/views/includes/header.jsp"/> -->
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="wrapper">
			<div id="content">
				<div id="site-introduction">
				
					<c:if test="${not empty authUser }">
						<img id="profile" src="${pageContext.request.contextPath }${siteVo.profileURL }" style="width:200px">
						<h2>${siteVo.welcomeMessage }</h2>
					</c:if>
					
					<p>
						${siteVo.description }<br><br>
						<a href="${pageContext.request.contextPath }/guestbook">방명록</a>에 글 남기기<br>
					</p>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<!-- <jsp:include page="/WEB-INF/views/includes/navigation.jsp"/> -->
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
		<!-- <jsp:include page="/WEB-INF/views/includes/footer.jsp"/> -->
	</div>
</body>
</html>