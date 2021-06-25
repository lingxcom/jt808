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

<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/avalon.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>

<script type="text/javascript">
var handlerJsp="<%=basePath%>lingx/workflow/center/handler.jsp";
var model=avalon.define({
	$id:"body",
	list:[],
	startWf:function(code){
		openFullWindow('w?d='+code);
	},
	viewW:function(code){
		openFullWindow("lingx/workflow/define/view.jsp?code="+code);
		//openFullWindow('w?d='+code);
	},
	viewF:function(code){
		openFullWindow("lingx/workflow/form/view.jsp?code="+code);
		//openFullWindow('w?d='+code);
	}
});
$.post(handlerJsp,{c:"listWf"},function(json){
	model.list=json.list;
},"json");
</script>
</head>
<body ms-controller="body" style="padding-top:10px;">
<div class="container col-md-4" >
<div   class="panel panel-info ">
  <div class="panel-heading">发起流程</div>
  <div class="panel-body">
   <div ms-repeat-el2="list" style="margin-bottom:5px;">
   <a href="javascript:;" ms-click="startWf(el2.code)">&bull; {{el2.name}} </a>
   <span style="float:right;color:#999;font-size:12px;">
   <a style="color:#999;font-size:12px;" href="javascript:;" ms-click="viewW(el2.code)">流程</a>
   |
   <a style="color:#999;font-size:12px;" href="javascript:;" ms-click="viewF(el2.form)">表单</a>
   </span>
   <div style="color:#999;"> 
   {{el2.remark}}
   <div>
   </div>
  </div>
</div>
</div>
</body>
</html>