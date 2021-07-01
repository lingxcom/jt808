<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.support.method.executor.*,com.lingx.support.database.*,com.lingx.support.database.impl.*,com.lingx.core.engine.*,com.lingx.core.engine.impl.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
public void funcTree(List<Map<String,Object>> list,String roleid,JdbcTemplate jdbc){
	String sql="select count(*) from tlingx_rolefunc where role_id=? and func_id=?";
	for(Map<String,Object> map:list){
		map.put("checked", jdbc.queryForInt(sql,roleid,map.get("id"))>0);
		if(map.get("children")!=null){
			 funcTree((List<Map<String,Object>>)map.get("children"), roleid, jdbc);
		}
	}
}

public void roleTree(List<Map<String,Object>> list,String roleid,JdbcTemplate jdbc){
	String sql="select count(*) from tlingx_rolerole where role_id=? and refrole_id=?";
	for(Map<String,Object> map:list){
		map.put("checked", jdbc.queryForInt(sql,roleid,map.get("id"))>0);
		if(map.get("children")!=null){
			 funcTree((List<Map<String,Object>>)map.get("children"), roleid, jdbc);
		}
	}
}

public void orgTree(List<Map<String,Object>> list,String roleid,JdbcTemplate jdbc){
	String sql="select count(*) from tlingx_roleorg where role_id=? and org_id=?";
	for(Map<String,Object> map:list){
		map.put("checked", jdbc.queryForInt(sql,roleid,map.get("id"))>0);
		if(map.get("children")!=null){
			 funcTree((List<Map<String,Object>>)map.get("children"), roleid, jdbc);
		}
	}
}

public void menuTree(List<Map<String,Object>> list,String roleid,JdbcTemplate jdbc){
	String sql="select count(*) from tlingx_rolemenu where role_id=? and menu_id=?";
	for(Map<String,Object> map:list){
		map.put("checked", jdbc.queryForInt(sql,roleid,map.get("id"))>0);
		if(map.get("children")!=null){
			 funcTree((List<Map<String,Object>>)map.get("children"), roleid, jdbc);
		}
	}
}
%>
<%

UserBean user = (UserBean) request.getSession().getAttribute(
		Constants.SESSION_USER);
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
IScriptApisService apisService=applicationContext.getBean(IScriptApisService.class);
IContext context=ContextHelper.createContext(user, request.getParameterMap(), new HashMap<String,Object>(), new HashMap<String,Object>());
DefaultPerformer jsper=new DefaultPerformer(apisService.getScriptApis(),context.getRequest());
IChooseService chooser=applicationContext.getBean(IChooseService.class);

List<ICondition> clist=new ArrayList<ICondition>();
clist.add(applicationContext.getBean(RuleCondition.class));


Map<String,Object>success=new HashMap<String,Object>();
success.put("msg","操作成功");
Map<String,Object>failure=new HashMap<String,Object>();
failure.put("msg","操作失败");
String cmd=request.getParameter("c");
String roleid=request.getParameter("roleid");
if(roleid==null){
	out.println(JSON.toJSONString(failure));
	return;
}
//System.out.println("----------->"+roleid);
if("funcTree".equals(cmd)){
	String entity="tlingx_func",method="tree";
	TreeExecutor te=applicationContext.getBean(TreeExecutor.class);//new TreeExecutor();
	//te.setJdbcTemplate(jdbc);
	////te.setConditions(clist);
	Map<String,String[]>params= new HashMap<String,String[]>();
	params.put("e", new String[]{entity});
	params.put("m", new String[]{method});
	params.put("node", new String[]{request.getParameter("node")});
	
	context.setEntity(chooser.getEntity(entity));
	context.setMethod(chooser.getMethod(method, context.getEntity()));
	List<Map<String,Object>> list=(List<Map<String,Object>>)te.execute(context,jsper );
	funcTree(list,roleid,jdbc);
	out.println(JSON.toJSONString(list));
}else if("orgTree".equals(cmd)){
	String entity="tlingx_org",method="tree";
	TreeExecutor te=applicationContext.getBean(TreeExecutor.class);
	//te.setJdbcTemplate(jdbc);
	////te.setConditions(clist);
	Map<String,String[]>params= new HashMap<String,String[]>();
	params.put("e", new String[]{entity});
	params.put("m", new String[]{method});
	params.put("node", new String[]{request.getParameter("node")});
	context.setEntity(chooser.getEntity(entity));
	context.setMethod(chooser.getMethod(method, context.getEntity()));
	List<Map<String,Object>> list=(List<Map<String,Object>>)te.execute(context,jsper);
	orgTree(list,roleid,jdbc);
	out.println(JSON.toJSONString(list));
}else if("roleTree".equals(cmd)){
	String entity="tlingx_role",method="tree";
	TreeExecutor te=applicationContext.getBean(TreeExecutor.class);
	//te.setJdbcTemplate(jdbc);
	//te.setConditions(clist);
	Map<String,String[]>params= new HashMap<String,String[]>();
	params.put("e", new String[]{entity});
	params.put("m", new String[]{method});
	params.put("node", new String[]{request.getParameter("node")});
	//IContext context=ContextHelper.createContext(user,params,  new HashMap<String,Object>());
	context.setEntity(chooser.getEntity(entity));
	context.setMethod(chooser.getMethod(method, context.getEntity()));
	List<Map<String,Object>> list=(List<Map<String,Object>>)te.execute(context,jsper);
	roleTree(list,roleid,jdbc);
	out.println(JSON.toJSONString(list));
}else if("menuTree".equals(cmd)){
	String entity="tlingx_menu",method="tree";
	TreeExecutor te=applicationContext.getBean(TreeExecutor.class);
	//te.setJdbcTemplate(jdbc);
	//te.setConditions(clist);
	Map<String,String[]>params= new HashMap<String,String[]>();
	params.put("e", new String[]{entity});
	params.put("m", new String[]{method});
	params.put("node", new String[]{request.getParameter("node")});
	//IContext context=ContextHelper.createContext(user,params,  new HashMap<String,Object>());
	context.setEntity(chooser.getEntity(entity));
	context.setMethod(chooser.getMethod(method, context.getEntity()));
	List<Map<String,Object>> list=(List<Map<String,Object>>)te.execute(context,jsper);
	menuTree(list,roleid,jdbc);
	out.println(JSON.toJSONString(list));
	
}else if("trolefunc_add".equals(cmd)){
	String sql="select count(*) from tlingx_rolefunc where role_id in(select role_id from tlingx_userrole where user_id=?) and func_id=?";//
	if(jdbc.queryForInt("select count(*) from tlingx_rolefunc where role_id=? and func_id=?",roleid,request.getParameter("funcid"))==0&&jdbc.queryForInt(sql,user.getId(),request.getParameter("funcid"))>=0)
	jdbc.update("insert into tlingx_rolefunc(id,role_id,func_id)values(uuid(),?,?)",roleid,request.getParameter("funcid"));
	out.println(JSON.toJSONString(success));
}else if("trolefunc_del".equals(cmd)){
	jdbc.update("delete from tlingx_rolefunc where role_id=? and func_id=?",roleid,request.getParameter("funcid"));
	out.println(JSON.toJSONString(success));
}else if("troleorg_add".equals(cmd)){
	String sql="select count(*) from tlingx_roleorg where role_id in(select role_id from tlingx_userrole where user_id=?) and org_id=?";//
	if(jdbc.queryForInt("select count(*) from tlingx_roleorg where role_id=? and org_id=?",roleid,request.getParameter("orgid"))==0&&jdbc.queryForInt(sql,user.getId(),request.getParameter("orgid"))>=0)
	jdbc.update("insert into tlingx_roleorg(id,role_id,org_id)values(uuid(),?,?)",roleid,request.getParameter("orgid"));
	out.println(JSON.toJSONString(success));
}else if("troleorg_del".equals(cmd)){
	jdbc.update("delete from tlingx_roleorg where role_id=? and org_id=?",roleid,request.getParameter("orgid"));
	out.println(JSON.toJSONString(success));
}else if("trolemenu_add".equals(cmd)){
	String sql="select count(*) from tlingx_rolemenu where role_id in(select role_id from tlingx_userrole where user_id=?) and menu_id=?";//
	if(jdbc.queryForInt("select count(*) from tlingx_rolemenu where role_id=? and menu_id=?",roleid,request.getParameter("menuid"))==0&&jdbc.queryForInt(sql,user.getId(),request.getParameter("menuid"))>=0)
	jdbc.update("insert into tlingx_rolemenu(id,role_id,menu_id)values(uuid(),?,?)",roleid,request.getParameter("menuid"));
	out.println(JSON.toJSONString(success));
}else if("trolemenu_del".equals(cmd)){
	jdbc.update("delete from tlingx_rolemenu where role_id=? and menu_id=?",roleid,request.getParameter("menuid"));
	out.println(JSON.toJSONString(success));
}else if("trolerole_add".equals(cmd)){
	String sql="select count(*) from tlingx_rolerole where role_id in(select role_id from tlingx_userrole where user_id=?) and refrole_id=?";//
	if(jdbc.queryForInt("select count(*) from tlingx_rolerole where role_id=? and refrole_id=?",roleid,request.getParameter("refroleid"))==0&&jdbc.queryForInt(sql,user.getId(),request.getParameter("refroleid"))>=0)
	jdbc.update("insert into tlingx_rolerole(id,role_id,refrole_id)values(uuid(),?,?)",roleid,request.getParameter("refroleid"));
	out.println(JSON.toJSONString(success));
}else if("trolerole_del".equals(cmd)){
	jdbc.update("delete from tlingx_rolerole where role_id=? and refrole_id=?",roleid,request.getParameter("refroleid"));
	out.println(JSON.toJSONString(success));
}else{
	out.println(JSON.toJSONString(failure));
}
%>