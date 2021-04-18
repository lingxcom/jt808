<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="java.net.*,java.io.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);

Map<String,Object>success=new HashMap<String,Object>();
success.put("message","操作成功");
Map<String,Object>failure=new HashMap<String,Object>();
failure.put("message","操作失败");
String cmd=request.getParameter("c");
if("list".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select * from tlingx_wf_define where app_id=?",userBean.getApp().getId());
	success.put("list", list);
	out.println(JSON.toJSONString(success));
}else{//show create function func_name
	System.out.println("NO CMD:"+request.getParameter("c"));
	/*
	CREATE TABLE gbsmilie(id int NOT NULL AUTO_INCREMENT,primary key(id));
	*/
	out.println(JSON.toJSONString(failure));
}
%>