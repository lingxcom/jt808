<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path1 = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path1 + "/";
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
IWorkflowService wf=applicationContext.getBean(IWorkflowService.class);
ILingxService lingx=applicationContext.getBean(ILingxService.class);
IContextService contextService=applicationContext.getBean(IContextService.class);
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
}else if("tree".equals(cmd)){
	List<Map<String,Object>> tree=new ArrayList<Map<String,Object>>();//tree
	Map<String,Object> root=new LinkedHashMap<String,Object>();//根节点
	List<Map<String,Object>> children=new ArrayList<Map<String,Object>>();//children
	root.put("text", "text");
	root.put("expanded", false);
	root.put("children", children);
	
	Map<String,Object> node=new LinkedHashMap<String,Object>();
	node.put("text", "text1");
	children.add(node);
	
	node=new LinkedHashMap<String,Object>();
	node.put("text", "text2");
	children.add(node);
	
	tree.add(root);
	String json=JSON.toJSONString(tree);
	out.println(json);
}else if("upload".equals(cmd)){
	String id=request.getParameter("id");
	String path=request.getParameter("path");
	String name=request.getParameter("name");
	String length=request.getParameter("length");
	String time=Utils.getTime();
	String sql="insert into tlingx_wf_instance_attachment(id,instance_id,name,path,user_id,create_time,modify_time,length) values(uuid(),?,?,?,?,?,?,?)";
	jdbc.update(sql,id,name,path,userBean.getId(),time,time,length);
	out.println(JSON.toJSONString(ret));
}else if("setApprover".equals(cmd)){
	String taskId=request.getParameter("taskId");
	String userId=request.getParameter("userId");
	wf.quickSetApprover(taskId, userId,contextService.getContext(request));
	out.println(JSON.toJSONString(ret));
}else if("saveComment".equals(cmd)){
	String content=request.getParameter("content");
	String taskId=request.getParameter("_TASK_ID");
	wf.quickSaveComment(taskId, content, userBean.getId());
	response.sendRedirect(basePath+"w?m=form&is_mobile="+request.getParameter("is_mobile")+"&_TASK_ID="+taskId);
}else if("getUsersByOrgId".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select id value,name text from tlingx_user where id in(select user_id from tlingx_userorg where org_id=?) order by orderindex asc",request.getParameter("orgid"));
	ret.put("list",list);
	out.print(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>