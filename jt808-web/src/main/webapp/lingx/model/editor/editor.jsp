<%@page import="com.lingx.core.Constants,com.lingx.core.service.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>模型编辑工具</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
</head>
<script type="text/javascript" src="lingx/model/editor/editor.js?12345678"></script>
<!-- 统计日均使用人数 font: 300 13px/21px 'Open Sans', 'Helvetica Neue', helvetica, arial, verdana, sans-serif; -->
<script type="text/javascript" src="http://mdd.lingx.com/lingx_count.jsp"></script>
<style>
.x-form-text-default {
    color: #333;
    padding: 5px 10px 4px;
    background-color: #fafafa;
    min-height: 30px;
    font-size:16px;
    line-height:32px;
    font-family:  "Microsoft YaHei","Open Sans", "Helvetica Neue", helvetica, arial, verdana, sans-serif ;
}
</style>
<script type="text/javascript">
var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/model/editor/handler.jsp";
</script>
<body>
<div style="display:none;">
<div id="API" style="overflow:auto;height:100%;font-weight:bold;font-size:14px;line-height:28px;padding-left:10px;">
CUser 当前用户<br/>
JDBC 数据库操作<br/>
LINGX 综合API<br/>
LANGUAGE 多语言类<br/>
REQUEST 客户端请求<br/>
ENTITY_CODE 当前的实体代码<br/>
<a href="js/icon_summary.png" target="_blank">ICON图标</a>
</div>
</div>
</body>
</html> 