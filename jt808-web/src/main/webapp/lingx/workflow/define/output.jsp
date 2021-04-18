<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %><%
	ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
    IWorkflowService workflowService=applicationContext.getBean(IWorkflowService.class);
    String temp=workflowService.output(request.getParameter("id"), true);
    response.setCharacterEncoding("utf-8");  
    response.setHeader("Content-Disposition","attachment; filename=LINGX_WF_"+request.getParameter("code")+".lwf");  
    //获取响应报文输出流对象  
    try{
	    java.io.PrintWriter  out1 =response.getWriter();
	    out1.print(temp);
	    out1.flush();
	    out1.close();
    }catch(Exception e){
    	e.printStackTrace();
    }
%>