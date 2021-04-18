<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
IWorkflowService wf=applicationContext.getBean(IWorkflowService.class);
ILingxService lingx=applicationContext.getBean(ILingxService.class);
//IActivitiService activitiService=applicationContext.getBean(IActivitiService.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
ret.put("success", true);
ret.put("code", 1);
ret.put("message", "操作成功");
if("put".equals(cmd)){
	String json=request.getParameter("json");
	Map<String,Object> map=(Map<String,Object>)JSON.parseObject(json);
	List<Map<String,Object>> items=(List<Map<String,Object>>)map.get("items");
	if(lingx.queryForInt("select count(*) from tlingx_option where id=?",map.get("id"))==0){
		jdbc.update("insert into tlingx_option(id,name,code,app_id,create_time) values(?,?,?,?,?)",
			map.get("id"),map.get("name"),map.get("code"),map.get("app_id"),map.get("create_time"));
	}
	for(Map<String,Object> item:items){
		if(lingx.queryForInt("select count(*) from tlingx_optionitem where id=?",item.get("id"))==1){
			jdbc.update("update tlingx_optionitem set name=?,value=?,orderindex=?,enabled=? where id=?",
					item.get("name"),item.get("value"),item.get("option_id"),item.get("orderindex"),item.get("enabled"),item.get("id"));
		}else{
			jdbc.update("insert into tlingx_optionitem(id,name,value,option_id,orderindex,enabled) values(?,?,?,?,?,?)",
					item.get("id"),item.get("name"),item.get("value"),item.get("option_id"),item.get("orderindex"),item.get("enabled"));
		}
		
	}
	out.println(JSON.toJSONString(ret));
	//tree
}else{
	System.out.println("参数c的值["+cmd+"]有误,"+request.getServletPath());
}
%>