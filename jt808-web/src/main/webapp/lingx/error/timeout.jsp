<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>用户登陆超时</title>

<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">
function gotoLogin(){
	try{
	var win=getRootWindow();
	win.location.href="d?c=logout";
	}catch(e){
		window.location.href="d?c=logout";
	}
}
</script>
</head>

<body>
<div style="margin:2px;color:red;font-size:12px;">
用户登陆超时，请<a href="javascript:;" onclick="gotoLogin();">重新登陆</a>。
</div>
</body>
</html>