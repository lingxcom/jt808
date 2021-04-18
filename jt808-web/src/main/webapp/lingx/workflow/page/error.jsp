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
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程主页</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="js/jquery.js"></script>
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">

<script type="text/javascript">
function lingxSave(){
	alert("save");
}
function lingxSubmit(){
	alert("submit");
}

var s=5;
$(function(){
	setInterval("timeout()",1000);	
});

function timeout(){
	s--;
	$("#close").text(s+"秒后自动关闭");
	if(0==s){
		window.close();
	}
}
</script>
</head>
<body>
<br/>
<div class="container" >
<div class="well">

<span style="color:red;">
工作流错误 :<br/>
<%
java.util.Map<String,Object> map=(java.util.Map<String,Object>)request.getAttribute("REQUEST_MESSAGES");

for(String s:map.keySet()){
	out.println(s+"-"+map.get(s)+"<br/>");
}
%>

<br/>
<button onclick="window.close();" type="button">关闭窗口</button>
<br/>
<span id="close" style="color:#999;font-size:12px">
5秒后自动关闭
</span>
</span>
</div></div>
</body>

</html>