<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript">
$(function(){
	Lingx.getRootWindow().resizeWindow({height:$("body").height()+Lingx.PANEL_HEIGHT+60+20});
});
</script>
</head>

<body>
<div style="margin:10px;color:red;font-size:16px;width:100%;text-align:left;">
 <%=i18n.text("操作失败，权限不足！如需开通，请联系系统管理员。",session)%>
</div>
</body>
</html>