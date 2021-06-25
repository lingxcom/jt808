<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
String cmd=request.getParameter("c");
ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
UserBean userBean=null;
if(session.getAttribute(Constants.SESSION_USER)==null)return;
userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
Map<String,Object> ret=new HashMap<String,Object>();
if("history2".equals(cmd)){
	String id=request.getParameter("id");
	String stime=request.getParameter("stime").replace("-", "").replace(" ", "").replace(":", "")+"00";
	String etime=request.getParameter("etime").replace("-", "").replace(" ", "").replace(":", "")+"59";
	String acc=request.getParameter("acc");
	try{

		List<Map<String,Object>> list=jdbc.queryForList("select lat,lng,acc from tgps_data where car_id=? and gpstime>? and gpstime<? order by gpstime asc",id,stime,etime );
		for(Map<String,Object> cache:list){
			String str=com.lingx.jt808.utils.CoordinateConversion.wgs84togcj02(Double.parseDouble(cache.get("lng").toString()),Double.parseDouble(cache.get("lat").toString()));
		 	String arr[]=str.split(",");
		 	cache.put("lng", arr[0]);
		 	cache.put("lat", arr[1]);
		}
		ret.put("list", list);
	}catch(Exception e){
		ret.put("code",-1);
		ret.put("message", "数据不存在");
	}
	out.println(JSON.toJSONString(ret));
}else{
	System.out.println("参数c的值有误:"+cmd+","+request.getServletPath());
}
%>