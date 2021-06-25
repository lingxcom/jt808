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
var handlerUrl="lingx/model/editor/handler.jsp";
var fromPageId='${param.pageid}';
var index=1;
var table;
$(function(){
	Lingx.getRootWindow().resizeWindow({height:400+Lingx.PANEL_HEIGHT});
	if(store.get("name")){
		$(".tdname").html(store.get("name"));
	}
	table=$("#options");
	 add();
	/* for(var tmp in options){
		var tr=$("<tr/>");
		tr.append($("<td>"+ ++index +"</td>"));
		tr.append($("<td><input id='name_"+index+"' class='name' value='"+tmp+"'/></td>"));
		tr.append($("<td><input id='value_"+index+"' value='"+options[tmp]+"'/></td>"));
		tr.append($("<td><a href='javascript:;' onclick='del(this);'>删除</a></td>"));
		table.append(tr);
	} */
	
	$(window).bind("resize",function(){
		$("#div").height($(window).height());
	})
});



function add(){
	var tr=$("<tr/>");
	tr.append($("<td>"+ index++ +"</td>"));
	tr.append($("<td><input id='name_"+index+"' class='name' value=''/></td>"));
	tr.append($("<td><select id='value_"+index+"' ><option>文本</option><option>数字</option><option>金额</option></select></td>"));
	tr.append($("<td><a href='javascript:;' onclick='del(this);'>删除</a></td>"));
	table.append(tr);
}
function del(param){
	if(!confirm("确定删除吗？"))return;
	var tr=$(param).parent().parent();
	tr.remove();
}

function lingxSubmit(){
	var name=store.get("name");
	var nameEls=$(".name");
	var array=new Array();
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
			array.push({name:nameEl.val(),type:valueEl.val()});
		}
	}
	if(!confirm("检查通过，确认创建“"+name+"”对象吗？"))return;
	var json=(JSON.stringify(array));
	$.post(handlerUrl,{c:"createEntityAuto",name:name,options:json},function(json){
		lgxInfo(json.message);
		getFromWindow(fromPageId).reloadGrid();
		closeWindow();
	},"json");

}
</script>

<style type="text/css">
body{
padding:0px;
}
tr{height:24px;text-align:center;}
</style>
</head>
<body style="background-color:#dfe9f6;">
<div id="div" style="padding:0px;width:100%;height:400px;overflow:auto;">
<table width="100%">
<tr><td colspan="4" align="center"><span  class="tdname" style="font-size:14px;" ></span> </td> </tr>
<tr><td><span style="color:#999">说明：在建立树形模型时属性名“树型上级”、“树型状态”、“树型图标”，对应fid、state、icon_cls。</span> </td></tr>
<tr>
<td align="center" >

	<table id="options" width="450" cellSpacing="0" cellPadding="0" >
	<tr><th>序号</th><th>属性名称</th> <th>数据类型</th> <th>操作</th></tr>
	</table>
	<button onclick="add()">添加属性</button><!--
	<button onclick="save()">保存设置</button>  -->
	</td>
</tr>
</table>
</div>
</body>

</html>