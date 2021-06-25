<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss4.jsp"%> 
<SCRIPT type="text/javascript">
var downloadUrl="<%="" %>"+"?callback=?";
var handlerJsp="<%=basePath %>lingx/model/update/handler.jsp";
Ext.onReady(function(){
	Ext.create("Ext.Viewport",{
		layout:"fit",
		border: false,
        autoScroll:true,
		items:[{
			id:"form",
			xtype:'form',
			width:300,
	        autoScroll:true,
			bodyStyle:"background:#dfe9f6;",
			 //frame: true,
			 bodyPadding: 5,
			 fieldDefaults: {
		            labelAlign: 'right',
		            labelWidth: 100
		           , anchor: '80%'
		        },
			//layout: 'absolute',
	        url: handlerJsp,
	        defaultType: 'textfield',
	        border: false,
	        items:[{
	        	id:"file",
	        	fieldLabel:"<span style='color:red;'>*</span>更新包",
	        	name:"file",
	        	xtype:"file"
	        }],
	        listeners:{
	        	afterrender:function(panel){
	        		if(panel.getHeight()+Lingx.PANEL_HEIGHT>Lingx.MAX_HEIGHT){
		        		Lingx.getRootWindow().resizeWindow({height:Lingx.MAX_HEIGHT});
	        		}else{
	        			Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT+50});
	        		}
	        	}
	        }
		}]
	}).show();
	
});


function lingxSubmit(){
	var file=Ext.getCmp("file").getValue();
	if(!file){
		lgxInfo("更新包不可为空");
		return;
	}
	//lgxInfo(file);
	$.post(handlerJsp,{c:"updatePakeage",file:file},function(json){
		lgxInfo(json.message);
	},"json");
}
</SCRIPT>
</head>
<body>

</body>

</html>