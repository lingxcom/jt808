<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
%>
<%
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
IWorkflowService activitiService=applicationContext.getBean(IWorkflowService.class);
if(session.getAttribute(Constants.SESSION_USER)==null)return;
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
ret.put("success", true);
ret.put("code", 1);
ret.put("message", "操作成功");

if("getWorkflowListByDaiban".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select t.id,t.name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId,t.stime,b.name user_name,a.serial_number from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_user b where b.id=a.user_id and t.instance_id=a.id and t.user_id=? and t.status=2 and a.status<>3 order by t.stime desc",userBean.getId());
	out.println(JSON.toJSONString(list));
	
}else if("getWorkflowListByDL".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select t.id,t.name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId,c.name user_name from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_wf_define_task b,tlingx_user c where c.id=a.user_id and t.instance_id=a.id and t.task_id=b.id and t.status=1 and a.status<>3 and t.user_id regexp '"+userBean.getId()+"' order by t.create_time desc");
	out.println(JSON.toJSONString(list));
}else if("getWorkflowListByFQ".equals(cmd)){
	//List<Map<String,Object>> list=jdbc.queryForList("select t.id,t.name,a.name workflow,a.id processInstanceId from tlingx_wf_instance_task t,tlingx_wf_instance a where t.instance_id=a.id and a.user_id=? and t.status=2 order by t.stime desc",userBean.getId());
	List<Map<String,Object>> list=jdbc.queryForList("select a.id,a.create_time name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId from tlingx_wf_instance a where user_id=? and a.status<>3 order by a.create_time desc",userBean.getId());
	String sql="select t.id from tlingx_wf_instance_task t,tlingx_wf_define_task a where t.task_id=a.id and t.instance_id=? and a.type=3 order by t.create_time asc limit 1";
	for(Map<String,Object> map:list){
		try{
		map.put("id", jdbc.queryForObject(sql, String.class,map.get("processInstanceId")));

		map.put("task", jdbc.queryForObject("select name from tlingx_wf_instance_task where id =(select task_id from tlingx_wf_instance where id=?)", String.class,map.get("processInstanceId")));
		}catch(Exception e){}
	}

	out.println(JSON.toJSONString(list));
}else if("getWorkflowListByXG".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select  t.id,t.name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId,t.stime,b.name user_name,a.serial_number from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_user b where b.id=a.user_id and t.instance_id=a.id and t.user_id=? and a.user_id<>?  and t.status in(3,4) and a.status<>3 order by t.stime desc",userBean.getId(),userBean.getId());
	Set<String> sets=new HashSet<String>();
	List<Map<String,Object>> nlist=new ArrayList<Map<String,Object>>();
	for(Map<String,Object> map:list){
		String id=map.get("id").toString();
		if(!sets.contains(id)){
			sets.add(id);
			nlist.add(map);
		}
	}
	out.println(JSON.toJSONString(nlist));
}else if("getWorkflowListByCC".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select t.id,t.name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId,c.name user_name from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_wf_define_task b,tlingx_user c,tlingx_wf_instance_task d where c.id=a.user_id and t.instance_id=a.id and t.task_id=b.id and a.id=d.instance_id and a.task_id=t.id and d.status=5 and d.user_id = ? order by t.create_time desc",userBean.getId());
	out.println(JSON.toJSONString(list));
}else if("getWorkflowList".equals(cmd)){
	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	
	out.println(JSON.toJSON(list));
}else if("getWorkflowCombobox".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select id value,name text from tlingx_wf_define where status=3 and app_id=? order by name asc",userBean.getApp().getId());
	out.println(JSON.toJSON(list));
}else if("removeWorkflow".equals(cmd)){
	out.println(JSON.toJSON(ret));
	
}else if("centerInit".equals(cmd)){
	String sql="select count(*) from tlingx_wf_instance_task t,tlingx_wf_instance a where  t.instance_id=a.id and t.user_id=? and t.status=2 and a.status<>3";
	ret.put("db", jdbc.queryForInt(sql,userBean.getId()));

	sql="select count(*) from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_wf_define_task b where t.instance_id=a.id and t.task_id=b.id and t.status=1 and a.status<>3 and t.user_id regexp '"+userBean.getId()+"'  ";
	ret.put("dl", jdbc.queryForInt(sql));
	
	sql="select count(*) from tlingx_wf_instance where  user_id=? and status<>3";
	ret.put("fq", jdbc.queryForInt(sql,userBean.getId()));
	
	sql="select count(distinct a.id) from tlingx_wf_instance_task t,tlingx_wf_instance a where t.instance_id=a.id and t.user_id=? and a.user_id<>?  and t.status in(3,4) and a.status<>3";
	ret.put("xg",jdbc.queryForInt(sql,userBean.getId(),userBean.getId()));
	
	//List<Map<String,Object>> list=jdbc.queryForList("select t.id,t.name,concat(a.serial_number,'-',a.name) workflow,a.id processInstanceId,c.name user_name from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_wf_define_task b,tlingx_user c,tlingx_wf_instance_task d where c.id=a.user_id and t.instance_id=a.id and t.task_id=b.id and a.id=d.instance_id and a.task_id=t.id and d.status=5 and d.user_id = ? order by t.create_time desc",userBean.getId());
	sql="select count(*) from tlingx_wf_instance_task t,tlingx_wf_instance a,tlingx_wf_define_task b,tlingx_user c,tlingx_wf_instance_task d where c.id=a.user_id and t.instance_id=a.id and t.task_id=b.id and a.id=d.instance_id and a.task_id=t.id and d.status=5 and d.user_id = ?";
	ret.put("cc", jdbc.queryForInt(sql,userBean.getId()));
	out.println(JSON.toJSON(ret));
}else if("category".equals(cmd)){
	String sql="select id ,name from tlingx_wf_category where fid<>'0' and app_id=? order by orderindex asc";
	String sql2="select t.id,t.name,t.code,t.remark,a.code form from tlingx_wf_define t,tlingx_wf_define_form a where t.form_id=a.id and t.id in(select define_id from tlingx_wf_define_category where category_id=?) and t.status=3 and t.app_id=? order by convert(t.name using gbk) asc";
	List<Map<String,Object>> list=jdbc.queryForList(sql,userBean.getApp().getId());
	for(Map<String,Object> map:list){
		map.put("list", jdbc.queryForList(sql2,map.get("id"),userBean.getApp().getId()));
	}
	ret.put("list", list);
	out.println(JSON.toJSON(ret));
}else if("listWf".equals(cmd)){
	String sql="select  t.id,t.name,t.code,a.code form from tlingx_wf_define t,tlingx_wf_define_form a where t.form_id=a.id and t.status=3  and (t.user_ids regexp ? or t.org_ids regexp ? or t.role_ids regexp ?)";
	List<Map<String,Object>> list=jdbc.queryForList(sql,userBean.getId(),userBean.getRegexp().getRoleOrg(),userBean.getRegexp().getCurrentRole());
	ret.put("list", list);
	out.println(JSON.toJSON(ret));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>