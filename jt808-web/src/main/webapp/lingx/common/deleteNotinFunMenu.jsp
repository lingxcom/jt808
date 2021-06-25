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
List<Map<String,Object>> list=jdbc.queryForList("select id from tlingx_func where fid<>'0' and fid not in(select id from tlingx_func)");
ret.put("NOT_IN_FUNC_SIZE:", list.size());
for(Map<String,Object> m:list){
	jdbc.update("delete from tlingx_func where id=?",m.get("id"));
}

list=jdbc.queryForList("select id from tlingx_menu where fid<>'0' and fid not in(select id from tlingx_menu)");
ret.put("NOT_IN_MENU_SIZE:", list.size());
for(Map<String,Object> m:list){
	jdbc.update("delete from tlingx_menu where id=?",m.get("id"));
}

out.print(JSON.toJSONString(ret));
%>