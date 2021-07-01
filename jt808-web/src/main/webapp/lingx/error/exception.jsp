<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%

String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
</head>

<body>
<div style="margin:2px;color:red;font-size:12px;">
<%
Exception e=(Exception)request.getAttribute("e");
out.println(e.toString());
out.println("<br/>");
for(StackTraceElement e1:e.getStackTrace()){
	out.println(e1.toString());
	out.println("<br/>");
}
%>
</div>
</body>
</html>