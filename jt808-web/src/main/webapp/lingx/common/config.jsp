<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.gps.service.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";


ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
Map<String,Object> map=jdbc.queryForMap("select * from tlingx_app limit 1");
request.setAttribute("app", map);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript">
var handlerJsp="lingx/common/configHandler.jsp";

function saveAppName(){
	var name=$("#appname").val();
	$.post(handlerJsp,{c:"saveAppName",id:"${app.id}",name:name},function(json){
		lgxInfo(json.message);
	},"json");
}

function saveIndexpage(){
	var indexpage=$("#indexpage").val();
	$.post(handlerJsp,{c:"saveIndexpage",id:"${app.id}",indexpage:indexpage},function(json){
		lgxInfo(json.message);
	},"json");
}
function saveAppLogo(){
	var logo=$("#logo").val();
	$.post(handlerJsp,{c:"saveLogo",id:"${app.id}",logo:logo},function(json){
		lgxInfo(json.message);
	},"json");
}
function saveConfig(key,el){
	$.post(handlerJsp,{c:"lingxConfig",paramName:key,paramValue:el.checked},function(json){
		lgxInfo(json.message);
	},"json");
}


function saveConfig2(key,elID){
	$.post(handlerJsp,{c:"lingxConfig",paramName:key,paramValue:$("#"+elID).val()},function(json){
		lgxInfo(json.message);
	},"json");
}
</script>
<style type="text/css">

.ipt{
width:320px;
}
</style>
</head>
<body ms-controller="body">
<div class="container-fluid" style="margin-top:10px;">

<div class="row">
<div class="col-md-12">
<table class="table table-striped table-hover" style="margin-top:10px;">
<tr>
<th>名称</th>
<th>值</th>
<th>备注</th>
</tr>
<!-- -->
<tr>
<td>1.系统名称: </td>
<td><input class="ipt" id="appname" value="${app.name }"/> <input type="button" onclick="saveAppName()" value="保存"> </td>
<td>用来显示头部的系统名称</td>
</tr>
<!-- -->
<tr>
<td>1.系统Logo: </td>
<td><input class="ipt" id="logo" value="${app.logo }"/> <input type="button" onclick="saveAppLogo()" value="保存"> </td>
<td>用来显示头部的Logo图标</td>
</tr>
<!-- -->
<tr>
<td>2.系统默认页: </td>
<td><input class="ipt" id="indexpage" value="${app.indexpage }"/> <input type="button" onclick="saveIndexpage()" value="保存"> </td>
<td>登陆成功后默认显示的界面</td>
</tr>

<!-- -->
<tr>
<td>3.登录验证码: </td>
<td><input type="checkbox" onchange="saveConfig('lingx.login.verifycode',this)" <%="true".equals(lingx.getConfigValue("lingx.login.verifycode", "true"))?"checked":"" %>/></td>
<td>登陆的验证功能</td>
</tr>
<!--  -->
<tr>
<td>4.弱密码保护 </td>
<td><input type="checkbox" onchange="saveConfig('lingx.login.password.zero6',this)" <%="true".equals(lingx.getConfigValue("lingx.login.password.zero6", "false"))?"checked":"" %>/></td>
<td>密码为6个0时，强制要求重置</td>
</tr>
<!--  -->
<tr>
<td>5.防止暴力破解密码</td>
<td><input type="checkbox" onchange="saveConfig('lingx.login.islock',this)" <%="true".equals(lingx.getConfigValue("lingx.login.islock", "false"))?"checked":"" %>/></td>
<td>密码错误输入次数过多时自动锁定账号5分钟 </td>
</tr>
<!--  -->
<tr>
<td>6.密码错误输入次数: </td>
<td><input class="ipt" id="islock_max" value="<%=lingx.getConfigValue("lingx.login.lock.max", 10)%>"/> <input type="button" onclick="saveConfig2('lingx.login.lock.max','islock_max')" value="保存"> </td>
<td>第5点为true时生效</td>
</tr>
<!--  -->
<tr>
<td>7.TOKEN登陆 </td>
<td><input type="checkbox" onchange="saveConfig('lingx.login.user.token',this)" <%="true".equals(lingx.getConfigValue("lingx.login.user.token", "false"))?"checked":"" %>/></td>
<td>通过TOKEN单点登录,URL参数名lingx_user_token对应tlingx_user.token</td>
</tr>
<!--  -->

</table>
</div>
</div>
</div>
</body>
</html>