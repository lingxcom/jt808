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
<title>实体列表</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">

var fromPageId='${param.pageid}';
var entityCode='tlingx_rolefield';
var methodCode='grid';
var entityId='${entityId }';
var params={entitycode:'${param.code}',role_id:'${param.role_id}'};

var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
var extParams={};

function resetExtParams(){
	extParams={};
		for(var t in params){
			extParams[t]=params[t];
		}
	return extParams;
}
/**
 * 数据提交
 */
function lingxSubmit(){
	var array=Ext.getCmp("grid").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj=array[0];
		var val=obj.data[valueField];
		var txt=obj.data[textField];
		var win=getFromWindow(fromPageId);
		win.lingxSet({cmpId:cmpId,text:txt,value:val});
		closeWindow();
	}else{
		lgxInfo("请选择列表项");
	}
	//win.lingxSet({value:"有志者事竟成"});
}
/**
 * 数据查询
 */
function lingxSearch(array){
	extParams=resetExtParams();
	for(var i=0;i<array.length;i++){
		var temp=array[i];
		for(var t in temp){
			extParams[t]=temp[t];
		}
	}
	reloadGrid();
}
/**
 * 刷新列表数据
 */
function reloadGrid(){
	Ext.getCmp("grid").getStore().reload();
}
function methodWindow(button,entityOptions,methodOptions){
	var condi="";//级联参数
	for(var i in params){
		condi=condi+"&"+i+"="+params[i];
	}
	if(methodOptions.currentRow){
		var array=Ext.getCmp("grid").getSelectionModel().getSelection();
		if(array&&array.length>0){
			var id=array[0].data.id;
			openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi);
		}else{
			lgxInfo("请选择要操作的记录");
		}
	}else{
		openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+condi);
	}
}
Ext.onReady(function(){
	extParams=resetExtParams();
	var items=new Array();
	Lingx.post("g",{e:entityCode},function(json){
		for(var i=0;i<json.fields.length;i++){
			var f=json.fields[i];
			if(f.inputType=='hidden'||params[f.code]){//隐藏的不留标签,级联字段
				continue;
			}
			items.push({
				id:f.code,
				fieldLabel:f.name,
				name:f.code});
			
		}
		// 级联处理
		var tempArray=new Array();
		for(var i=0;i<json.columns.length;i++){
			var c=json.columns[i];
			if(!params[c.dataIndex]){
				tempArray.push(c);
			}
		}
		json.columns=tempArray;
		// 级联处理 end
		Ext.each(json.fields,function(obj,index,self){
			if(obj.comboType==2)textField=obj.code;
			if(obj.comboType==1)valueField=obj.code;
		});
		if(json.GridConfig.rownumbers){
			json.columns.unshift({ xtype: 'rownumberer'});
		}
		json.toolbar.push("->");
		json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){
			openSearchWindow(items);
		}});//,xtype:"cycle"
		/*
		* Model
		*/
		Ext.define(entityCode, {
		    extend: 'Ext.data.Model',
		    fields:json.model,
		    idProperty: json.GridConfig.idField
		});
		
		/*
		* Store
		*/
		var store = Ext.create('Ext.data.Store', {
		    pageSize: json.GridConfig.pageSize,
		    model: entityCode,
		    remoteSort: json.GridConfig.remoteSort,
		    autoLoad:json.GridConfig.autoLoad,
		    proxy: {
		    	actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	            },

		    	type: 'ajax',
		        url: "e?e="+entityCode+"&m="+methodCode+"&lgxsn=1",
		        reader: {
		        	type: 'json',
		            root: 'rows',
		            totalProperty: 'total'
		        }//,
		        //simpleSortMode: true
		    },
		    sorters: [{
		        property: json.GridConfig.sortName,
		        direction: json.GridConfig.sortOrder
		    }],
		    listeners:{
		    	beforeload:function(){
		    		Ext.apply(store.proxy.extraParams,extParams);  
		    	},
		    	load:function(){
		    		Ext.getCmp("grid").getSelectionModel().select(0);
		    	}
		    }
		});
		
		Ext.create("Ext.Viewport",{
			layout:'fit',
			border:json.GridConfig.broder,
			items:[{
				id:'grid',
				border:json.GridConfig.broder,
				loadMask:json.GridConfig.loadMask,
				store: store,
				//forceFit: true,
				split: true,
				xtype:'grid',
				viewConfig : {
					//True表示为自动展开/缩小列的宽度以适应grid的宽度，这样就不会出现水平的滚动条
					//forceFit : true
					},

			    dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	        }],
	    	        listeners:{
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		openViewWindow(entityCode,json.name,record.data.id);
	    	        	}
	    	        },
				  columns:json.columns,
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: store,
			            displayInfo:json.GridConfig.displayInfo
			        })
			}]
		});
	});
});
</script>
</head>

</body>

</html>