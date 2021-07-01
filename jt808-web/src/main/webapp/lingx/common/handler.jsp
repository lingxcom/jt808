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
if("orgtree".equals(cmd)){
	String fid=request.getParameter("fid");
	if(fid==null){fid=request.getParameter("node");}
	List<Map<String,Object>> tree;
	if(Utils.isNull(fid)){
	tree=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=jdbc.queryForMap("select *,name text from tlingx_org where id=?",userBean.getApp().getOrgRootId());
		map.put("open", true);
		tree.add(map);
	}else{
		 tree=jdbc.queryForList("select *,name text from tlingx_org where fid=?  order by orderindex asc",fid);
	}
	boolean b=true;
	for(Map<String,Object> map : tree){
		b=jdbc.queryForInt("select count(*) from tlingx_org where fid=?",map.get("id"))>0;
		map.put("isParent", b);
		map.put("leaf", !b);
		if(map.get("state")!=null){
			map.put("expanded", "open".equals(map.get("state").toString()));
		}
		map.put("iconCls", map.get("icon_cls"));
	}

	String json=JSON.toJSONString(tree);
	out.println(json);
	//tree
}else if("roletree".equals(cmd)){
	String fid=request.getParameter("fid");
	if(fid==null){fid=request.getParameter("node");}
	List<Map<String,Object>> tree;
	if(Utils.isNull(fid)){
	tree=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=jdbc.queryForMap("select *,name text from tlingx_role where id=?",userBean.getApp().getRoleRootId());
		map.put("open", true);
		tree.add(map);
	}else{
		 tree=jdbc.queryForList("select *,name text from tlingx_role where fid=?  order by orderindex asc",fid);
	}
	boolean b=true;
	for(Map<String,Object> map : tree){
		b=jdbc.queryForInt("select count(*) from tlingx_role where fid=?",map.get("id"))>0;
		map.put("isParent", b);
		map.put("leaf", !b);
		if(map.get("state")!=null){
			map.put("expanded", "open".equals(map.get("state").toString()));
		}
		map.put("iconCls", map.get("icon_cls"));
	}

	String json=JSON.toJSONString(tree);
	out.println(json);
}else if("getUsersByOrg".equals(cmd)){
	String id=request.getParameter("id");
	List<Map<String,Object>> list=jdbc.queryForList("select id,name from tlingx_user where status=1 and id in(select user_id from tlingx_userorg where org_id=?) order by orderindex asc",id);//convert(name using gbk)
	out.println(JSON.toJSONString(list));
}else if("getUsersByRole".equals(cmd)){
	String id=request.getParameter("id");
	List<Map<String,Object>> list=jdbc.queryForList("select id,name from tlingx_user where status=1 and id in(select user_id from tlingx_userrole where role_id=?) order by orderindex asc",id);//convert(name using gbk)
	out.println(JSON.toJSONString(list));
}else if("lanuage".equals(cmd)){
	II18NService i18n=applicationContext.getBean(II18NService.class);
	String lanuage=request.getParameter("lanuage");
	session.setAttribute("SESSION_LANGUAGE", lanuage);
	i18n.setLanuage(lanuage);
	userBean.setI18n(lanuage);
	String appName=jdbc.queryForObject("select name from tlingx_app where id=?",String.class,userBean.getApp().getId());
	//userBean.getApp().setName(i18n.text(appName));
	out.print(JSON.toJSONString(ret));
}else if("listUserByOrgId".equals(cmd)){
	String id=request.getParameter("id");
	String page1=request.getParameter("page");
	String limit=request.getParameter("limit");
	String sort=request.getParameter("sort");
	String order="";
	if(sort!=null){//EXTJS的传参方式
		List<Map<String,String>> list=(List<Map<String,String>>)JSON.parse(sort);
		Map<String,String>map=list.get(0);
		sort=map.get("property");
		order=map.get("direction");
	}
	if(id!=null){
	
		List<Map<String,Object>> list=jdbc.queryForList("select id,account,name from tlingx_user where status=1 and id in(select user_id from tlingx_userorg where org_id=?) order by "+sort+" "+order+" limit "+(Integer.parseInt(limit)*(Integer.parseInt(page1)-1))+","+limit,id);//convert(name using gbk)
		ret.put("rows",list);
		ret.put("total",jdbc.queryForInt("select count(*) from tlingx_user where status=1 and id in(select user_id from tlingx_userorg where org_id=?)",id));
	}
	
	out.print(JSON.toJSONString(ret));
}else if("listUserByRoleId".equals(cmd)){
	String id=request.getParameter("id");
	String page1=request.getParameter("page");
	String limit=request.getParameter("limit");
	String sort=request.getParameter("sort");
	String order="";
	if(sort!=null){//EXTJS的传参方式
		List<Map<String,String>> list=(List<Map<String,String>>)JSON.parse(sort);
		Map<String,String>map=list.get(0);
		sort=map.get("property");
		order=map.get("direction");
	}
	if(id!=null){
	
		List<Map<String,Object>> list=jdbc.queryForList("select id,account,name from tlingx_user where status=1 and id in(select user_id from tlingx_userrole where role_id=?) order by "+sort+" "+order+" limit "+(Integer.parseInt(limit)*(Integer.parseInt(page1)-1))+","+limit,id);//convert(name using gbk)
		ret.put("rows",list);
		ret.put("total",jdbc.queryForInt("select count(*) from tlingx_user where status=1 and id in(select user_id from tlingx_userorg where org_id=?)",id));
	}
	
	out.print(JSON.toJSONString(ret));
}else if("orgtree_checkbox".equals(cmd)){
	String fid=request.getParameter("fid");
	if(fid==null){fid=request.getParameter("node");}
	List<Map<String,Object>> tree;
	if(Utils.isNull(fid)){
	tree=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=jdbc.queryForMap("select *,name text from tlingx_org where id=?",userBean.getApp().getOrgRootId());
		map.put("open", true);
		tree.add(map);
	}else{
		 tree=jdbc.queryForList("select *,name text from tlingx_org where fid=?  order by orderindex asc",fid);
	}
	boolean b=true;
	for(Map<String,Object> map : tree){
		b=jdbc.queryForInt("select count(*) from tlingx_org where fid=?",map.get("id"))>0;
		map.put("isParent", b);
		map.put("leaf", !b);
		if(map.get("state")!=null){
			map.put("expanded", "open".equals(map.get("state").toString()));
		}
		map.put("iconCls", map.get("icon_cls"));
		//map.put("checked", false);
	}

	String json=JSON.toJSONString(tree);
	out.println(json);
	//tree
}else if("authcode".equals(cmd)){//335ec1fc-1011-11e5-b7ab-1234567b5f61
	String authcode=request.getParameter("p2"),time=Utils.getTime();
	if(jdbc.queryForInt("select count(*) from tlingx_config where id=? ","SN")==0){
		jdbc.update(String.format("INSERT INTO `tlingx_config` VALUES ('SN', 'SN', 'SN', '%s', '1', '%s', '%s', 'SN', 'SN')",authcode,time,time));
	}else{
		String temp=jdbc.queryForObject("select config_value from tlingx_config where id=?", String.class,"SN");
				temp+=","+authcode;
		jdbc.update("update tlingx_config set config_value=?,modify_time=? where id=?",temp,time,"SN");
	}
	ret.put("message", "授权码已保存，请重新登录！");
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值["+cmd+"]有误,system/workflow/manager/handler.jsp");
}
%>