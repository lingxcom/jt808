<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
String cmd=request.getParameter("c");
ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
com.lingx.core.service.IConfigService lingxConfig=spring.getBean(com.lingx.core.service.IConfigService.class);
UserBean userBean=null;
if(session.getAttribute(Constants.SESSION_USER)==null)return;
userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
ret.put("code", 1);
ret.put("message", "SUCCESS");
if("saveAppName".equals(cmd)){
	String id=request.getParameter("id");
	String name=request.getParameter("name");
	jdbc.update("update tlingx_app set name=? where id=?",name,id);
	out.println(JSON.toJSONString(ret));
}else if("saveIndexpage".equals(cmd)){
	String id=request.getParameter("id");
	String indexpage=request.getParameter("indexpage");
	jdbc.update("update tlingx_app set indexpage=? where id=?",indexpage,id);
	out.println(JSON.toJSONString(ret));
}else if("saveLogo".equals(cmd)){
	String id=request.getParameter("id");
	String logo=request.getParameter("logo");
	jdbc.update("update tlingx_app set logo=? where id=?",logo,id);
	out.println(JSON.toJSONString(ret));
}else if("lingxConfig".equals(cmd)){
	String paramName=request.getParameter("paramName");
	String paramValue=request.getParameter("paramValue");
	jdbc.update("update tlingx_config set config_value=? where config_key=?",paramValue,paramName);
	lingxConfig.reset();
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值有误:"+cmd+","+request.getServletPath());//sjtc_text
}
%>