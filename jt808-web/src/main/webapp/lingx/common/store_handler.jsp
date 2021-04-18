<%@page import="java.net.URL"%>
<%@page import="com.lingx.core.utils.HttpUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

String c=request.getParameter("c");
if("tree".equals(c)){
	
	String node=request.getParameter("node");
	String ret=HttpUtils.get("http://mdd.lingx.com/bi/action/tree.jsp?node="+node);
	out.print(ret);
}else if("grid".equals(c)){
	String page1=request.getParameter("page");
	String rows=request.getParameter("limit");
	String node=request.getParameter("node");
	String ret=HttpUtils.get("http://mdd.lingx.com/bi/action/grid.jsp?node="+node+"&page="+page1+"&rows="+rows);
	out.print(ret);
}else if("inst".equals(c)){
	String no=request.getParameter("no");
	Map<String,Object> ret=new HashMap<String,Object>();
	IUpdateService update=spring.getBean(IUpdateService.class);
	String basePath=request.getServletContext().getRealPath("/");
	String ts=Utils.getTime();
	boolean b=update.update(new URL("http://mdd.lingx.com/downloadCode?no="+no), basePath,ts);
	if(b){
		ret.put("code", 1);
		ret.put("message", "安装成功");
	}else{
		ret.put("code", -1);
		ret.put("message", "安装失败");
		
	}
	out.print(JSON.toJSONString(ret));
}
%>