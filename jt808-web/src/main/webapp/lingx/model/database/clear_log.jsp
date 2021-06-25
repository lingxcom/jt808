<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
JdbcTemplate jdbc=spring.getBean("jdbcTemplate",JdbcTemplate.class);
if(!lingx.isSuperman(request))return;

jdbc.update("delete from tlingx_login_log");
jdbc.update("delete from tlingx_operate_log");
jdbc.update("delete from tlingx_schedule_log");
jdbc.update("delete from tlingx_file");
jdbc.update("delete from tlingx_update");
out.println("清理成功");
%>