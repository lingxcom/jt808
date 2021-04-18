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
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑实体</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">
var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/center/handler.jsp";
var taskid='${param.taskid}';
Ext.onReady(function(){
	
    var viewport = Ext.create('Ext.Viewport', {
    	layout:'fit',
        border: false,
		items:[{
			id:"form",
			xtype:'form',
	        border: false,
			bodyStyle:"background:#dfe9f6;",
			 //frame: true,
			 bodyPadding: 5,
			 fieldDefaults: {
		            labelAlign: 'right',
		            labelWidth: 120
		           , anchor: '80%'
		        },
			//layout: 'absolute',
	        url: "w",
	        defaultType: 'displayfield',
	        border: false,
	        items:[
	        {
	        	id:"code",
	        	fieldLabel:"任务代码",
				name:"code"
	        },{
	        	id:"name",
	        	fieldLabel:"任务名称",
				name:"name"
	        },{
	        	id:"assignee",
	        	fieldLabel:"任务当前处理人",
				name:"assignee"
	        },{
	        	id:"createtime",
	        	fieldLabel:"任务创建时间",
				name:"createtime"
	        },{
	        	id:"_REQ_USER_NAME",
	        	fieldLabel:"流程发起人",
				name:"_REQ_USER_NAME"
	        },{
	        	id:"_REQ_DATE",
	        	fieldLabel:"流程发起时间",
				name:"_REQ_DATE"
	        }],
	        listeners:{
	        	afterrender:function(panel){
	        		Lingx.post(handlerJsp,{c:"getTaskInfo",taskid:taskid},function(json){
	        			console.log(json);
	        			Ext.getCmp("code").setValue(json.task.code);
	        			Ext.getCmp("name").setValue(json.task.name);
	        			Ext.getCmp("assignee").setValue(json.task.assignee);
	        			Ext.getCmp("createtime").setValue(Lingx.formatTimeTS(json.task.createTime));
	        			Ext.getCmp("_REQ_USER_NAME").setValue(json.variable._REQ_USER_NAME);
	        			Ext.getCmp("_REQ_DATE").setValue(Lingx.formatTime14(json.variable._REQ_DATE));
	        		});
	        		Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT});
	        	}
	        }
		}
		       ]
    });
    viewport.show();
});

function lingxSubmit(){
	
	Lingx.post("w",{_c:"rollback",taskid:taskid},function(json){
		alert(json.message);
		getFromWindow("${param.pageid}").location.reload();
		closeWindow();
	});
}
</script>
</head>
<body>
</body>

</html>