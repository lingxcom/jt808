<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
IWorkflowService wf=applicationContext.getBean(IWorkflowService.class);
//IActivitiService activitiService=applicationContext.getBean(IActivitiService.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
ret.put("success", true);
ret.put("code", 1);
ret.put("message", "操作成功");

//System.out.println("cmd:"+cmd);
if("getFormList".equals(cmd)){
	String param=request.getParameter("param");
	param=param==null?" 1=1 ":"  (name like '%"+param+"%' or code like '%"+param+"%')";
	List<Map<String,Object>> list=jdbc.queryForList("select * from tlingx_wf_define_form where  "+param+" order by create_time desc");
	out.println(JSON.toJSONString(list));
}else if("saveForm".equals(cmd)){
	String id=request.getParameter("id");
	String content=request.getParameter("content");
	try{
		jdbc.update("update tlingx_wf_define_form set content=? where id=?",content,id);
		ret.put("message", "保存成功");
	}catch(Exception e){
		e.printStackTrace();
		ret.put("code", -1);
		ret.put("message", "保存失败");
	}
	out.println(JSON.toJSONString(ret));
}else if("getForm".equals(cmd)){
	String id=request.getParameter("id");
	String content=jdbc.queryForObject("select content from tlingx_wf_define_form where id=?", String.class,id);
	ret.put("content",content);
	out.println(JSON.toJSONString(ret));
}else if("saveForm2".equals(cmd)){
	String id=request.getParameter("id");
	String content=request.getParameter("content");
	try{
		jdbc.update("update tlingx_wf_define_form set content2=? where id=?",content,id);
		ret.put("message", "保存成功");
	}catch(Exception e){
		e.printStackTrace();
		ret.put("code", -1);
		ret.put("message", "保存失败");
	}
	out.println(JSON.toJSONString(ret));
}else if("getForm2".equals(cmd)){
	String id=request.getParameter("id");
	String content=jdbc.queryForObject("select content2 from tlingx_wf_define_form where id=?", String.class,id);
	ret.put("content",content);
	out.println(JSON.toJSONString(ret));
}else if("saveForm3".equals(cmd)){
	String id=request.getParameter("id");
	String content=request.getParameter("content");
	try{
		jdbc.update("update tlingx_wf_define_form set content3=? where id=?",content,id);
		ret.put("message", "保存成功");
	}catch(Exception e){
		e.printStackTrace();
		ret.put("code", -1);
		ret.put("message", "保存失败");
	}
	out.println(JSON.toJSONString(ret));
}else if("getForm3".equals(cmd)){
	String id=request.getParameter("id");
	String content=jdbc.queryForObject("select content3 from tlingx_wf_define_form where id=?", String.class,id);
	ret.put("content",content);
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>