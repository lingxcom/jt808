<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="lingx/js/template/grid_cascade.js"></script>
<script type="text/javascript">

var request_params=${REQUEST_PARAMS};
var fromPageId='${param.pageid}';
var entityCode=request_params.e;
var methodCode='grid';
var entityId='${entityId }';
var params=${_params};
params=request_params;
removeDefaultAttribute(params);
var grid_cascade=${grid_cascade};

var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
var extParams={};
var extparam='${extparam}';
var fieldNames=["<%=i18n.text("查询",session)%>","<%=i18n.text("高级",session)%>"];
</script>
</head>

<body >

</body>

</html>