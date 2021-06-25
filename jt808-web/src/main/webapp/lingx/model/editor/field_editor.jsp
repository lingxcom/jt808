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
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体查看</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<style type="text/css">
.edit_word{font-size:12px;line-height:24px;text-align:right;width:100px;}
</style>
<script type="text/javascript">

var fromPageId='';
var entityCode='tlingx_role';
var methodCode='tree';
var entityId='';
var params={};
var entityFields={};
var grid_group={};

var handler="<%=basePath %>system/auth/auth_center2_handler.jsp";



var extParams={};
var cmpId='';
var textField='id';
var valueField='id';

/**
 * 刷新列表数据
 */
 function reloadGrid(){
	 var store=Ext.getCmp("tree").getStore();
		store.getRootNode().remove();
		store.setRootNode({
			 text: "ROOT",
	         id:entityId,
	         expanded: true
		});
}
function methodWindow(button,entityOptions,methodOptions){
	if(methodOptions.currentRow){
		var array=Ext.getCmp("tree").getSelectionModel().getSelection();
		if(array&&array.length>0){
			var id=array[0].data.id;
			openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id);
		}else{
			lgxInfo("请选择要操作的记录");
		}
	}else{
		openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'');
	}
}
function reloadTree(tree1){
	var store=Ext.getCmp(tree1).getStore();
	store.getRootNode().remove();
	store.setRootNode({
		 text: "ROOT",
         id:0,
         expanded: true
	});
}
Ext.onReady(function(){

	
	entityId=entityId?entityId:0;
	Lingx.post("g",{e:entityCode},function(json){
		Ext.each(json.fields,function(obj,index,self){
			if(obj.comboType==2)textField=obj.code;
			if(obj.comboType==1)valueField=obj.code;
		});
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			id:"tree",
			region:"west",
			width:200,
            animate:false,
            border:false,
            autoScroll: true,
            rootVisible: false,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: "e?e="+entityCode+"&m=tree&lgxsn=1",//"+methodCode+"
        	         reader: {
        	             type: 'json'
        	         },
        	     },
        	     root: {
        	         text: "ROOT",
        	         id:entityId,
        	         expanded: true
        	    },
        	     autoLoad: true,
        	     listeners:{
      		    	load:function(){
      		    		if(Ext.getCmp("tree"))
      		    		Ext.getCmp("tree").getSelectionModel().select(0);
      		    	}
         	     }
        	}),
			xtype:"treepanel",
	        listeners:{
	        	itemdblclick:function(view,record,item,index,event,obj){
	        		openViewWindow(entityCode,json.name,record.data.id);
	        	},select:function(view,record,item,index,event,obj){
	        		var tab=Ext.getCmp("tabpanel").getActiveTab();
	        		activeTab(record,tab);
	        	}
	        }
		},{
			id:'tabpanel',
			region:'center',
			xtype:'tabpanel',
			activeTab: 0,
            split: true,
			width:getCenterWidth()/2,
			items:[{
	        	id:"trolefield",//e?e='+o.code+'&m=grid
	        	html: '<iframe id="trolefield" scrolling="auto" frameborder="0" width="100%" height="100%" src=""> </iframe>',
	            title:"角色不可见字段",
	            border:false,
	            autoScroll: true,
				listeners:{
					activate:function(panel,eopts){
						
					}
				}
	        }]
		}]
	});

	});
});

function activeTab(record,tab){
	var frame=Ext.get(tab.getEl()).query("iframe")[0];
	var code='${param.code}';
	frame.src='lingx/model/editor/field_editor_grid.jsp?role_id='+record.data.id+'&code='+code;
}
</script>
</head>
<body>
</body>

</html>