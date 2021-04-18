﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
public String getLanguage(HttpSession session){
	if(session.getAttribute("SESSION_LANGUAGE")==null)return "zh_CN";
	return session.getAttribute("SESSION_LANGUAGE").toString();
}
%>
<%
String cmd=request.getParameter("c");
ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);

com.lingx.core.service.ILingxService lingx=spring.getBean(com.lingx.core.service.ILingxService.class);
UserBean userBean=null;
if(session.getAttribute(Constants.SESSION_USER)==null)return;
userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
if("getTreeList".equals(cmd)){
	String text=request.getParameter("text");
	String node=request.getParameter("node");
	String checkbox=request.getParameter("checkbox");
	String groupWhere1="",groupWhere2="";

	if(Utils.isNotNull(text)){
		List<Map<String,Object>> list2=jdbc.queryForList("select id,carno text,id value,speed from tgps_car where (carno like '%"+text+"%' or id like '%"+text+"%' or czxm like '%"+text+"%') order by online desc");
		for(Map<String,Object> map:list2){
			
			map.put("iconCls", "carStatus_3");
			map.put("id",0+"_"+map.get("id"));
			map.put("leaf",true);
			if("true".equals(checkbox))
			map.put("checked",false);
			

		}
		out.println(JSON.toJSONString(list2));
	}else{
		List<Map<String,Object>> list=null;
		if("0".equals(node)){
			list=jdbc.queryForList("select id,name text,id value,icon_cls from tgps_group where 1=1 order by orderindex asc,convert(name using gbk) asc");
			
		}else{
			list=jdbc.queryForList("select id,name text,id value,icon_cls from tgps_group where fid=?  order by orderindex asc,convert(name using gbk) asc",node);
		}
		 for(Map<String,Object> map:list){
			if("0".equals(node)){
				map.put("nodeType", "async");
			map.put("expanded",false);//2016-12-28改为false
			}
			if("true".equals(checkbox))
			map.put("checked",false);
		
		} 
		 {//根据分组ID取车辆
			List<Map<String,Object>> list2=jdbc.queryForList("select id,carno text,id value,speed from tgps_car where id in(select car_id from tgps_group_car where group_id=?) order by online desc, convert(carno using gbk) asc",node);
			for(Map<String,Object> map:list2){
				Map<String,Object> cache=com.lingx.jt808.IJT808Cache.CACHE_0x0200.getIfPresent(map.get("id").toString());
				
				map.put("iconCls", "carStatus_3");
				map.put("id",node+"_"+map.get("id"));
				map.put("leaf",true);
				if("true".equals(checkbox))
				map.put("checked",false);
				

			}
			list.addAll(list2);
		
		 }
		out.println(JSON.toJSONString(list));
	}
}else{
	System.out.println("参数c的值有误:"+cmd+","+request.getServletPath());
}
%>