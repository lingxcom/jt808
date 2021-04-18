<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String text="";
	if(request.getParameter("text")!=null)
	text=java.net.URLDecoder.decode(request.getParameter("text"),"UTF-8");
	String value=request.getParameter("value");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体查看</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="lingx/js/template/tree_combo2.js"></script>
<style type="text/css">
.label{font-size:14px;line-height:28px;text-align:center;width:200px;margin:2px;border:1px solid #99bbe8;background-color:#ffefbb;}
.remove{line-height:24px;height:24px;border:0px none;}
</style>
<script type="text/javascript">

var request_params=${REQUEST_PARAMS};
var fromPageId='${param.pageid}';
var entityCode=request_params.e;
var methodCode=request_params.m;
var entityId='${entityId }';
var params=${_params};

var text="<%=text%>";
var value="<%=value%>";

params=request_params;
removeDefaultAttribute(params);
var entityFields={};

var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
if(params&&params.fid){
	entityId=params.fid;
}
</script>
</head>
<body>
<div id="Value-DIV" style="text-align:center;">
</div>
</body>

</html>