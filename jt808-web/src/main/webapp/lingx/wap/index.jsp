<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	if(session.getAttribute("SESSION_USER")==null){
		response.sendRedirect(basePath+"lingx/wap/login.jsp");
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="viewport"
	content="initial-scale=1, width=device-width, maximum-scale=1, user-scalable=no">
<meta name="viewport"
	content="initial-scale=1.0,user-scalable=no,maximum-scale=1"
	media="(device-height: 568px)">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-touch-fullscreen" content="yes">
<meta name="full-screen" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="address=no">
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript">
var handlerJspWf="lingx/workflow/center/handler.jsp";
var model=avalon.define({
	$id:"body",
	listDb:[]
});
$(function(){
	$.post(handlerJspWf,{c:"getWorkflowListByDaiban"},function(json){
		model.listDb=json;
	},"json");
});
</script>
</head>
<body ms-controller="body">
<div class="container-fluid">
<div class="row">
<div class="col-md-12"  style="text-align:center;">
<h1>${SESSION_USER.account }</h1><br>
</div>
<div class="col-md-12">
<div class="list-group">
<!--  <li class="list-group-item">
<a href="w?d=QJLC&is_mobile=true">请假流程</a>
</li> -->
<li class="list-group-item">
<a href="lingx/workflow/wap/wf-start.jsp">发起流程</a>
</li>

<li class="list-group-item">
<a href="lingx/workflow/wap/wf-main.jsp">流程审批</a>
</li>

</div>
</div>
<hr>
<div class="col-md-12">
<div class="list-group">
 <li class="list-group-item" ms-repeat="listDb">
<a ms-attr-href="'w?m=form&is_mobile=true&_TASK_ID='+el.id">{{el.workflow}}</a>
</li>
</div>
</div>
</div>
</div>
</div>
</body>
</html>