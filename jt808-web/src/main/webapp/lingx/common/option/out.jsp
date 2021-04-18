<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
String id=request.getParameter("id");
Map<String,Object> map=jdbc.queryForMap("select * from tlingx_option where id=?",id);
List<Map<String,Object>> items=jdbc.queryForList("select * from tlingx_optionitem where option_id=?",id);
map.put("items", items);
String json=JSON.toJSONString(map);
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

</SCRIPT>
</head>
<body ms-controller="body" >
<div class="container-fluid" style="margin-top:10px;">
<table border="0" cellSpacing="0" cellPadding="0" width="100%" height="100%">
<tr>
<td>
<textarea id="content" readonly="readonly" style="width:100%;height:280px;"><%=json %></textarea>
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