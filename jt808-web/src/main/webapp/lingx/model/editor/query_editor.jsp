<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*,org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
	JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
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
Ext.onReady(function(){
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			region: 'west',
			//title: '条件',
            split: true,
            autoScroll:true,
            border:true,
            width: 162,
            margins: '2 0 2 2',
            contentEl:"west"
		},{
			region: 'center',
			//title: '属性',
            margins: '2 2 2 0',
            contentEl:"center",
            bodyStyle:"background:#dfe9f6;",
		},{
			region: 'south',
			//title: 'JSON格式',
			height:90,
            margins: '2 2 2 2',
            bodyStyle:"background:#dfe9f6;",
            contentEl:"south"
		}]
	});
});

var list=[];
var ccode="";
function reloadList(){
	$(".list").empty();
	for(var i=0;i<list.length;i++){
		var obj=list[i];
		$(".list").append('<li><a href="javascript:;" onclick="edit(\''+obj.code+'\')">'+obj.code+'</a></li>');
	}
	$(".list").append('<li><a href="javascript:;" onclick="add()">添加条件</a></li>');
	toJSON();
}
function add(){
	var json={name:"New",code:"new",xtype:"textfield"};
	list.push(json);
	putJSON(json);
	reloadList();
}
function edit(code){
	for(var i=0;i<list.length;i++){
		var json=list;
		if(code==json[i].code){
			putJSON(json[i]);
			break;
		}
	}
}
function putJSON(json){
	ccode=json.code;
	$("#name").val(json.name);
	$("#code").val(json.code);
	$("#xtype").val(json.xtype);
	$("#sql").val(json.sql);
	$("#url").val(json.url);
}

function saveJSON(){
	var exists=false;
	for(var i=0;i<list.length;i++){
		var json=list;
		if(ccode==json[i].code){
			exists=true;
			json[i].name=$("#name").val();
			json[i].code=$("#code").val();
			json[i].xtype=$("#xtype").val();
			json[i].sql=$("#sql").val();
			json[i].url=$("#url").val();
			ccode=$("#code").val();
			break;
		}
	}
	if(!exists){
		lgxInfo("保存失败，请先添加条件！");
	}
	reloadList();
}
function toJSON(){
	$("#textarea").val(JSON.stringify(list));
}
function parseJSON(){
	list=JSON.parse($("#textarea").val());
	reloadList();
}
</script>
</head>
<body id="body" style="margin: 0px; padding: 0px; background-color: #dfe9f6;">
<div style="display:none;">
<div id="west">
<ul class="list">
<li><a href="javascript:;" onclick="add()">添加条件</a></li>
</ul>
</div>
<div id="center" style="padding:10px;background-color: #dfe9f6;">
<table>
<tr><td>显示名称:</td><td><input id="name" onblur="saveJSON()"></td><td style="color:#aaa">用户查询时显示的</td></tr>
<tr><td>调用代码:</td><td><input id="code"  onblur="saveJSON()"></td><td style="color:#aaa">后台调用</td></tr>
<tr><td>输入控件:</td><td><input id="xtype"  onblur="saveJSON()"></td><td style="color:#aaa">输入控件</td></tr>
<tr><td>查询模版:</td><td><input id="sql"  onblur="saveJSON()"></td><td style="color:#aaa">可不填，不填为代码=</td></tr>
<tr><td>URL值:</td><td><input id="url"  onblur="saveJSON()"></td><td style="color:#aaa">选择控件的选择项URL</td></tr>
<tr>
<td colspan="3" style="color:#aaa">
注：查询模版是SQL片断，已有and标记。模板中用${code}来替换为实际值。<br>
例： id in (select user_id from table where name like '%\${code}%')
URL值:
e?e=tlingx_option&m=items&lgxsn=1&issearch=1&code=SF
e?e='+field.refEntity+'&m=combo&lgxsn=1&issearch=1
</td>
</tr>
</table>
</div>
<div id="south" style="text-align:center;background-color: #dfe9f6;">
<textarea id="textarea" style="width:100%;height:62px;"></textarea><br/>

<button onclick="parseJSON()">导入JSON</button>
</div>
</div>
</body>
</html>
