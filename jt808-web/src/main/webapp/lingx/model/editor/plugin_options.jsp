<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*,org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String id=request.getParameter("id");
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
	JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
	String options=jdbc.queryForObject("select options from tlingx_plugin where id=?", String.class,id);
	request.setAttribute("options", options);
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
var pluginId="<%=id %>";
var handlerUrl="<%=basePath%>lingx/model/editor/handler.jsp";
var fromPageId='${param.pageid}';
var options=${options};
var index=0;
var table;
$(function(){
	table=$("#options");
	for(var tmp in options){
		var tr=$("<tr/>");
		tr.append($("<td>"+ ++index +"</td>"));
		tr.append($("<td><input id='name_"+index+"' class='name' value='"+tmp+"'/></td>"));
		tr.append($("<td><input id='value_"+index+"' value='"+options[tmp]+"'/></td>"));
		tr.append($("<td><a href='javascript:;' onclick='del(this);'>删除</a></td>"));
		table.append(tr);
	}
	
});

function add(){
	var tr=$("<tr/>");
	tr.append($("<td>"+ ++index +"</td>"));
	tr.append($("<td><input id='name_"+index+"' class='name' value=''/></td>"));
	tr.append($("<td><input id='value_"+index+"' value=''/></td>"));
	tr.append($("<td><a href='javascript:;' onclick='del(this);'>删除</a></td>"));
	table.append(tr);
}
function del(param){
	if(!confirm("确定删除吗？"))return;
	var tr=$(param).parent().parent();
	tr.remove();
}

function save(){
	var nameEls=$(".name");
	options={};
	
	for(var i=0;i<nameEls.length;i++){
		var obj=nameEls[i];
		var nameEl=$(obj);
		var valueEl=$("#"+nameEl.attr("id").replace("name","value"));
		if(!nameEl.val()||!valueEl.val()){
			lgxInfo("保存失败，参数名可参数值不可为空");
			return;
		}else{
			options[nameEl.val()]=valueEl.val();
		}
	}
	var json=(JSON.stringify(options));
	$.post(handlerUrl,{c:"pluginSetOptions",id:pluginId,options:json},function(json){
		lgxInfo(json.message);
	},"json");
}
function lingxSubmit(){
	save();
	if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){
    	getFromWindow(fromPageId).reloadGrid();
	}
	closeWindow();
}
</script>
</head>
<body style="margin: 0px; padding: 0px; background-color: #dfe9f6;">
<table width="100%">
<tr>
<td align="center">

	<table id="options" width="450" style="margin:10px;">
	<tr><th>序号</th><th>参数名</th> <th>参数值</th> <th>操作</th></tr>
	</table>
	<button onclick="add()">添加参数</button>
	<button onclick="save()">保存设置</button>
	</td>
</tr>
</table>
</body>
</html>
