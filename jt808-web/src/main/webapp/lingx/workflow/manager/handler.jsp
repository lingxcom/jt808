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
//System.out.println("cmd:"+cmd);
if("getPopleTask".equals(cmd)){
	String instance_id=request.getParameter("instance_id");
	List<Map<String,Object>> list=jdbc.queryForList("select t.*,b.name user_name from tlingx_wf_instance_task t,tlingx_wf_define_task a,tlingx_user b where t.task_id=a.id and t.user_id=b.id and t.instance_id=? and a.type=3 order by create_time asc",instance_id);
	out.println(JSON.toJSONString(list));
}else if("expire".equals(cmd)){
	String sql="SELECT t.*,a.expire from tlingx_wf_instance_task t ,tlingx_wf_define_task a where t.task_id=a.id and t.`status`=2 and expire<>0 and t.stime < date_format( date_sub(current_timestamp() , interval a.expire hour),'%Y%m%d%H%i%S') ";
	List<Map<String,Object>> list=jdbc.queryForList(sql);
	out.println(JSON.toJSONString(list));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>