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
if("getDefineList".equals(cmd)){
	String param=request.getParameter("param");
	String condi=" 1=1 ";
	if(param!=null){
		condi="  ( t.name like '%"+param+"%' or t.code like '%"+param+"%' ) ";
	}
	List<Map<String,Object>> list=jdbc.queryForList("select * from tlingx_wf_define t where "+condi+" order by create_time desc");
	out.println(JSON.toJSONString(list));
}else if("getDefineTaskAndLine".equals(cmd)){
	String id=request.getParameter("id");
	ret.put("tasks", jdbc.queryForList("select * from tlingx_wf_define_task where define_id=?",id));
	ret.put("lines", jdbc.queryForList("select * from tlingx_wf_define_line where define_id=?",id));
	String json=JSON.toJSONString(ret);
	out.println(json);
}else if("addTask".equals(cmd)){
	String defineId=request.getParameter("defineId");
	String top=request.getParameter("top");
	String left=request.getParameter("left");
	String width=request.getParameter("width");
	String height=request.getParameter("height");
	String type=request.getParameter("type");
	String name="",script_after="";
	switch(Integer.parseInt(type)){
	case 1:
		name="开始";
	if(jdbc.queryForInt("select count(*) from tlingx_wf_define_task where define_id=? and type=1",defineId)>0){
		ret.put("code", -1);
		ret.put("message","只能有一个开始节点");
		out.println(JSON.toJSONString(ret));
		return;
	}
	break;
	case 2:name="结束";break;
	case 3:name="手工任务";break;
	case 4:name="自动任务";break;
	case 5:name="会签任务";
	script_after="WL.addParam('_SIGN',WL.sign(_TASK_ID),_INSTANCE_ID);";
	break;
	case 6:name="子流程任务";break;
	default:
		name="未知任务";
	}
	
	String realTop=wf.round(top);
	String realLeft=wf.round(left);
	if(jdbc.queryForInt("select count(*) from tlingx_wf_define_task where define_id=? and top=? and `left`=?",defineId,realTop,realLeft)>0){
		ret.put("code", -1);
		ret.put("message","不可在同一位置创建任务");
		out.println(JSON.toJSONString(ret));
		return;
	}
	String formId=jdbc.queryForObject("select form_id from tlingx_wf_define where id=?", String.class,defineId);
	jdbc.update("insert into tlingx_wf_define_task(id,define_id,name,type,user_ids,org_ids,role_ids,form_id,create_time,modify_time,top,`left`,width,height,script_after) values(uuid(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			,defineId,name,type,"","","",formId,Utils.getTime(),Utils.getTime(),realTop,realLeft,width,height,script_after);
	out.println(JSON.toJSONString(ret));
}else if("saveTask".equals(cmd)){
	String id=request.getParameter("id");
	String top=request.getParameter("top");
	String left=request.getParameter("left");
	String width=request.getParameter("width");
	String height=request.getParameter("height");
	wf.updateLinePoint(id, wf.round(Integer.parseInt(top)), wf.round(Integer.parseInt(left)));
	jdbc.update("update tlingx_wf_define_task set top=?,`left`=?,width=?,height=? where id=?",wf.round(top),wf.round(left),width,height,id);
	out.println(JSON.toJSONString(ret));
}else if("addLine".equals(cmd)){
	String defineId=request.getParameter("defineId");
	String top1=request.getParameter("top1");
	String left1=request.getParameter("left1");
	String top2=request.getParameter("top2");
	String left2=request.getParameter("left2");
	String sourcePoint=request.getParameter("sourcePoint");
	String targetPoint=request.getParameter("targetPoint");
	String type=request.getParameter("type");
	
	String sid=jdbc.queryForObject("select id from tlingx_wf_define_task where define_id=? and top=? and `left`=?", String.class,defineId,top1,left1);
	String tid=jdbc.queryForObject("select id from tlingx_wf_define_task where define_id=? and top=? and `left`=?", String.class,defineId,top2,left2);
	if(sid.equals(tid)){
		ret.put("code", -1);
		out.println(JSON.toJSONString(ret));
		return;
	}
	if(jdbc.queryForInt("select count(*) from tlingx_wf_define_line where define_id=? and source_id=? and target_id=? ",defineId,sid,tid)>0){
		ret.put("code", -1);
		out.println(JSON.toJSONString(ret));
		return;
	}
	String id=lingx.uuid();
	jdbc.update("insert into tlingx_wf_define_line(id,define_id,source_id,target_id,content,top1,left1,top2,left2,source_point,target_point,type) values(?,?,?,?,?,?,?,?,?,?,?,?)",
			id,defineId,sid,tid,"",top1,left1,top2,left2,sourcePoint,targetPoint,type);
	wf.lineTest(id);
	out.println(JSON.toJSONString(ret));
}else if("deleteTask".equals(cmd)){
	String id=request.getParameter("id");
	jdbc.update("delete from tlingx_wf_define_task where id=?",id);
	out.println(JSON.toJSONString(ret));
}else if("deleteLine".equals(cmd)){
	String id=request.getParameter("id");
	jdbc.update("delete from tlingx_wf_define_line where id=?",id);
	out.println(JSON.toJSONString(ret));
}else if("move".equals(cmd)){
	String id=request.getParameter("id");
	String type=request.getParameter("t");
	String num=request.getParameter("num");
	if("top".equals(type)){
		jdbc.update("update tlingx_wf_define_task set top=top+? where define_id=?",num,id);
		jdbc.update("update tlingx_wf_define_line set top1=top1+?,top2=top2+? where define_id=?",num,num,id);
	}else{
		jdbc.update("update tlingx_wf_define_task set `left`=`left`+? where define_id=?",num,id);
		jdbc.update("update tlingx_wf_define_line set left1=left1+?,left2=left2+? where define_id=?",num,num,id);
	}
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>