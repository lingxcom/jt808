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
<link href="js/mui/css/mui.min.css" rel="stylesheet">
<script type="text/javascript" src="js/mui/js/mui.min.js"></script>
<script type="text/javascript">
function lingxSubmit(){
	if(!$("#content").val()){
		alert("评论内容不可为空！");
		return;
	}
	$("form").submit();
}
</script>
</head>
<body>
<header id="default_header" class="mui-bar mui-bar-nav" style="background-color:#5b8dca;">
			<a class="mui-action-back mui-btn mui-btn-link mui-pull-left" style="color:#fff;">取消</a>
			<a id="submitBtn" class="mui-btn mui-btn-link mui-pull-right" style="color:#fff;" onclick="lingxSubmit()">提交</a>
			<h1 class="mui-title" style="color:#fff;">意见反馈</h1>
</header>

<form id="form" action="lingx/workflow/page/handler.jsp" method="post">
<div style="dispaly:none;">
<input name="c" type="hidden" value="saveComment"  />
<input name="is_mobile" type="hidden" value="true"  />
<input type="hidden" name="_TASK_ID" value="${param._TASK_ID }"/><!-- 当前办理的任务ID -->
</div>
<div style="width:100%;height:45px;">&nbsp;</div>

		<div class="mui-content" id="7jziirua7A">
			<div class="mui-content-padded" style="margin: 5px;" id="Gj5mbbfpba">
				<div class="mui-input-row" style="margin: 10px 5px;" id="Pjx41Iz3nQ">
					<textarea id="content" rows="5" placeholder="评论内容" id="mpkMQaArOl" name="content"></textarea>
				</div>
			</div>
		</div>
	</form>
</body>

</html>