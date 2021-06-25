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
var no='';
$(function(){
	$.getJSON("http://mdd.lingx.com/bi/action/buy.jsp?callback=?&id=${param.id}",function(json){
		//console.log(json);
		
		no=json.no;
		if("none"!=json.payCode){
		var url="http://tool.oschina.net/action/qrcode/generate?data="+json.payCode+"&output=image%2Fpng&error=L&type=5&margin=10&size=4&1538981680504";
		var div=$("#div");
		div.empty();
		div.append("<img src='"+url+"'/>");
		}else{
			clearInterval(ip1);
			alert("无需支付");
			window.location.href="lingx/common/store_inst.jsp?no="+no;
		}
	});

});
function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14);
}
function check(){
	if(no){
		$.getJSON("http://mdd.lingx.com/bi/action/check.jsp?callback=?&no="+no,function(json){
			if(json.status==2){
				clearInterval(ip1);
				alert("支付成功");
				window.location.href="lingx/common/store_inst.jsp?no="+no;
			}
		});
	}
}


var ip1=setInterval("check()",1000);

</script>
</head>
<body style="background:#dfe9f6;">
<table width="100%">
<tr>
<td>&nbsp;<br>&nbsp;</td>
</tr>
<tr>
<td align="center">

<div id="div" style="font-size:14px;"><h2>正在下单中，请稍候...</h2> </div>

</td>
</tr>
<tr>
<td align="center">&nbsp;<br>微信支付，打开微信扫一扫；付款后自动安装。</td>
</tr>
</table>
</body>
</html>