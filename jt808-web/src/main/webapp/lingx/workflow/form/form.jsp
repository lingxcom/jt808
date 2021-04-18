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
<script type="text/javascript" src="lingx/workflow/form/form.js"></script>
<script type="text/javascript" src="lingx/js/lingx-editor.js"></script>
<link rel="stylesheet" type="text/css" href="lingx/js/resources/css/lingx-editor.css">

<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>

<link href="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>

<style type="text/css">
.redBorder{
color:green;
border:1px solid red;
}
.lingx-wf{
color:red;
-webkit-user-select:none;
-moz-user-select:none;
-ms-user-select:none;
user-select:none;
}

</style>

<script type="text/javascript">

var LE=null;
var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/form/handler.jsp";
var formId=0;
var inputId='';
function reloadGrid(){
	Ext.getCmp("entityGrid_1").getStore().reload();
	//WF.reload();
}

function init(){
	

	 $('.form_datetime').datetimepicker({
		 	language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
	        showMeridian: 1
	    });
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		$('.form_time').datetimepicker({
			language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 1,
			minView: 0,
			maxView: 1,
			forceParse: 0
	    });
}
</script>
</head>
<body>
</body>
</html> 