<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
public static Object lock=new Object();
private static synchronized boolean insertFunc(JdbcTemplate jdbc,String module, String func, boolean isScriptEntity,IChooseService chooser,IModelService modelService) {
	
	String sql = "select count(*) from tlingx_func where module=? and func=?";
	int c =jdbc.queryForInt(sql, new Object[] { module, func });
	if (c == 0) {
		if (isScriptEntity) {
			
			IEntity se =modelService.getEntity(module); //modelService.get(module);
			String methodName = "";
			if (chooser.defaultMethodContains(func)) {
				methodName = func;
			} else {
				List<IMethod> list = se.getMethods().getList();
				for (IMethod m : list) {
					if (func.equals(m.getCode())) {
						methodName = m.getName();
						break;
					}
				}
			}
			String fid=getFuncFid(jdbc,module,func);
			String name = String.format("%s-%s", se.getName(), methodName);
			sql = "insert into tlingx_func(id,name,module,func,type,fid)values(?,?,?,?,2,?)";
			jdbc.update(sql,
					new Object[] {String.format("%s-%s", module, func), name, module, func,fid });
		}
		return false;
	} else {
		return true;
	}
}

private static synchronized String getFuncFid(JdbcTemplate jdbc,String module, String func){
	String sql="select id from tlingx_func where module=?";
	try {
		return jdbc.queryForObject(sql,String.class,new Object[]{module+"_object"});
	} catch (Exception e) {
		String fid=getObjFid(jdbc,module);
		Map<String,Object> map=jdbc.queryForMap("select name from tlingx_entity where code=?",new Object[]{module});
		jdbc.update("insert into tlingx_func(id,name,module,func,type,fid)values(uuid(),?,?,?,2,?)",new Object[]{map.get("name"),module+"_object","-",fid});

		return jdbc.queryForObject(sql,String.class,new Object[]{module+"_object"});
	}
}
private static synchronized String getObjFid(JdbcTemplate jdbc,String module){
	String sql="select id from tlingx_func where module=?";
	String appId=jdbc.queryForObject("select app_id from tlingx_entity where code=?",String.class,new Object[]{module});
	
	try {
		return jdbc.queryForObject(sql,String.class,new Object[]{appId+"_app"});
	} catch (Exception e) {
		String fid= getAppFid(jdbc);
		Map<String,Object> app=jdbc.queryForMap("select name from tlingx_app where id=?",new Object[]{appId});
		jdbc.update("insert into tlingx_func(id,name,module,func,type,fid)values(uuid(),?,?,?,2,?)",new Object[]{app.get("name"),appId+"_app","-",fid});

		return jdbc.queryForObject(sql,String.class,new Object[]{appId+"_app"});
	}
}
private static synchronized String getAppFid(JdbcTemplate jdbc){

	String sql="select id from tlingx_func where module='root_root'";
	try {
		return jdbc.queryForObject(sql,String.class);
	} catch (Exception e) {
		jdbc.update("insert into tlingx_func(id,name,module,func,type,fid)values(uuid(),?,?,?,2,0)",new Object[]{"ROOT","root_root","-"});

		return jdbc.queryForObject(sql,String.class);
	}
}

public String getRoleId(String userId,JdbcTemplate jdbc){
	return jdbc.queryForObject("select role_id from tlingx_userrole where user_id=? limit 1", String.class,userId);
}
%>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
IModelService modelService=spring.getBean(IModelService.class);
IDatabaseService databaseService=spring.getBean(IDatabaseService.class);
if(!lingx.isSuperman(request))return;
	String cmd=request.getParameter("c");
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
if("getEntityList".equals(cmd)){
	String param=request.getParameter("param");
	String condi="";
	if(param!=null){
		condi=" and ( t.name like '%"+param+"%' or t.code like '%"+param+"%' ) ";
	}
	List<Map<String,Object>> list=jdbc.queryForList("select t.* from tlingx_entity t,tlingx_app a where t.status=1 "+condi+" and t.app_id=a.id and a.status=1 and t.app_id in(select app_id from tlingx_roleapp where role_id in(select role_id from tlingx_userrole where user_id=?)) order by t.create_time desc",userBean.getId());
	for(Map<String,Object> map:list){
		try{
			IEntity e=modelService.getCacheEntity(map.get("code").toString());
			map.put("modelId", e.getId());
		}catch(Exception e){}
	}
	out.println(JSON.toJSONString(list));
}else if("getEntityTree".equals(cmd)){
	String code=request.getParameter("code");
	String id=request.getParameter("node");
	IEntity entity=modelService.getCacheEntity(code);
	if(entity==null){
		out.println("{}");
		return;
	}
	List<Map<String,Object>> list=modelService.getTreeList(entity,id);
	out.println(JSON.toJSONString(list));
}else if("getTableTree".equals(cmd)){
	String databaseName=databaseService.getDatabaseName();
	List<Map<String,Object>> root=new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> tables=jdbc.queryForList("SELECT table_name from information_schema.TABLES where table_type='BASE TABLE' and table_schema=? and table_name not like 'tlingx_%' order by table_name asc",databaseName);
	for(Map<String,Object> map:tables){
		Object key=map.keySet().toArray()[0];
		map.put("text",map.get(key));
		map.put("id",map.get(key));
		map.put("leaf", true);
	}
	Map<String,Object> tableNode=new HashMap<String,Object>();
	tableNode.put("children",tables);
	tableNode.put("text","数据表");
	tableNode.put("id","table");
	root.add(tableNode);
	out.println(JSON.toJSONString(tables));
}else if("getViewTree".equals(cmd)){

String databaseName=databaseService.getDatabaseName();
	List<Map<String,Object>> root=new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> tables=jdbc.queryForList("SELECT table_name from information_schema.TABLES where table_type='VIEW' and table_schema=? order by table_name asc",databaseName);
	for(Map<String,Object> map:tables){
		Object key=map.keySet().toArray()[0];
		map.put("text",map.get(key));
		map.put("id",map.get(key));
		map.put("leaf", true);
	}
	Map<String,Object> tableNode=new HashMap<String,Object>();
	tableNode.put("children",tables);
	tableNode.put("text","数据表");
	tableNode.put("id","table");
	root.add(tableNode);
	out.println(JSON.toJSONString(tables));
}else if("getColumns".equals(cmd)){
	String databaseName=databaseService.getDatabaseName();
	String table=request.getParameter("table");
	String sql="SELECT column_name as id,table_schema,table_name, column_name,is_nullable,column_type,column_key,extra,column_default,column_comment from information_schema.columns where table_name= ? and table_schema=? order by ordinal_position asc";//(column_name LIKE ? AND) 

	List<Map<String,Object>> list=jdbc.queryForList(sql,new Object[]{table,databaseName});

	out.println(JSON.toJSONString(list));	
}else if("createEntity".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String code=request.getParameter("code");
	String name=request.getParameter("name");
	String app=request.getParameter("app");
	String idtype=request.getParameter("idtype");
	IEntity entity=modelService.createScriptEntity(code,name, userBean.getAccount() ,Integer.parseInt(idtype),databaseService.getDatabaseName(), jdbc);
	modelService.save(entity);
	if(jdbc.queryForInt("select count(*) from tlingx_entity where code=?",code)==0){
		jdbc.update("insert into tlingx_entity(id,name,code,type,status,app_id,create_time)values(uuid(),?,?,1,1,?,?)",name,code,app,Utils.getTime());
	}
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));	
}else if("getProperty".equals(cmd)){
	String code=request.getParameter("code");
	String id=request.getParameter("id");
	
	out.println(JSON.toJSONString(modelService.getProperty(code, id)));
}else if("saveProperty".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	boolean b=modelService.saveProperty(request.getParameter("code"), request.getParameter("id"), request.getParameter("prop"), request.getParameter("value"),request.getParameter("oldvalue"),userBean.getAccount());//-------
	if(b){
		res.put("code", 1);
		res.put("message", "设置成功");
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("getScript".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	IScript sc=(IScript)modelService.getById(modelService.get(request.getParameter("code")), request.getParameter("id"));
	res.put("code", 1);
	res.put("message", "设置成功");
	res.put("script", sc.getScript());
	out.println(JSON.toJSONString(res));	
}else if("addField".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String id=modelService.addField(request.getParameter("code"), request.getParameter("id"));
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("addFieldGlobal".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String code=request.getParameter("code");
	String id=request.getParameter("id");
	String fieldCode=request.getParameter("fieldCode");
	String order=request.getParameter("order");
	id=modelService.addFieldGlobal(code, id, fieldCode, order);
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
/* 	System.out.println(code);
	System.out.println(id);
	System.out.println(fieldCode);
	System.out.println(order); */
	out.println(JSON.toJSONString(res));	
}else if("addMethod".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String id=modelService.addMethod(request.getParameter("code"), request.getParameter("id"));
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("addValidator".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String id=modelService.addValidator(request.getParameter("code"), request.getParameter("id"));
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("addInterpreter".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String id=modelService.addInterpreter(request.getParameter("code"), request.getParameter("id"));
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("addExecutor".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String id=modelService.addExecutor(request.getParameter("code"), request.getParameter("id"));
	if(id!=null){
		res.put("code", 1);
		res.put("message", "设置成功");
		res.put("id",id);
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("copyEntity".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	boolean b=modelService.copyEntity(request.getParameter("code"), request.getParameter("ids"));
	if(b){
		res.put("code", 1);
		res.put("message", "复制成功");
	}else{
		res.put("code", -1);
		res.put("message", "复制失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("pasteEntity".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	boolean b=modelService.pasteEntity(request.getParameter("code"), request.getParameter("id"));
	if(b){
		res.put("code", 1);
		res.put("message", "粘贴成功");
	}else{
		res.put("code", -1);
		res.put("message", "粘贴失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("move".equals(cmd)){

	Map<String,Object> res=new HashMap<String,Object>();
	boolean b=modelService.move(request.getParameter("code"), request.getParameter("id"),Integer.parseInt(request.getParameter("type")));
	if(b){
		res.put("code", 1);
		res.put("message", "移动成功，刷新可见");
	}else{
		res.put("code", -1);
		res.put("message", "移动失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("remove".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	boolean b=modelService.remove(request.getParameter("code"), request.getParameter("id"));
	if(b){
		res.put("code", 1);
		res.put("message", "设置成功");
	}else{
		res.put("code", -1);
		res.put("message", "设置失败");
	}
	out.println(JSON.toJSONString(res));	
}else if("leftSelectItems".equals(cmd)){
	List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	Map<String,String> map=new HashMap<String,String>();
	map.put("text", "当前用户");
	map.put("value", "{currentUser}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "当前组织");
	map.put("value", "{currentOrg}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "当前应用");
	map.put("value", "{currentApp}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	
	IEntity entity=(IEntity)modelService.get(request.getParameter("entityCode"));
	for(IField f:entity.getFields().getList()){

		map=new HashMap<String,String>();
		map.put("text", "对象."+f.getName());
		map.put("value", f.getCode());
		list.add(map);
	}

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	out.println(JSON.toJSONString(list));
}else if("rightSelectItems".equals(cmd)){
	List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	Map<String,String> map;

	map=new HashMap<String,String>();
	map.put("text", "授权组织ID拼接");
	map.put("value", "{authOrg2}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "授权角色ID拼接");
	map.put("value", "{authRole2}");
	list.add(map);	
	
	map=new HashMap<String,String>();
	map.put("text", "当前用户所在组织ID拼接");
	map.put("value", "{currentOrg2}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "当前角色ID拼接");
	map.put("value", "{currentRole2}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "行政组织及所有下级ID拼接");
	map.put("value", "{subOrg2}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "应用组织及所有下级ID拼接");
	map.put("value", "{appOrg2}");
	list.add(map);
	

	map=new HashMap<String,String>();
	map.put("text", "角色组织及所有下级ID拼接");
	map.put("value", "{roleOrg2}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "授权组织");
	map.put("value", "{authOrg}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "授权角色");
	map.put("value", "{authRole}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "授权权限");
	map.put("value", "{authFunc}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "授权菜单");
	map.put("value", "{authMenu}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "当前用户");
	map.put("value", "{currentUser}");
	list.add(map);
	

	map=new HashMap<String,String>();
	map.put("text", "当前用户行政组织");
	map.put("value", "{currentUserOrg}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "当前用户角色组织");
	map.put("value", "{currentRoleOrg}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "当前用户所在组织");
	map.put("value", "{currentOrg}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "行政组织及所有子节点");
	map.put("value", "{subOrg}");
	list.add(map);
	
	map=new HashMap<String,String>();
	map.put("text", "应用组织及所有子节点");
	map.put("value", "{appOrg}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "角色组织及所有子节点");
	map.put("value", "{roleOrg}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "当前角色");
	map.put("value", "{currentRole}");
	list.add(map);
	map=new HashMap<String,String>();
	map.put("text", "当前应用");
	map.put("value", "{currentApp}");
	list.add(map);	

	map=new HashMap<String,String>();
	map.put("text", "正开发应用");
	map.put("value", "{currentDevApp}");
	list.add(map);

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	IEntity entity=(IEntity)modelService.get(request.getParameter("entityCode"));
	for(IField f:entity.getFields().getList()){

		map=new HashMap<String,String>();
		map.put("text", "对象."+f.getName());
		map.put("value", f.getCode());
		list.add(map);
	}

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	List<Map<String,Object>> listEntity=jdbc.queryForList("select * from tlingx_entity where type=3 and status=1");
	for(Map<String,Object> temp:listEntity){
		entity=(IEntity)modelService.get(temp.get("code").toString());
		for(IField f:entity.getFields().getList()){
			map=new HashMap<String,String>();
			map.put("text", temp.get("name")+"."+f.getName());
			map.put("value", temp.get("code").toString()+"."+f.getCode());
			list.add(map);
		}
	}

	map=new HashMap<String,String>();
	map.put("text", "-");
	map.put("value", "-");
	list.add(map);
	for(int i=0;i<10;i++){
		map=new HashMap<String,String>();
		map.put("text",String.valueOf(i));
		map.put("value", String.valueOf(i));
		list.add(map);
	}
	out.println(JSON.toJSONString(list));
}else if("getFuncTree".equals(cmd)){
	String code=request.getParameter("code");
	IEntity entity=modelService.get(code);
	List<Map<String,Object>> tree=new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> children=new ArrayList<Map<String,Object>>();
	Map<String,Object> root=new HashMap<String,Object>();
	int count=jdbc.queryForInt("select count(*) from tlingx_func where module=?",code+"_object");
	root.put("text", entity.getName());
	root.put("expanded", true);//expanded: true
	root.put("checked", count>0);
	root.put("id",code);
	tree.add(root);
	root.put("children", children);
	for(IMethod m:entity.getMethods().getList()){
		count=jdbc.queryForInt("select count(*) from tlingx_func where module=? and func=?",code,m.getCode());
		Map<String,Object> leaf=new HashMap<String,Object>();
		leaf.put("text", m.getName()+"["+m.getCode()+"]");
		leaf.put("id", m.getCode());
		leaf.put("leaf", true);
		leaf.put("checked", count>0);
		children.add(leaf);
	}
	
	IChooseService chooser=applicationContext.getBean(IChooseService.class);
	Map<String,IMethod> map=chooser.getDefaultMethods();
	for(String s:map.keySet()){
		count=jdbc.queryForInt("select count(*) from tlingx_func where module=? and func=?",code,s);
		Map<String,Object> leaf=new HashMap<String,Object>();
		IMethod m=map.get(s);
		leaf.put("text",m.getName()+"["+s+"]");
		leaf.put("id", s);
		leaf.put("leaf", true);
		leaf.put("checked", count>0);
		children.add(leaf);
	}
	out.println(JSON.toJSONString(tree));
}else if("addFunc".equals(cmd)){
	synchronized(lock){
	Map<String,Object> res=new HashMap<String,Object>();
	String module=request.getParameter("module");
	String func=request.getParameter("func");
	IChooseService chooser=applicationContext.getBean(IChooseService.class);
	insertFunc(jdbc,module,func,true,chooser,modelService);
	jdbc.update("INSERT into tlingx_rolefunc(id,role_id,func_id)select uuid(),'6e0362e8-100e-11e5-b7ab-74d02b6b5f61',id from tlingx_func t where not EXISTS (select 1 from tlingx_rolefunc where role_id='6e0362e8-100e-11e5-b7ab-74d02b6b5f61' and func_id=t.id)");
	
	String currentRoleId=getRoleId(userBean.getId(),jdbc);
	if(!"6e0362e8-100e-11e5-b7ab-74d02b6b5f61".equals(currentRoleId))
	jdbc.update("INSERT into tlingx_rolefunc(id,role_id,func_id)select uuid(),'"+currentRoleId+"',id from tlingx_func t where not EXISTS (select 1 from tlingx_rolefunc where role_id='"+currentRoleId+"' and func_id=t.id)");
	
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
	}
}else if("delFunc".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	jdbc.update("delete from tlingx_func where fid is null");
	jdbc.update("delete from tlingx_func where module=? and func=?",request.getParameter("module"),request.getParameter("func"));
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("cloneStart".equals(cmd)){
	String code=request.getParameter("code");
	IEntity entity=modelService.get(code);
	Map<String,Object> res=new HashMap<String,Object>();
	res.put("name", entity.getName());
	res.put("code", entity.getCode());
	res.put("table", entity.getTableName());
	res.put("message", "操作成功");

	out.println(JSON.toJSONString(res));
}else if("clone".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String code=request.getParameter("cloneCode");
	String newCode=request.getParameter("newCode");
	String newName=request.getParameter("newName");
	//System.out.println(code);
	//System.out.println(newCode);
	IEntity entity=modelService.getEntity(code);
	IEntity newEntity=(IEntity)modelService.clone(entity);
	newEntity.setCode(newCode);
	newEntity.setName(newName);
	modelService.save(newEntity);
	String appid=jdbc.queryForObject("select app_id from tlingx_entity where code=?",String.class,code);
	if(jdbc.queryForInt("select count(*) from tlingx_entity where code=?",newCode)==0)
	jdbc.update("insert into tlingx_entity(id,name,code,type,status,app_id,create_time)values(uuid(),?,?,1,1,?,?)",newName,newCode,appid,Utils.getTime());
	res.put("success", true);
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("getComboData".equals(cmd)){//lingx/model/editor/handler.jsp?c=getComboData&type=validater
	String type=request.getParameter("type");
	if("validater".equals(type)){
		IValidateService vs=spring.getBean(IValidateService.class);
		out.print(JSON.toJSONString(vs.getComboData()));
	}else if("interpreter".equals(type)){
		IInterpretService vs=spring.getBean(IInterpretService.class);
		out.print(JSON.toJSONString(vs.getComboData()));
	}
}else if("createQueryEntity".equals(cmd)){//lingx/model/editor/handler.jsp?c=getComboData&type=validater
	Map<String,Object> res=new HashMap<String,Object>();

	String code=request.getParameter("code");
	String name=request.getParameter("name");
	String app=request.getParameter("app");
	modelService.createQueryEntity(code, name, userBean.getAccount(), app);
	res.put("success", true);
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("getDisplayModel".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();

	String code=request.getParameter("code");

	res.put("method", modelService.get(code).getDisplayMode());
	res.put("success", true);
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("pluginSetOptions".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();

	String id=request.getParameter("id");
	String options=request.getParameter("options");
	
	jdbc.update("update tlingx_plugin set options=?,modify_time=? where id=?",options,Utils.getTime(),id);
	lingx.getPluginManager().refreshPlugin(id);
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("saveModelToDisk".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();

	modelService.saveModelToDisk();
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("recoveryModelForDisk".equals(cmd)){//http://127.0.0.1:8080/lingx-web/lingx/model/editor/handler.jsp?c=recoveryModelForDisk
	Map<String,Object> res=new HashMap<String,Object>();

	modelService.recoveryModelForDisk();
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("isComplete".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	int c=jdbc.queryForInt("select count(*) from tlingx_entity where code=? and status=2",request.getParameter("code"));
	res.put("code", 1);
	res.put("isComplete", c==1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("createEntityAuto".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	String name=request.getParameter("name");
	String options=request.getParameter("options");
	ICreateService createService=spring.getBean(ICreateService.class);
	createService.create(name, options, userBean.getApp().getId(),userBean);
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("deleteEntity".equals(cmd)){
	String code=request.getParameter("code");
	if(code.indexOf("lingx")!=-1&&code.indexOf("clone")==-1){
		Map<String,Object> res=new HashMap<String,Object>();
		res.put("code", 1);
		res.put("message", "操作失败，不可删除平台对象");
		out.println(JSON.toJSONString(res));
		return ;
	}
	jdbc.update("delete from tlingx_entity where code=?",code);
	jdbc.update("delete from tlingx_model where id=?",code);
	jdbc.update("delete from tlingx_func where module=?",code);
	jdbc.update("delete from tlingx_func where module=?",code+"_object");
	//jdbc.update("DROP TABLE "+code);
	Map<String,Object> res=new HashMap<String,Object>();
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("getEntityListForRef".equals(cmd)){
	List<Map<String,Object>> list=jdbc.queryForList("select name ,code from tlingx_entity where is_ref=1 and app_id in (select app_id from tlingx_roleapp where role_id in(select role_id from tlingx_userrole where user_id=?) ) order by create_time desc",userBean.getId());

	out.println(JSON.toJSONString(list));
}else if("supermanAuth".equals(cmd)){
	IUserService us=spring.getBean(IUserService.class);
	us.superManagerAuthRefresh();
	Map<String,Object> res=new HashMap<String,Object>();
	res.put("code", 1);
	res.put("message", "操作成功");
	out.println(JSON.toJSONString(res));
}else if("exeSQL".equals(cmd)){
	Map<String,Object> res=new HashMap<String,Object>();
	res.put("code", 1);
	res.put("message", "操作成功");
	String sql1=request.getParameter("sql").trim();
	try{
		StringBuilder sb=new StringBuilder();
		String array[]=sql1.split(";");
		for(String sql:array){
			if(sql.startsWith("select")||sql.startsWith("SELECT")){
				sb.append(JSON.toJSONString(jdbc.queryForList(sql))).append("<br>");
			}else{
				sb.append("影响记录数："+jdbc.update(sql)).append("<br>");
			}
		}
		res.put("message", sb.toString());
	}catch(Exception e){
		res.put("message", e.getMessage());
	}
	
	out.println(JSON.toJSONString(res));
}else{
	System.out.println("参数c的值有误,editor/handler.jsp");
}
%>