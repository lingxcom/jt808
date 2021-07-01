<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.action.IRequestAware,com.lingx.core.engine.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
IContextService contextService=spring.getBean(IContextService.class);
com.lingx.core.action.IActionExecutor action=spring.getBean("uploadActionExecutor", com.lingx.core.action.IActionExecutor.class);
IContext context=ContextHelper.createContext(contextService.getUserBean(request)
		, contextService.getRequestParameters(request),contextService.getRequestAttributes(request)
		,contextService.getSesssionAttributes(request.getSession()));

IRequestAware ra=(IRequestAware)action.getAction();
ra.setRequest(request);
action.execute(context);
response.getWriter().print(context.getRequest().getAttribute(Constants.REQUEST_JSON));
%>