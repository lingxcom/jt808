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

var grid=new GridCombo({entityCode:entityCode,methodCode:methodCode,entityId:entityId,params:params,extparam:extparam});


function getEntityFields(){
	return grid.getEntityFields;
}
function initGrid(w,h){
	grid.initGrid(w,h);
}
function openWin(a){
	grid.openWin(a);
}
function reloadGrid(){
	grid.reloadGrid();
}
</script>
</head>

<body class="easyui-layout" >
<div id='loading-mask'></div>
<div id='loading'>
<div class='loading'></div>
</div>
<div  region="center" border="false" style="overflow:hidden;" data-options="onResize:function(w,h){initGrid(w,h);}" title="" >
<table id="EntityGrid"></table> 
<div id="xb-toolbar1" ><!--工具栏 -->
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px" id="btns">
				</td>
				<td style="text-align:right;padding-right:2px" >
					<input id="searchbox" class="easyui-searchbox_bak" data-options=""></input>
				</td>
			</tr>
		</table>
</div>
<div id="mm" style="width:150px">
</div>

</div>

<div id="south" data-options="region:'south',split:false,border:0,onResize:function(w,h){}"  style="height:50px;overflow:hidden;background:#efefef;">
<hr/>

<table id="table2" width="100%" style="margin:0px;padding:0px;"  border="0" cellpadding="0" cellspacing="0">
<tr><td align="right">
<input style="width:80px;margin-right:5px;" type="button" value="确定" onclick="grid.ok();" />
<input style="width:80px;margin-right:15px;" type="button" value="取消"  onclick="closeWin();"/>
 </td></tr>
</table>
</div>
</body>

</html>