<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*,org.springframework.jdbc.core.JdbcTemplate"%>
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
	JdbcTemplate jdbc=spring.getBean("jdbcTemplate",JdbcTemplate.class);
	request.setAttribute("exists", jdbc.queryForInt("select count(*) from tlingx_entity where code=?",request.getParameter("tableName")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>创建实体对象</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">
var exists="${exists}";
var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/model/editor/handler.jsp";
var tableName="<%=request.getParameter("tableName") %>";
function lingxSubmit(){
	    if (checkParam()){
	    	if("1"==exists){
	    		if(!confirm("该对象已经存在，确定要覆盖吗？"))return;
	    	}
	    	var text=Ext.getCmp("name").getValue();
	    	var app=Ext.getCmp("app").getValue();
	    	var idtype=Ext.getCmp("idtype").getValue();
	    	Ext.Ajax.request({url:handlerJsp,params:{c:"createEntity",code:tableName,name:text,app:app,idtype:idtype},success:function(res){
	    		var json=Ext.JSON.decode(res.responseText);
	    		lgxInfo(json.message);
	    		if(json.code==1){
	    			getFromWindow(fromPageId).reloadGrid();
	    			closeWindow();
	    			
	    		}
	    	}});
	    	
	    }
	
}
Ext.onReady(function(){
	 
	Ext.create("Ext.Viewport",{
		layout:'fit',
		border:false,
		items:[{
			id:"form",
			xtype:"form",
			bodyPadding: 5,
			border:false,
	        waitMsgTarget: true,
	        bodyStyle:"background:#dfe9f6;",
       	 defaultType: 'textfield',
	        fieldDefaults: {
	            labelAlign: 'right',
	            labelWidth: 100,
	            width: 420
	        },
			items:[{
	            fieldLabel: '<span style="color:red;">*</span>对象代码',
	            xtype:'displayfield',
	            name: 'code',
	            value:'${param.tableName}'
	        },{
	        	id:'name',
	            fieldLabel: '<span style="color:red;">*</span>对象名称',
	            emptyText: '对象名称',
	            name: 'name'
	        },{
	        	id:'app',
	            fieldLabel: '<span style="color:red;">*</span>隶属应用',
	            emptyText: '隶属应用',
	            name: 'app',
	            xtype:'dialogoption',
	        	etype:"tlingx_app"
	        },{
            	id:'idtype',
                fieldLabel: '<span style="color:red;">*</span>主键类型',
                emptyText: '主键类型',
                xtype:"radio",
               store:new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=ZJLX',reader:{type:'json'}}),
					autoLoad:false}),
					value:'1',
                name: 'idtype'
            }],
	        listeners:{
	        	afterrender:function(panel){
	        		Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT});
	        	}
	        }
		}]
	}).show();
});


function checkParam(){
	var fields=Ext.getCmp("form").getForm().getFields();
	var objCache={};
	for(var i=0;i<fields.items.length;i++){
		var obj=fields.items[i].getSubmitData();
		for(var temp in obj){
			objCache[temp]=obj[temp];
		}
	}
	if(!objCache["name"]){
		lgxInfo("对象名称不可为空");
		return false;
	}
	if(!objCache["app"]){
		lgxInfo("隶属应用不可为空");
		return false;
	}
	return true;
}
</script>
</head>
<body>
</body>
</html> 