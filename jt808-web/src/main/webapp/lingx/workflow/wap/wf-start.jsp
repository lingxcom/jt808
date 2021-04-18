<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";%>
<!DOCTYPE HTML>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务办理</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/avalon.js"></script>
<link href="js/mui/css/mui.min.css" rel="stylesheet">
<link href="lingx/js/resources/css/workflow.css" rel="stylesheet">
<script type="text/javascript" src="js/mui/js/mui.min.js"></script>
<script type="text/javascript">
$(function(){
	mui('.mui-scroll-wrapper').scroll();
	var handlerJspWf="lingx/workflow/center/handler.jsp";
	var model=avalon.define({
		$id:"body",
		list:[],
		start:function(code){
			window.location.href="w?d="+code+"&is_mobile=true";
		}
	});
	$(function(){
		$.post(handlerJspWf,{c:"listWf"},function(json){
			model.list=json.list;
		},"json");
	});
});
</script>
</head>
<body ms-controller="body">
<header id="default_header" class="mui-bar mui-bar-nav" style="background-color:#5b8dca;">
			<a class="mui-action-back mui-btn mui-btn-link mui-pull-left" style="color:#fff;" onclick="close()">后退</a>
			<!-- <a id="submitBtn" class="mui-btn mui-btn-link mui-pull-right" style="color:#fff;" onclick="lingxSubmit()">提交</a> -->
			<h1 class="mui-title" style="color:#fff;">选择流程</h1>
</header>
<div class="m-page yzui-page" style="top:45px">
	<div class="mui-scroll-wrapper">
	<div class="mui-scroll">
    
    <ul class="mui-table-view">
        <li class="mui-table-view-cell" ms-repeat="list">
            <a class="mui-navigate-right" tapmode ms-click="start(el.code)">
                {{el.name}}
            </a>
        </li>
         <li class="mui-table-view-cell" ms-if="list.length==0" style="color:#999;text-align:center;">
            暂无流程，请先添加流程！
        </li>
    </ul>
    
    
    </div></div>
</div>


<script type="text/javascript">
apiready=function(){
	$(".mui-action-back").bind("tap",function(){
		setTimeout(function(){
			api.closeWin({});
		},100);
		
	});
}

</script>
</body>

</html>