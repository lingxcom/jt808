<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String text=com.lingx.core.utils.LingxUtils.getEMS();
out.print(text+"<br>");

Runtime run = Runtime.getRuntime();

long max = run.maxMemory();

long total = run.totalMemory();

long free = run.freeMemory();

long usable = max - total + free;

out.print("最大内存 = " + max/ 1024 / 1024+" MB<br>");
out.print("已分配内存 = " + total/ 1024 / 1024+" MB<br>");
out.print("已分配内存中的剩余空间 = " + free/ 1024 / 1024+" MB<br>");
out.print("最大可用内存 = " + usable/ 1024 / 1024+" MB<br>");
%>
</body>
</html>