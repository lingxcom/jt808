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
<base href="<%=basePath%>">
<title>对象编辑工具</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="lingx/workflow/define/define.js"></script>
<script type="text/javascript" src="lingx/workflow/define/workflow.js"></script>
<link rel="stylesheet" type="text/css" href="lingx/workflow/define/workflow.css">


<script type="text/javascript">


var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/define/handler.jsp";
var WF=new WorkFlow({action:handlerJsp,el:"#pad"});

function reloadGrid(){
	//Ext.getCmp("entityGrid_1").getStore().reload();
	WF.reload();
}
</script>
</head>
<body>
</body>
</html> 