<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  <%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
	String id=jdbc.queryForObject("select id from tlingx_wf_define where code=?", String.class,request.getParameter("code"));
	request.setAttribute("id", id);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>流程图</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="lingx/workflow/define/workflow.js"></script>
<link rel="stylesheet" type="text/css" href="lingx/workflow/define/workflow.css">

<style type="text/css">
#pad{
background-color:#fff;
width:100%;
height:800px;
}
</style>
<script type="text/javascript">


var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/define/handler.jsp";
var WF=new WorkFlow({action:handlerJsp,el:"#pad"});
$(function(){
	WF.setDefineId("${id}");
	WF.reload();
});
function reloadGrid(){
	//Ext.getCmp("entityGrid_1").getStore().reload();
	WF.reload();
}
</script>
</head>
<body>
<div id="pad">

</div>
</body>
</html> 