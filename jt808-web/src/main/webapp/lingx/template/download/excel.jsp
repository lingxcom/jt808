<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %><%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
/* 	java.util.Enumeration<String> keys=request.getParameterNames();
	while(keys.hasMoreElements()){
		String name=keys.nextElement();
		System.out.println(name+":"+request.getParameter(name));
	} */
	String entityCode=request.getParameter("e");
	//
	
		//String sql=obj.toString();
		
		org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		IReportService report=spring.getBean(IReportService.class);
		ILingxService lingx=spring.getBean(ILingxService.class);
		String sql=report.getSqlBySession(entityCode, request.getSession());
		int index=sql.toLowerCase().lastIndexOf(" limit ");
		if(index!=-1){
			sql=sql.substring(0,index);
		}
		
		//System.out.println(sql);
		if(!"".equals(sql)){
			sql+=" limit "+lingx.getConfigValue("lingx.grid.excel.limit", "1000") ;
			response.setContentType("application/vnd.ms-excel;charset=utf-8"); 
			response.addHeader("Content-Disposition", "attachment;filename="+Utils.getTime()+".xlsx");  
			report.createExcelBySQL(sql, entityCode,request).write(response.getOutputStream());
		}else{
			out.println("<span style='color:red;'>参数错误，导出失败</span>");
		}
		
%>