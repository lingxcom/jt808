<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.model.bean.UserBean,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.core.service.*" %>

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
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript">
var no='${param.no}';
$(function(){
	$.post("lingx/common/store_handler.jsp",{c:"inst",no:no},function(json){
		alert(json.message);
		closeWindow();
	},"json");
});
function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14);
}

</script>
</head>
<body style="background:#dfe9f6;">
<table width="100%">
<tr>
<td>&nbsp;<br>&nbsp;</td>
</tr>
<tr>
<td align="center">

<div id="div" style="background:#dfe9f6;" ><img style="background:#dfe9f6;" width="240" src="lingx/common/timg.gif"></div>

</td>
</tr>
<tr>
<td align="center">&nbsp;<br>自动安装中，不要关闭。</td>
</tr>
</table>
</body>
</html>