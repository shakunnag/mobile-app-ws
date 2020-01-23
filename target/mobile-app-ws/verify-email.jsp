<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.shakun.ws.ui.service.UserService"%>
<%@ page import="com.shakun.ws.ui.service.UserServiceImpl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Email Verification Page</title>
</head>
<body>
	<%
		String token = request.getParameter("token");
		UserService userService = new UserServiceImpl();
		boolean isVerified = userService.verifyEmail(token);
		if (isVerified) {
	%>
	<p>Email Verified
	<p>
		<%
			} else {
		%>
	
	<p>Email not Verified
	<p>
		<%
			}
		%>
	
</body>
</html>