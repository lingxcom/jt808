<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.gps.service.*" %>
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
<title>实体列表</title>
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container-fluid" style="margin-top:10px;">
<div class="panel panel-info" style="margin-top:20px;">
  <div class="panel-heading">
    <h3 class="panel-title">欢迎使用灵犀开发平台</h3>
  </div>
  <div class="panel-body" style="line-height:30px;font-size:14px;">
技术文档：<a href="http://docs.lingx.com" target="_blank">http://docs.lingx.com</a> <br>
视频教程：<a href="https://space.bilibili.com/482658194" target="_blank">https://space.bilibili.com/482658194</a> <br>
源码地址：<a href="https://github.com/lingxcom/lingx" target="_blank">https://github.com/lingxcom/lingx</a> <br>
    <br>
  </div>
</div>
</div>
</body>
</html>