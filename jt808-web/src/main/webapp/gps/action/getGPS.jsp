<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
 <%
 if(session.getAttribute("SESSION_USER")==null)return;
 	ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
 	String carid=request.getParameter("carid");//
	UserBean userBean=null;
	userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
	Map<String,Object> cache=com.lingx.jt808.IJT808Cache.CACHE_0x0200.getIfPresent(carid);
	String str=com.lingx.jt808.utils.CoordinateConversion.wgs84togcj02(Double.parseDouble(cache.get("lng").toString()),Double.parseDouble(cache.get("lat").toString()));
 	String arr[]=str.split(",");
 	cache.put("lng_gaode", arr[0]);
 	cache.put("lat_gaode", arr[1]);
	out.println(JSON.toJSONString(cache));
 %>