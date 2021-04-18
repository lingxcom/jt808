<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
try {
	String url=request.getAttribute("url").toString();
	
	response.sendRedirect( url);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} 
%>