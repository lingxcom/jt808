<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.model.bean.UserBean,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.core.service.*" %>

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
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>js/store.js"></script>
<script type="text/javascript">
var fromPageId='${param.pageid}';
$(function(){
	Lingx.getRootWindow().resizeWindow({height:160+Lingx.PANEL_HEIGHT});
	if(store.get("name")){
		$("#name").val(store.get("name"));
	}
});
function lingxSubmit(){
	var name=$("#name").val();
	var file=$("#file").val();
	if(!name){lgxInfo("对象名称不可为空！");return ;}
	if(!file){lgxInfo("对象名称不可为空！");return ;}
	store.set("name",name);
	store.set("file",file);
	window.location.href="lingx/model/editor/create/excel_step2.jsp?pageid="+fromPageId+"&path="+file;
}

function selectFile(){
	openWindow("文件上传框","lingx/template/upload/upload.jsp?cmpId=file&type=URL");
}

function lingxSet(options){
	if(options.value.indexOf(".xlsx")>0)
	$("#file").val(options.value);
	else
		lgxInfo("只能上传Excel文件，后缀名为xlsx");
}
</script>
<style type="text/css">
body{
padding:10px;
}
tr{height:30px;}
</style>
</head>
<body>
<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tr><td><span style="color:#999">说明：Excel文件的第一行为模型属性，第二行开始为数据；只读取Sheet1。</span> </td></tr>
<tr><td><span style="color:red;">*</span>模型名称：<input id="name" /></td></tr>
<tr><td><span style="color:red;">*</span>Excel文件：<input id="file" readonly onclick="selectFile()" /><button onclick="selectFile()">选择...</button></td></tr>

<tr><td><span style="color:#999"> 点击“确定”进入下一步。</span> </td></tr>
</table>

</body>

</html>