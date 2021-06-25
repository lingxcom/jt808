<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>创建实体对象</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">
var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/model/editor/handler.jsp";
var tableName="<%=request.getParameter("tableName") %>";
var confirmMsg=null;
function lingxSubmit(){
	if(confirmMsg){
		Ext.MessageBox.confirm("系统消息",confirmMsg,function(val){
			if(val=='yes')lingxSubmitFn();
		});
	}else{
		lingxSubmitFn();
	}
}
function lingxSubmitFn(){
	// 非空检查 

	// 非空检查 end
	Ext.getCmp("form").getForm().submit({
		params:{c:"createQueryEntity"},
		success: function(form, action) {
			if(action.result.message)lgxInfo(action.result.message);
		        if(action.result.code==1){
		        	if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){
			        	getFromWindow(fromPageId).reloadGrid();
		        	}
		        	closeWindow();
		        }else{
		        	var json=action.result;
		        		if(json.messages){
		        			for(var i=0;i<json.messages.length;i++){
		        				lgxInfo(json.messages[i]);
		        			}
		        		}
		        }
		    },
		    failure: function(form, action) {
		        switch (action.failureType) {
		            case Ext.form.action.Action.CLIENT_INVALID:
		                Lingx.getRootWindow().Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
		                break;
		            case Ext.form.action.Action.CONNECT_FAILURE:
		            	 Lingx.getRootWindow().Ext.Msg.alert('Failure', 'Ajax communication failed');
		                break;
		            case Ext.form.action.Action.SERVER_INVALID:
		            	 Lingx.getRootWindow().Ext.Msg.alert('Failure', action.result.message);
		       }
		    }
	});
}

Ext.onReady(function(){
	Ext.create("Ext.Viewport",{
		layout:'fit',
		border:false,
		items:[{
			id:'form',
			border:false,
			bodyStyle:"background:#dfe9f6;",
			bodyPadding: 5,
			url:"lingx/model/editor/handler.jsp",
			fieldDefaults: {
		            labelAlign: 'right',
		            labelWidth: 80
		           , anchor: '80%'
		        },
			xtype:"form",
			defaultType: 'textfield',
			items:[{
				fieldLabel:"<span style='color:red;'>*</span>对象代码",
				name:"code",
				value:""
			},{
				fieldLabel:"<span style='color:red;'>*</span>对象名称",
				name:"name",
				value:""
			},{
				fieldLabel:"<span style='color:red;'>*</span>隶属应用",
				name:"app",
	            xtype:'dialogoption',
	        	etype:"tlingx_app",
			}/* ,{
	        	id:'app',
	            fieldLabel: '<span style="color:red;">*</span>隶属应用',
	            emptyText: '隶属应用',
	            name: 'app',
	            xtype:'dialogoption',
	        	etype:"tlingx_app",
	        } */],
	        listeners:{
	        	afterrender:function(panel){
	        		Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT});
	        	}
	        }
		}
		       
		       ]
	}).show();
});
</script>
</head>
<body>
</body>
</html> 