<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*,org.springframework.jdbc.core.JdbcTemplate"%>
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
<title>代码编辑</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%>
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>js/json2.js"></script>
<script type="text/javascript">
var handlerUrl="<%=basePath%>lingx/model/editor/handler.jsp";

function lingxSubmit(){
	var sql=$("#content").val();
	if(sql){
		$.post(handlerUrl,{c:"exeSQL",sql:sql},function(json){
			$("#message").html(json.message);
		},"json");
	}else{
		lgxInfo("SQL不可为空");
	}
	//closeWindow();
}
</script>
<style type="text/css">
textarea{
font-size:14px;
line-height:28px;
}
</style>
</head>
<body style="margin: 0px; padding: 0px; background-color: #dfe9f6;overflow:auto;" >
<table style="margin: 0px; padding: 0px;width:100%; "border="0" cellSpacing="0" cellPadding="0">
<tr>
<td>
例：
alter table tlingx_org add icon_cls varchar(16) DEFAULT 'Group';
</td>
</tr>
<tr>
<td align="center">
<textarea id="content" style="width:99%;height:100px;"></textarea>
</td>
</tr>
<tr>
<td  >
<textarea id="message" style="width:99%;height:200px;"></textarea>
</td>
</tr>
</table>
</body>
</html>
