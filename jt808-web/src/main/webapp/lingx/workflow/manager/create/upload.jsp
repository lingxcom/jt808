<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>上传流程</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">

var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/manager/handler.jsp";
function lingxSubmit(){
	Ext.getCmp("form").getForm().submit({
		params:{lgxsn:1},
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
			id:"form",
			xtype:'form',
			width:300,
			bodyStyle:"background:#dfe9f6;",
			 //frame: true,
			 bodyPadding: 5,
			 fieldDefaults: {
		            labelAlign: 'right',
		            labelWidth: 80
		           , anchor: '100%'
		        },
			//layout: 'absolute',
	        url: handlerJsp+"?c=deployment",
	        defaultType: 'textfield',
	        border: false,
	        items:[{
				fieldLabel:"<span style='color:red;'>*</span>"+"流程名称",
				name:'name',
				value:'' ,
				minWidth:100,
				maxWidth:400,
				},{
				fieldLabel:"<span style='color:red;'>*</span>"+"流程文件",
				name:'file',
				xtype:'file',
				value:'' ,
				minWidth:100,
				maxWidth:400,
				}],
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