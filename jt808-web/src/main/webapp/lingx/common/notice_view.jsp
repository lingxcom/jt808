<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
String ts=lingx.getConfigValue("lingx.update.version.ts", "20121221152030");
request.setAttribute("ts", ts);
if(!lingx.isSuperman(request))return;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="lingx/model/update/pager.js"></script>
<SCRIPT type="text/javascript">
var targetUrl="<%="http://mdd.lingx.com/bi/action/notice_get.jsp" %>"+"?callback=?";
$(function(){
	$.getJSON(targetUrl+"&id=${param.id}",function(json){
		$("#content").val(json.notice.content);
		$("#ts").text(ft(json.notice.ts));
	});
});

function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12);
}
</SCRIPT>
</head>
<body ms-controller="body" >
<div class="container-fluid" style="margin-top:10px;">
<table border="0" cellSpacing="0" cellPadding="0" width="100%" height="100%">
<tr>
<td>
<textarea id="content" readonly="readonly" style="width:100%;height:280px;"></textarea>
</td>
</tr>

<tr>
<td align="right">
<span id="ts" style="height:30px;line-height:30px;"></span>
</td>
</tr>
</table>
</div>
</body>

</html>