<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
IConfigService configService=spring.getBean(IConfigService.class);
IModelService modelService=spring.getBean(IModelService.class);
IPackageService packageService=spring.getBean(IPackageService.class);
if(!lingx.isSuperman(request))return;
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);

Map<String,Object> ret=new HashMap<String,Object>();
ret.put("code", 1);
ret.put("message","操作成功");
if("resetSecret".equals(cmd)){
	String secret=lingx.uuid();
	configService.saveValue("lingx.update.secret",secret);
	configService.reset();
	ret.put("secret",secret);
	out.println(JSON.toJSONString(ret));
}else if("saveData".equals(cmd)){
	String url=request.getParameter("url");
	String secret=request.getParameter("secret");
	String text=request.getParameter("text");
	String value=request.getParameter("value");

	configService.saveValue("lingx.update.submit.url",url);
	configService.saveValue("lingx.update.submit.secret",secret);
	configService.saveValue("lingx.update.submit.text",text);
	configService.saveValue("lingx.update.submit.value",value);
	configService.reset();
	
	out.println(JSON.toJSONString(ret));
}else if("submitData".equals(cmd)){
	String url=request.getParameter("url");
	String secret=request.getParameter("secret");
	String text=request.getParameter("text");
	String value=request.getParameter("value");

	configService.saveValue("lingx.update.submit.url",url);
	configService.saveValue("lingx.update.submit.secret",secret);
	configService.saveValue("lingx.update.submit.text",text);
	configService.saveValue("lingx.update.submit.value",value);
	configService.reset();
	
	String temp=packageService.packAndSubmit(url,secret,value,request.getRealPath("/"));
	ret.put("message",temp);
	out.println(JSON.toJSONString(ret));
}else if("submitDataOne".equals(cmd)){
	
	String value=request.getParameter("value");
	String url=lingx.getConfigValue("lingx.update.submit.url","");
	String secret=lingx.getConfigValue("lingx.update.submit.secret","");
	if("".equals(url)||"none".equals(url)){
		ret.put("message","操作失败，请先设置远程地址！");
	}else{
		String temp=packageService.packAndSubmit(url,secret,value,request.getRealPath("/"));
		ret.put("message",temp);
	}
	
	out.println(JSON.toJSONString(ret));
}else if("submitFMO".equals(cmd)){
	
	String type=request.getParameter("type");
	String url=lingx.getConfigValue("lingx.update.submit.url","");
	String secret=lingx.getConfigValue("lingx.update.submit.secret","");
	if("".equals(url)||"none".equals(url)){
		ret.put("message","操作失败，请先设置远程地址！");
	}else{
		String temp=packageService.packAndSubmitFMO(url,secret,Integer.parseInt(type),request.getRealPath("/"));
		ret.put("message",temp);
	}
	
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值有误,editor/submit_handler.jsp");
}
%>