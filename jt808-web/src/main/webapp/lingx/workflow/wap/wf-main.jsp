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
		cs:0,
		sp:0,
		start:function(code){
			window.location.href="w?d="+code+"&is_mobile=true";
		},
		view:function(taskId){
			window.location.href="w?m=form&is_mobile=true&_TASK_ID="+taskId;
		}
	});
	$(function(){
		$.post(handlerJspWf,{c:"centerInit"},function(json){
			model.cs=json.cc;
			model.sp=json.db;
			//lgxInfo(json.dl);
		},"json");
		$.post(handlerJspWf,{c:"getWorkflowListByFQ"},function(json){
			model.list=json;
		},"json");
	});
});
</script>
</head>
<body ms-controller="body">
<header id="default_header" class="mui-bar mui-bar-nav" style="background-color:#5b8dca;">
			<a class="mui-action-back1 mui-btn mui-btn-link mui-pull-left" style="color:#fff;">后退</a>
			<a id="submitBtn" class="mui-btn mui-btn-link mui-pull-right" style="color:#fff;" href="lingx/workflow/wap/wf-approvaled-list.jsp">历史</a>
			<h1 class="mui-title" style="color:#fff;">流程中心</h1>
</header>
<div class="m-page yzui-page" style="top:45px;">
	<div class="mui-scroll-wrapper">
	<div class="mui-scroll">
    
    <ul class="mui-table-view m-table-view" style="margin-top:0;">
        <li class="mui-table-view-cell m-table-view-cell">
            <a class="mui-navigate-right" tapmode href="lingx/workflow/wap/wf-approval-list.jsp">
            <div class="mui-table f-flex-wrap">
            	<div class="mui-table-cell" style="position:relative; width:32px; vertical-align:middle;">
                    <span class="mui-icon mui-icon-list" style=" position:absolute; top:2px; color:#5a5d5f;"></span>
                </div>
                <div class="mui-table-cell f-flex-con">
                    需要我处理的
                </div>
            </div>
            <span class="mui-badge mui-badge-danger" ms-if="sp>0">{{sp}}</span>
            </a>
        </li>
        <li class="mui-table-view-cell m-table-view-cell">
            <a class="mui-navigate-right" tapmode href="lingx/workflow/wap/wf-chaosong-list.jsp">
            <div class="mui-table f-flex-wrap">
            	<div class="mui-table-cell" style="position:relative; width:32px; vertical-align:middle;">
                    <span class="mui-icon mui-icon-settings" style=" position:absolute; top:2px; color:#5a5d5f;"></span>
                </div>
                <div class="mui-table-cell f-flex-con">
                    抄送给我的
                </div>
            </div>
            <span class="mui-badge mui-badge-danger" ms-if="cs>0">{{cs}}</span>
            </a>
        </li>
    </ul>
    <!--
    	排序规则
        1、未完成状态排前面，已完成排后面
        2、再联合时间进行倒序排序
    -->
    <ul class="mui-table-view m-table-view" style="margin-top:-1px;">
    	<li class="mui-table-view-divider m-table-view-divider">我申请的</li>
        <li class="mui-table-view-cell m-table-view-cell" ms-repeat="list" ms-click="view(el.id)">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-12">
                    <h4 class="mui-ellipsis">{{el.workflow}}</h4>
                    <p class="mui-h6 fs14 mui-ellipsis fc-yellow">状态：{{el.task}}</p>
                </div>
            </div>
        </li>
        <li class="mui-table-view-cell mui-indexed-list-item" ms-if="list.length==0" style="text-align:center;">
            	无数据
        </li>
    </ul>
    
    </div></div>
</div>


<script type="text/javascript">
apiready=function(){
	$(".mui-action-back1").bind("tap",function(){
		api.closeWin({});
	});
	
	api.addEventListener({
		name : 'keyback'
	}, function(ret, err) {
		api.closeWin({});
	});

}

</script>
</body>

</html>