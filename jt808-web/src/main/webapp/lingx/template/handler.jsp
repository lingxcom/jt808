<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.action.IRequestAware,com.lingx.core.engine.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean("jdbcTemplate",JdbcTemplate.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
String cmd=request.getParameter("c");
Map<String,Object> ret=new HashMap<String,Object>();
ret.put("success", true);
ret.put("code", 1);
ret.put("message", "操作成功");
if("save".equals(cmd)){
	String sql="insert into tlingx_search(name,json,code,user_id,create_time) values(?,?,?,?,?)";
	jdbc.update(sql,request.getParameter("name"),request.getParameter("json"),request.getParameter("code"),userBean.getId(),Utils.getTime());
	out.print(JSON.toJSONString(ret));
}else if("del".equals(cmd)){
	jdbc.update("delete from tlingx_search where id=?",request.getParameter("id"));
	out.print(JSON.toJSONString(ret));
}else if("getByUser".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select * from tlingx_search where user_id=? and code=? order by id desc",userBean.getId(),request.getParameter("code"));
	ret.put("list", list);
	out.print(JSON.toJSONString(ret));
}else if("getNoUser".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select * from tlingx_search where user_id<>? and code=? order by id desc",userBean.getId(),request.getParameter("code"));
	ret.put("list", list);
	out.print(JSON.toJSONString(ret));
}else{
	
}
%>