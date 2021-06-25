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
<base href="<%=basePath%>">
<title>对象编辑工具</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="js/jquery.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
$(function(){
	$("#content").width($(window).width()-10);
	$("#content").height($(window).height()-10);
	var fromPage=getFromWindow("${param.pageid}");
	var temp=fromPage.LE.getCurrentContent();
	$("#content").val(temp);
});
function lingxSubmit(){

	var fromPage=getFromWindow("${param.pageid}");
	fromPage.LE.setCurrentContent($("#content").val());
	lgxInfo("操作成功");
	closeWindow();
}
</script>
</head>
<body>
<textarea id="content" name="content"></textarea>
</body>
</html> 