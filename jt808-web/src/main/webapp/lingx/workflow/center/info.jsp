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

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="lingx/workflow/define/workflow.js"></script>
<link rel="stylesheet" type="text/css" href="lingx/workflow/define/workflow.css">
<style type="text/css">
</style>
<script type="text/javascript">
var processInstanceId="${param.processInstanceId }";

var WF=new WorkFlow({action:"",el:"#IMG"});

Lingx.post("w",{m:"get",_INSTANCE_ID:processInstanceId},function(json){
	//WF
	//console.log(json);
	WF.reloadJSON(json.define);
	$.each(json.define.tasks,function(ind,obj){
		//alert(json.currentTaskId+":"+obj.id);
		if(json.currentTaskId.indexOf(obj.id)>-1){
			WF.currentTask(obj.top, obj.left, obj.width, obj.height);
		}
		
	});
});
</script>
</head>
<body>
<div id="IMG">

</div>
</body>

</html>