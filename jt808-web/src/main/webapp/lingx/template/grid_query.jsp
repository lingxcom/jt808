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
<title>实时监控</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<style type="text/css">
</style>
<script type="text/javascript">

var request_params=${REQUEST_PARAMS};

var fromPageId='${param.pageid}';
var entityCode=request_params.e;
var methodCode=request_params.m;
var entityId='${entityId }';
var params=${_params};

params=request_params;
removeDefaultAttribute(params);

var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
var extParams={};
var extparam='${extparam}';

function resetExtParams(){
	extParams={};
		for(var t in params){
			extParams[t]=params[t];
		}
		if(extparam){
			extParams['extparam']=extparam;
		}
	return extParams;
}
/**
 * 数据提交
 */
function lingxSubmit(){
	queryFn();
	/* 
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj=array[0];
		var val=obj.data[valueField];
		var txt=obj.data[textField];
		var win=getFromWindow(fromPageId);
		win.lingxSet({cmpId:cmpId,text:txt,value:val});
		closeWindow();
	}else{
		lgxInfo("请选择列表项");
	} */
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
			//extParams[t]='_'+temp[t];
			extParams[t]=temp[t];//查询不加'_',2015-09-14
		}
	}
	//extParams['page']=1;
	//reloadGrid();
	Ext.getCmp("datas").getStore().loadPage(1);
}
/**
 * 刷新列表数据
 */
function reloadGrid(){
	Ext.getCmp("datas").getStore().reload();
}
function queryFn(){
	var fields=Ext.getCmp("query-form").getForm().getFields();
	var array=new Array();
	for(var i=0;i<fields.items.length;i++){
		var obj=fields.items[i];
		array.push(obj.getSubmitData());
	}
	lingxSearch(array);
}
Ext.onReady(function(){
	extParams=resetExtParams();
	var items=new Array();
	Lingx.post("g",{e:entityCode},function(json){
		//查询框处理
		var queryItems=new Array();
		for(var i=0;i<json.params.length;i++){
			Lingx.fieldPushItems(json.params[i],json,queryItems,params);
		}
		//
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
		json.toolbar.push("-");
		/*
		json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){
			openSearchWindow(json.GridConfig.queryField,items);
		}});
		*/
		//,xtype:"cycle"
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
		    		Ext.getCmp("datas").getSelectionModel().select(0);
		    	}
		    }
		});
		
		Ext.create("Ext.Viewport",{
			layout:'border',
	           padding:2,
			border:json.GridConfig.broder,
			items:[{
				   id:"query-form",
	        	   region:"west",
	        	   split: true,
	               width: 300,
	               minWidth: 175,
	               maxWidth: 400,
	               title:"查询条件",
	               bodyStyle:"background:#dfe9f6;",
	               xtype:'form',
	               bodyPadding: 5,
					 fieldDefaults: {
				            labelAlign: 'right',
				            labelWidth: 100
				           , anchor: '80%'
				        },
	  		        defaultType: 'textfield',
	  		        items:queryItems,
	  		        buttons:[{text:"开始查询",handler:function(){
	  		        	queryFn();
	    			}},{text:"重置表单",handler:function(){
	  		        	Ext.getCmp("query-form").getForm().reset();
	    			}}]
	           },{
					id:"datas",
	        	   region: 'center',
					title:"数据展示",
					xtype:'grid',
					border:true,
					loadMask:json.GridConfig.loadMask,
					store: store,
					//forceFit: true,
					split: true,
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
		    	        		//openViewWindow(entityCode,json.name,record.data.id);
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
<body>
</body>
</html>