<%@ page import="java.net.*,java.io.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String e=request.getParameter("e");
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<SCRIPT type="text/javascript">
var code="<%=e%>";
var handlerUrl="<%=basePath %>sys/sysentity/handler.jsp";
$(function(){
	autoResizeDialog();
	$.getJSON(handlerUrl+"?c=getEntity&code="+code,function(json){
		json=json[0];
		$("#code").html(json.Code);
		$("#name").html(json.Name);
		$("#version").html(json.Version);
		//$("#code").html(json.Code);
	});
});
</SCRIPT>
</head>
<body>
<form id="form" action="<%=basePath %>sys/sysentity/synchro/step2.jsp" method="post">
<TABLE id="table-form" width="100%" height="100%" border="0" cellSpacing="0" cellPadding="0" ><!--  style="border:1px solid red;" -->
<TR>
<TD class="title" id="vaildMessage"></TD>
</TR><TR>
<TD class="middle" >

<div id="id_hidden" style="display:none;width:0px;height:0px;">
<input name="code" value="<%=e%>" type="hidden"/>
</div>
<table id="table" width="100%" style="margin:0px;padding:0px;" border="0" cellpadding="0" cellspacing="0">
<TR><TD width="10">&nbsp;</TD><TD align="right">对象代码：</TD><TD id="code"></TD></TR>
<TR><TD width="10">&nbsp;</TD><TD align="right">对象名称：</TD><TD id="name"></TD></TR>
<TR><TD width="10">&nbsp;</TD><TD align="right">对象版本：</TD><TD id="version"></TD></TR>
<TR><TD width="10">&nbsp;</TD><TD align="right">上传说明：</TD><TD><TEXTAREA id="remark" name="remark" style="width:300px;height:100px;"></TEXTAREA> </TD></TR>
</table>

</TD>
</TR><TR>
<TD class="bottom">
<input  class="btn" type="submit" value="确定" />
<input  class="btn" type="button" value="取消"  onclick="closeWin();"/>
</TD>
</TR><TR>
</TR>
</TABLE>
</form>
</body>
</html>