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
<title>实体列表</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">

var request_params=${REQUEST_PARAMS};
var fromPageId='${param.pageid}';
var entityCode='${entityCode }';
var methodCode='${methodCode }';
var entityId='${entityId }';
var params=${_params};

params=request_params;
removeDefaultAttribute(params);
var extparam='${extparam}';

$(function(){
	$("#tree").tree({lines:true,url:"<%=basePath %>e?e="+entityCode+"&m="+methodCode+"&lgxsn=1"
			,onLoadSuccess:function(){
		//var height=$(window.document.body).height()+45;
		//var width=$(window.document.body).width()+14;
		//window.parent.resizeWin(width,height);
		$("#loading-mask").remove();
		$("#loading").remove();
	}});
});

function ok(){
	var node=$("#tree").tree("getSelected");
	var id1=indexPopId();//取出缓存中的ID
	var win=getFromWindow();//取得来自window
	if(node){
		$(win.document).find("#"+id1+"_val").val(node.id);//对原来的控件设置
		$(win.document).find("#"+id1+"_key").val(node.text);//对原来的控件设置
	}else{
		$(win.document).find("#"+id1+"_val").val('');//对原来的控件设置
		$(win.document).find("#"+id1+"_key").val('');//对原来的控件设置
	}
	closeWin();
}
</script>
</head>

<body class="easyui-layout" >
<div id='loading-mask'></div>
<div id='loading'>
<div class='loading'></div>
</div>
<div  region="center" border="false" style="overflow:auto;" data-options="onResize:function(w,h){}" title="" >
<ul id="tree"></ul>
</div>

<div id="south" data-options="region:'south',split:false,border:0,onResize:function(w,h){}"  style="height:50px;overflow:hidden;background:#efefef;">
<hr/>

<table id="table2" width="100%" style="margin:0px;padding:0px;"  border="0" cellpadding="0" cellspacing="0">
<tr><td align="right">
<input style="width:80px;margin-right:5px;" type="button" value="确定" onclick="ok();" />
<input style="width:80px;margin-right:15px;" type="button" value="取消"  onclick="closeWin();"/>
 </td></tr>
</table>
</div>
</body>

</html>