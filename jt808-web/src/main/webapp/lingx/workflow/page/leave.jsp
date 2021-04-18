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
<title>流程主页</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">
var task="${param.task}";
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
	        defaultType: 'textfield',
	        border: false,
	        items:[{
	        	id:"workflowid",
	        	name:"workflowid",
	        	value:"${param.workflowid}",
	        	xtype:"hiddenfield"
	        },{
	        	id:"taskid",
	        	name:"taskid",
	        	value:"${param.taskid}",
	        	xtype:"hiddenfield"
	        },{
	        	id:"userid",
	        	name:"userid",
	        	value:"${SESSION_USER.account}",
	        	xtype:"hiddenfield"
	        },{
	        	id:"applyUserId",
	        	name:"applyUserId",
	        	value:"${SESSION_USER.account}",
	        	xtype:"hiddenfield"
	        },{//applyUserId
	        	id:"reApply",
	        	name:"reApply",
	        	value:"false",
	        	xtype:"hiddenfield"
	        },//reApply
	        {
	            xtype: 'fieldset',
	            title: '请假信息',
	            defaultType: 'textfield',
	            defaults: {
	                //width: 280
	            },
	            items: [{
					id:"stime",
				fieldLabel:"<span style='color:red;'>*</span>请假开始时间",
				name:"stime",
				xtype:"datetimefield",
			
				},{
					id:"etime",
					fieldLabel:"<span style='color:red;'>*</span>请假结束时间",
					name:"etime",
					xtype:"datetimefield",
				
				},{
					id:"days",
					fieldLabel:"<span style='color:red;'>*</span>请假天数",
					name:"days",
					xtype:"numberfield",
				
				},{
					id:"text",
					fieldLabel:"<span style='color:red;'>*</span>请假事由",
					xtype:"textarea",
					name:"text"
					},{
					id:"_REQ_USER_NAME",
						fieldLabel:"<span style='color:red;'>*</span>申请人",
						name:"_REQ_USER_NAME",
						xtype:"displayfield",
						value:"-"
				},{id:"_REQ_DATE",
					fieldLabel:"<span style='color:red;'>*</span>申请日期",
					name:"_REQ_DATE",
					xtype:"displayfield",
					value:'-'
			}]},{xtype: 'fieldset',
            title: '审核信息',
            defaultType: 'textfield',
            defaults: {
                //width: 280
            },
            items: [
                    {
                id:"pass1",
				fieldLabel:"<span style='color:red;'>*</span>部门经理审批",
				name:"pass1",
				xtype:"combobox",
				displayField:"text",
				valueField:'value',
				store:new Ext.data.Store({proxy:{ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=LCSP',reader:{type:'json'}},
					autoLoad:true})
				},{
					id:"pass2",
					fieldLabel:"<span style='color:red;'>*</span>总经理审批",
					name:"pass2",
					//disabled :true,
					xtype:"combobox",
					displayField:"text",
					valueField:'value',
					store:new Ext.data.Store({proxy:{ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=LCSP',reader:{type:'json'}},
						autoLoad:true})
					}]}],
	        listeners:{
	        	afterrender:function(panel){	        		
	        		Lingx.post("w",{_c:"variable",taskid:"${param.taskid}"},function(json){
	        			setValue(json);//设置值
	        			if(task=='usertask1'||task=='usertask4'){
		        			Ext.getCmp("pass1").setDisabled(true);
		        			Ext.getCmp("pass2").setDisabled(true); 
		        			//Ext.getCmp("bmjlsp").setValue('');
		        			//Ext.getCmp("rlzysp").setValue('');
		        		}else if(task=='usertask2'){

		        			Ext.getCmp("pass2").setDisabled(true); 
		        		}else if(task=='usertask3'){
		        			Ext.getCmp("pass1").setDisabled(true);
		        			
		        		}
	        		});
	        		Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT});
	        	}
	        }
		}
		       ]
    });
    viewport.show();
});

function setValue(json){
	for(var key in json){
		if("taskid"==key)continue;
		var temp=Ext.getCmp(key);
		if(temp)
		temp.setValue(json[key]);
	}
}

function postForm(submittype_){

	Ext.getCmp("form").getForm().submit({
		params:{_c:submittype_},
		success: function(form, action) {
			alert(action.result.message);
			//window.parent.location.reload();
			getFromWindow("${param.pageid}").location.reload();
			closeWindow();
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

function lingxSave(){
	postForm("save");
}
function lingxSubmit(){
	postForm("submit");
}
</script>
</head>
<body>
</body>

</html>