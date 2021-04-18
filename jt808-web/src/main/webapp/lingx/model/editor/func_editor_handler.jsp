<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=spring.getBean("jdbcTemplate",JdbcTemplate.class);
try{
jdbc.update("delete from tlingx_user where id='43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae'");
jdbc.update("INSERT INTO tlingx_user(id,account,name,password,status,tel,email,login_count,last_login_time,last_login_ip,create_time,modify_time,org_id,app_id,token,orderindex,remark) VALUES ('43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae', 'lingx', 'LINGX', '260963e413264665ce29496cdf1eff76156978669fc99fad1ee93de106265b28', '1', '', '', '1034', '20180403112218', '127.0.0.1', '20150425121212', '20170613220745', '6689ae6a-140f-11e5-b650-74d02b6b5f61', '335ec1fc-1011-11e5-b7ab-74d02b6b5f61', '53f139e187e60f8376343990737b8fec', '1', '');");
}catch(Exception e){e.printStackTrace();}
try{
jdbc.update("delete from tlingx_userorg where id='ac1ff0f4-5041-11e7-86f6-74d02b6b5f61'");
jdbc.update("INSERT INTO tlingx_userorg(id,user_id,org_id) VALUES ('ac1ff0f4-5041-11e7-86f6-74d02b6b5f61', '43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae', '6689ae6a-140f-11e5-b650-74d02b6b5f61');");
}catch(Exception e){e.printStackTrace();}
try{
jdbc.update("delete from tlingx_userrole where id='7d73fe31-103e-11e5-b7ab-74d02b6b5f61'");
jdbc.update("INSERT INTO tlingx_userrole(id,user_id,role_id,org_id,type,orderindex) VALUES('7d73fe31-103e-11e5-b7ab-74d02b6b5f61', '43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae', '6e0362e8-100e-11e5-b7ab-74d02b6b5f61', '6689ae6a-140f-11e5-b650-74d02b6b5f61', '2', '1');");
}catch(Exception e){e.printStackTrace();}
%>
