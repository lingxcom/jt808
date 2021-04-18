<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.model.bean.UserBean,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.core.service.*" %>

<%
/* ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return; */

com.lingx.support.web.listener.OnlineUserManager onlineUserManager=com.lingx.support.web.listener.OnlineUserManager.getOnlineUserManager(session);

String id=request.getParameter("id");
if(id!=null&&!"".equals(id)){
	onlineUserManager.invalidateSession(id);
	onlineUserManager.removeSession(id);
}List<UserBean> list=onlineUserManager.getListUser();
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
var json=<%=JSON.toJSONString(list) %>;
console.log(json);
var model=avalon.define({
	$id:"body",
	list:[]
});
$(function(){
	model.list=json;
});

function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14);
}
</script>
</head>
<body ms-controller="body">
<div class="container-fluid" style="margin-top:10px;">
<div class="row">
<div class="col-md-6">
说明：该用户列表是单容器下的，多容器时只能做为参考；因为Session存在不同的服务器内存。

</div>
<div class="col-md-6" style="text-align:right;">
<a class="btn btn-success" onclick="window.location.reload();"  href="javascript:;" role="button">刷新</a>
</div>
</div>
<div class="row">
<div class="col-md-12">
<table class="table table-striped table-hover" style="margin-top:10px;">
<tr>
<th >账号</th>
<th  >姓名</th>
<th  >登录时间</th>
<th  >登录IP</th>
<th  >登录次数</th>
<th  >操作</th>
</tr>
<tr ms-repeat="list">
<td >{{el.account}}</td>
<td>{{el.name}}</td>
<td >{{ft(el.loginTime)}}</td>
<td>{{el.loginIp}}</td>
<td >{{el.loginCount}}</td>

<td>
<a  ms-href="'lingx/template/online.jsp?id='+el.remark">强制下线</a> 

</td>
</tr>
</table>
</div>
</div>
</div>
</body>
</html>