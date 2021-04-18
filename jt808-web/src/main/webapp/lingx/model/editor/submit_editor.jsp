<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return;
java.io.File f=new java.io.File(request.getRealPath("temp"));
if(f.exists()){
org.apache.commons.io.FileUtils.cleanDirectory(f);
}

String lingxUpdateEnabled=lingx.getConfigValue("lingx.update.enabled","false");
String lingxUpdateSecret=lingx.getConfigValue("lingx.update.secret",lingx.uuid());
request.setAttribute("lingxUpdateEnabled",lingxUpdateEnabled);
request.setAttribute("lingxUpdateSecret",lingxUpdateSecret);

request.setAttribute("submit_url",lingx.getConfigValue("lingx.update.submit.url",""));
request.setAttribute("submit_secret",lingx.getConfigValue("lingx.update.submit.secret",""));
request.setAttribute("submit_text",lingx.getConfigValue("lingx.update.submit.text",""));
request.setAttribute("submit_value",lingx.getConfigValue("lingx.update.submit.value",""));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<SCRIPT type="text/javascript">
var handler="<%=basePath %>lingx/model/update/handler.jsp";
var idsArray=new Array();
var textsArray=new Array();

function addObject(etype){
	var value=$("#entity_value").val();
	var text=$("#entity_text").val();
	if(!value){text="";value="";}
	var url="e?e="+etype+"&m=combogrid2&cmpId=entity&valueField=code&value="+value+"&text="+encodeURI(encodeURI(text));
	openWindow("选择模型",url);
}
function clearObject(){
	$("#entity_value").val("");
	$("#entity_text").val("");
}
function saveData(){
	var url=$("#url").val();
	var secret=$("#secret").val();
	if(!url){
		alert("操作失败，远程地址不可为空!");return;
	}
	if(!secret){
		alert("操作失败，远程密钥不可为空!");return;
	}
	
	$.post("lingx/model/editor/submit_handler.jsp",{c:"saveData",url:url,secret:secret,value:$("#entity_value").val(),text:$("#entity_text").val()},function(json){
		lgxInfo(json.message);
	},"json");
	
}
function resetSecret(){
	$.post("lingx/model/editor/submit_handler.jsp",{c:"resetSecret"},function(json){

		$("#localsecret").html(json.secret);
	},"json");
}

function ok(){
	var url=$("#url").val();
	var secret=$("#secret").val();
	if(!url){
		alert("操作失败，远程地址不可为空!");return;
	}
	if(!secret){
		alert("操作失败，远程密钥不可为空!");return;
	}
	var ev=$("#entity_value").val();
	if(!ev){
		alert("操作失败，提交模型不可为空!");return;
	}
	if(!confirm("确定将数据提交到远程服务器吗？"))return;
	$.post("lingx/model/editor/submit_handler.jsp",{c:"submitData",url:url,secret:secret,value:$("#entity_value").val(),text:$("#entity_text").val()},function(json){
		lgxInfo(json.message);
	},"json");
}

function ok2(){
	var url=$("#url").val();
	var secret=$("#secret").val();
	if(!url){
		alert("操作失败，远程地址不可为空!");return;
	}
	if(!secret){
		alert("操作失败，远程密钥不可为空!");return;
	}
	
	if(!confirm("确定将数据提交到远程服务器吗？"))return;
	$.post("lingx/model/editor/submit_handler.jsp",{c:"submitFMO",url:url,secret:secret,type:1},function(json){
		lgxInfo(json.message);
	},"json");
}

function ok3(){
	var url=$("#url").val();
	var secret=$("#secret").val();
	if(!url){
		alert("操作失败，远程地址不可为空!");return;
	}
	if(!secret){
		alert("操作失败，远程密钥不可为空!");return;
	}
	
	if(!confirm("确定将数据提交到远程服务器吗？"))return;
	$.post("lingx/model/editor/submit_handler.jsp",{c:"submitFMO",url:url,secret:secret,type:2},function(json){
		lgxInfo(json.message);
	},"json");
}


function ok4(){
	var url=$("#url").val();
	var secret=$("#secret").val();
	if(!url){
		alert("操作失败，远程地址不可为空!");return;
	}
	if(!secret){
		alert("操作失败，远程密钥不可为空!");return;
	}
	
	if(!confirm("确定将数据提交到远程服务器吗？"))return;
	$.post("lingx/model/editor/submit_handler.jsp",{c:"submitFMO",url:url,secret:secret,type:3},function(json){
		lgxInfo(json.message);
	},"json");
}



</SCRIPT>
<style type="text/css">
td{
height:26px;
}
</style>
</head>
<body style="background-color:#dfe9f6;padding:10px;">
<TABLE id="table-form" width="100%" height="100%" border="0" cellSpacing="0" cellPadding="0" >
<tr><td  align="right"><span style="color:red">*</span>远程地址：</td><td><input id="url" style="width:360px" value="${submit_url }"> </td></tr>
<tr><td  align="right"><span style="color:red">*</span>远程密钥：</td><td><input id="secret" style="width:360px"  value="${submit_secret }"> </td></tr>
<tr><td  align="right"><span style="color:red">*</span>提交模型：</td><td>
<input type="hidden" id="entity_value"  value="${submit_value }"/><input id="entity_text" style="width:360px"  readonly="readonly"  value="${submit_text }"/>
<button onclick="addObject('tlingx_entity');">选择</button><button onclick="clearObject()">清空</button>

</td></tr>
<tr><td align="center" colspan="2" >
<button onclick="saveData()" style="width:80px">保存设置</button> 
<button onclick="ok()" style="width:80px">提交数据</button> 
<br>
<br>
<br>
<div style="width:100%;text-align:left;">

<button onclick="ok2()" style="width:100px">提交功能</button> 
<button onclick="ok3()" style="width:100px">提交菜单</button> 
<button onclick="ok4()" style="width:100px">提交字典</button> 
</div>

</td></tr>
<tr><td align="center" colspan="2" >&nbsp;
</td></tr>
</tr>
<tr><td align="center" colspan="2" >&nbsp;
</td></tr>
</tr>
<tr><td align="center" colspan="2" >&nbsp;
</td></tr>
</tr>
<tr><td align="center" colspan="2" >
本机是否接受远程提交：${lingxUpdateEnabled } <br>
本机更新密钥：<span id="localsecret">${lingxUpdateSecret }</span>&nbsp;&nbsp;<button onclick="resetSecret()">重置本机密钥</button>
</td></tr>
</TABLE>
</body>
</html>