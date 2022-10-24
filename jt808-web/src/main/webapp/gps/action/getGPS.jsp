<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.*,com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
 <%
 if(session.getAttribute("SESSION_USER")==null)return;
 	ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
 	String carid=request.getParameter("carid");//
 	Map<String,Object> param=new HashMap<String,Object>();
 	param.put("apicode",1201);
 	param.put("deviceId",carid);
 		String json=ApiTools.callApi(param);
 		out.println(json);
 %>