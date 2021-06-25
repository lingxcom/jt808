<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
IDatabaseService databaseService=spring.getBean(IDatabaseService.class);
if(!lingx.isSuperman(request))return;

String databaseName=databaseService.getDatabaseName();

ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);

Map<String,Object>success=new HashMap<String,Object>();
success.put("msg","操作成功");
Map<String,Object>failure=new HashMap<String,Object>();
failure.put("msg","操作失败");
String cmd=request.getParameter("c");
if("getDbTree".equals(cmd)){
	List<Map<String,Object>> root=new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> tables=jdbc.queryForList("SELECT table_name from information_schema.TABLES where table_type='BASE TABLE' and table_schema=? order by table_name asc",new  Object[]{databaseName});
	for(Map<String,Object> map:tables){
		Object key=map.keySet().toArray()[0];
		map.put("text",map.get(key));
		map.put("id",map.get(key));
		map.put("leaf",true);
	}
	Map<String,Object> tableNode=new HashMap<String,Object>();
	tableNode.put("children",tables);
	tableNode.put("text","数据表");
	tableNode.put("id","table");
	tableNode.put("expanded",true);
	
	List<Map<String,Object>> views=jdbc.queryForList("SELECT table_name from information_schema.VIEWS where table_schema=? order by table_name asc ",new  Object[]{databaseName});
	for(Map<String,Object> map:views){
		Object key=map.keySet().toArray()[0];
		map.put("text",map.get(key));
		map.put("id",map.get(key));
		map.put("leaf",true);
	}
	Map<String,Object> viewNode=new HashMap<String,Object>();
	viewNode.put("children",views);
	viewNode.put("text","视图");
	viewNode.put("id","view");
	viewNode.put("expanded",true);
	//show procedure status
	
	List<Map<String,Object>> pros=jdbc.queryForList("show procedure status");
	for(Map<String,Object> map:pros){
		//Object key=map.keySet().toArray()[0];
		map.put("text",map.get("name"));
		map.put("id",map.get("name"));
		map.put("leaf",true);
	}
	Map<String,Object> proNode=new HashMap<String,Object>();
	proNode.put("children",pros);
	proNode.put("text","过程");
	proNode.put("id","proc");
	proNode.put("expanded",true);
	
	List<Map<String,Object>>funcs=jdbc.queryForList("show function status");
	for(Map<String,Object> map:funcs){
		//Object key=map.keySet().toArray()[0];
		map.put("text",map.get("name"));
		map.put("id",map.get("name"));
		map.put("leaf",true);
	}
	Map<String,Object> funcNode=new HashMap<String,Object>();
	funcNode.put("children",funcs);
	funcNode.put("text","函数");
	funcNode.put("id","func");
	funcNode.put("expanded",true);
	
	root.add(tableNode);
	root.add(viewNode);
	root.add(proNode);
	root.add(funcNode);
	out.println(JSON.toJSONString(root));
}else if("getColumns".equals(cmd)){
	String table=request.getParameter("table");
	String sql="SELECT column_name as id,table_schema,table_name, column_name,is_nullable,column_type,column_key,extra,column_default,column_comment from information_schema.columns where table_name= ? and table_schema=? order by ordinal_position asc";//(column_name LIKE ? AND) 

	List<Map<String,Object>> list=jdbc.queryForList(sql,new Object[]{table,databaseName});

	success.put("total",list.size());
	success.put("rows",list);
	out.println(JSON.toJSONString(success));	
}else if("getViewScript".equals(cmd)){
	String table=request.getParameter("table");
	Map<String,Object> views=jdbc.queryForMap("SELECT view_definition from information_schema.VIEWS where table_schema=? and table_name=? ",new  Object[]{databaseName,table});
	
	success.put("msg",views.get("view_definition"));
	out.println(JSON.toJSONString(success));	
}else if("getProcScript".equals(cmd)){
	String table=request.getParameter("table");
	String sql="show create procedure  "+table;
	////System.out.println(sql);
	Map<String,Object> views=jdbc.queryForMap(sql);
	
	success.put("msg",views.get("create procedure"));
	out.println(JSON.toJSONString(success));	
}else if("getFuncScript".equals(cmd)){
	String table=request.getParameter("table");
	String sql="show create function  "+table;
	////System.out.println(sql);
	Map<String,Object> views=jdbc.queryForMap(sql);
	
	success.put("msg",views.get("create function"));
	out.println(JSON.toJSONString(success));	
}else if("finder".equals(cmd)){
	String sql=request.getParameter("sql");
	////System.out.println(sql);
	long stime=System.currentTimeMillis();
	try{
		if(sql.trim().indexOf("select ")==0||sql.trim().indexOf("SELECT ")==0){
			
		List<Map<String,Object>> rows=jdbc.queryForList(sql);
		List<Map<String,Object>> fields=new ArrayList<Map<String,Object>> ();
		if(rows.size()>0){
			for(String s:rows.get(0).keySet()){
				Map<String,Object> m=new HashMap<String,Object>();
				m.put("field",s);
				m.put("title",s);
				m.put("editor","text");
				fields.add(m);
			}
			
		}

		long etime=System.currentTimeMillis();
		success.put("total",0);
		success.put("rows",rows);
		success.put("fields",fields);
		success.put("type","grid");
		success.put("msg","成功执行，用时"+(etime-stime)+"ms");
		}else{
			int c=jdbc.update(sql);

			long etime=System.currentTimeMillis();
			success.put("msg","成功执行，用时"+(etime-stime)+"ms，影响记录数:"+c);
			success.put("type","msg");
		}

	}catch(Exception e){
		e.printStackTrace();
		success.put("msg",e.getLocalizedMessage());
		success.put("type","msg");
	}
	out.println(JSON.toJSONString(success));	
}else if("alert_edit".equals(cmd)){
	
	String table=request.getParameter("table");

	String id=request.getParameter("id");
	String column_name=request.getParameter("column_name");
	String column_type=request.getParameter("column_type");
	String is_nullable=request.getParameter("is_nullable");
	String column_key=request.getParameter("column_key");
	String extra=request.getParameter("extra");
	String column_default=request.getParameter("column_default");
	String column_comment=request.getParameter("column_comment");
	String sql="ALTER TABLE %s  CHANGE %s %s %s %s  COMMENT '%s'";
	String default_sql="alter table %s alter column %s set default '%s'";
	if("id".equalsIgnoreCase(id)){
		success.put("msg", "主键字段不可修改");
	}else{
	if("YES".equals(is_nullable)){
		is_nullable="";
	}else{
		is_nullable="not null"; 
	}
	//System.out.println(String.format(sql, table,id, column_name,column_type,is_nullable,column_comment));
	//System.out.println(String.format(default_sql, table, column_name,column_default));
	try{
	jdbc.update(String.format(sql, table,id, column_name,column_type,is_nullable,column_comment));
	if(Utils.isNotNull(column_default))
	jdbc.update(String.format(default_sql, table, column_name,column_default));
	success.put("msg", "字段修改成功");
	}catch(Exception e){
		e.printStackTrace();
		success.put("msg", "操作异常");
	}
	}
	out.println(JSON.toJSONString(success));
}else if("alert_add".equals(cmd)){
	String table=request.getParameter("table");

	String id=request.getParameter("id");
	String column_name=request.getParameter("column_name");
	String column_type=request.getParameter("column_type");
	String is_nullable=request.getParameter("is_nullable");
	String column_key=request.getParameter("column_key");
	String extra=request.getParameter("extra");
	String column_default=request.getParameter("column_default");
	String column_comment=request.getParameter("column_comment");
	String sql="ALTER TABLE %s  ADD  %s %s ";
	String default_sql="alter table %s alter column %s set default '%s'";
	
	if(id==null)id=column_name;
	////System.out.println(String.format(sql, table, column_name,column_type));
	try{
		jdbc.update(String.format(sql, table, column_name,column_type));
		sql="ALTER TABLE %s  CHANGE %s %s %s %s  COMMENT '%s'";
		jdbc.update(String.format(sql, table,id, column_name,column_type,is_nullable,column_comment));
		if(Utils.isNotNull(column_default))
			jdbc.update(String.format(default_sql, table, column_name,column_default));
		success.put("msg", "操作成功");
	}catch(Exception e){
		e.printStackTrace();
		success.put("msg", "操作异常");
	}
	out.println(JSON.toJSONString(success));
}else if("alert_remove".equals(cmd)){

	String table=request.getParameter("table");

	String id=request.getParameter("id");

	String sql="ALTER TABLE %s  DROP column  %s ";
	//System.out.println(String.format(sql, table, id));
	jdbc.update(String.format(sql, table, id));

	out.println(JSON.toJSONString(success));
}else{//show create function func_name
	System.out.println("NO CMD:"+request.getParameter("c"));
	/*
	CREATE TABLE gbsmilie(id int NOT NULL AUTO_INCREMENT,primary key(id));
	*/
	out.println(JSON.toJSONString(failure));
}
%>