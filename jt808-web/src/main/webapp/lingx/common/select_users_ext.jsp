<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String text="";
	if(request.getParameter("text")!=null)text=java.net.URLDecoder.decode(request.getParameter("text"),"UTF-8");
	String value="";
	if(request.getParameter("value")!=null)value=request.getParameter("value");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表_grid_combo2</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript">
var handlerJsp="lingx/common/handler.jsp";
//var request_params=${REQUEST_PARAMS};
var fromPageId='${param.pageid}';
//var entityCode=request_params.e;
//var methodCode=request_params.m;
var entityId='${entityId }';
//var params=${_params};

var text="<%=text%>";
var value="<%=value%>";

//params=request_params;
//removeDefaultAttribute(params);

var cmpId='${param.cmpId}';
var textField='name';
var valueField='id';
var extParams={};
var extparam="${extparam}";

var iptType="${param.lingxInputType}"||"1";


function resetExtParams(){
	extParams={};
		for(var t in params){
			extParams[t]=params[t];
		}
		if(extparam){
			extParams['extparam']=extparam;
		}

		//由于lingxSearch引用到该方法，所以查询时会有错误，查两次
		//if(Ext.getCmp("datas")&&Ext.getCmp("datas").getStore())
		//Ext.getCmp("datas").getStore().loadPage(1);
	return extParams;
}
/**
 * 数据提交
 */
function lingxSubmit(){
	var labels=$(".label");
	var val="",txt="";
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		val+=o.find(".value").html()+",";
		txt+=o.find(".text").html()+",";
	}
	if(labels.length>0){
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
	}
	var win=getFromWindow(fromPageId);
	win.lingxSet({cmpId:cmpId,text:txt,value:val});
	closeWindow();
}

function lingxSubmit_bak(){
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj,val="",txt="";
		for(var i=0;i<array.length;i++){
			obj=array[i];
			val=val+obj.data[valueField]+",";
			txt=txt+obj.data[textField]+",";
		}
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
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
			extParams[t]='_'+temp[t];
		}
	}
	//reloadGrid();
	Ext.getCmp("datas").getStore().loadPage(1);
}
/**
 * 刷新列表数据
 */
function reloadGrid(){
	Ext.getCmp("datas").getStore().reload();
}
var storeOrgUser = Ext.create('Ext.data.Store', {
    pageSize: 20,
    fields:['name', 'account', 'id'],
    remoteSort: true,
    autoLoad:true,
    proxy: {
    	actionMethods: {
            create : 'POST',
            read   : 'POST', // by default GET
            update : 'POST',
            destroy: 'POST'
        },

    	type: 'ajax',
        url: handlerJsp+"?c=listUserByOrgId",
        reader: {
        	type: 'json',
            root: 'rows',
            totalProperty: 'total'
        }//,
        //simpleSortMode: true
    },
    sorters: [{
        property: "orderindex",
        direction: "asc"
    }],
    listeners:{
    	beforeload:function(){
    		Ext.apply(storeOrgUser.proxy.extraParams,extParams);  
    	},
    	load:function(){
    		//Ext.getCmp("datas").getSelectionModel().select(0);
    	}
    }
});

var storeRoleUser = Ext.create('Ext.data.Store', {
    pageSize: 20,
    fields:['name', 'account', 'id'],
    remoteSort: true,
    autoLoad:true,
    proxy: {
    	actionMethods: {
            create : 'POST',
            read   : 'POST', // by default GET
            update : 'POST',
            destroy: 'POST'
        },

    	type: 'ajax',
        url: handlerJsp+"?c=listUserByRoleId",
        reader: {
        	type: 'json',
            root: 'rows',
            totalProperty: 'total'
        }//,
        //simpleSortMode: true
    },
    sorters: [{
        property: "orderindex",
        direction: "asc"
    }],
    listeners:{
    	beforeload:function(){
    		Ext.apply(storeRoleUser.proxy.extraParams,extParams);  
    	},
    	load:function(){
    		//Ext.getCmp("datas").getSelectionModel().select(0);
    	}
    }
});

var sm1 = Ext.create('Ext.selection.CheckboxModel');  
var sm2 = Ext.create('Ext.selection.CheckboxModel');  
Ext.onReady(function(){
	Ext.create("Ext.Viewport",{
		layout:'border',
		items:[{
			region: 'west',
            split: true,
            autoScroll:true,
            border:false,
            width: 162,
            minWidth: 75,
            maxWidth: 260,
            contentEl:"Value-DIV"
		},{
			
			region:"center",
			//forceFit: true,
			
			split: true,
			xtype:'tabpanel',
			items:[{
				
		        title: '组织',
		        layout:"border",
		        items:[{//组织树型区
		        	id:"orgtrees",
					region: 'west',
					margins: '2 2 2 2',
		            width: 200,
		            xtype:"treepanel",
		            border:false,
		            autoScroll: true,
		            rootVisible: false,
		            store: Ext.create('Ext.data.TreeStore', {
		        		proxy: {
		        	         type: 'ajax',
		        	         url: handlerJsp+"?c=orgtree",
		        	         reader: {
		        	             type: 'json'
		        	         }
		        	     },
		        	     root: {
		        	         text: "ROOT",
		        	         id:"0",
		        	         expanded: true
		        	    },
		        	     autoLoad: true,
		        	     listeners:{
		        	    	 datachanged:function(){
		        	    		 if(Ext.getCmp("orgtrees")&&Ext.getCmp("orgtrees").getSelectionModel().getCount()==0)
		           		    		Ext.getCmp("orgtrees").getSelectionModel().select(0);
		      		    	}
		         	     }
		        	}),
		        	listeners:{
		        		select:function(view,record,item,index,event,obj){
			        		 extParams["id"]=record.data.id;
			        		 storeOrgUser.reload();
			        	}
		        	}
		        },{
		        	region: 'center',
		        	margins: '2 2 2 2',
		        	store: storeOrgUser,
					//forceFit: true,
					split: true,
					xtype:'grid',
					selModel: sm1,
					listeners:{
	    	        	select:function(el,record, index, eOpts ){
	    	        		addItem(record.data);
	    	        	}
	    	        },
	    	        columns:[
	    	                 { text: '账号',  dataIndex: 'account' },
	    	                 { text: '姓名', dataIndex: 'name' }
					],
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: storeOrgUser,
			            displayInfo:true
			        })
		        }]
		    }, {
		        title: '角色',
		        layout:"border",
		        items:[{
					region: 'west',
					margins: '2 2 2 2',
		            width: 200,
		            xtype:"treepanel",
		            border:false,
		            autoScroll: true,
		            rootVisible: false,
		            store: Ext.create('Ext.data.TreeStore', {
		        		proxy: {
		        	         type: 'ajax',
		        	         url: handlerJsp+"?c=roletree",
		        	         reader: {
		        	             type: 'json'
		        	         }
		        	     },
		        	     root: {
		        	         text: "ROOT",
		        	         id:"0",
		        	         expanded: true
		        	    },
		        	     autoLoad: true,
		        	     listeners:{
		        	    	 datachanged:function(){
		        	    		
		      		    	}
		         	     }
		        	}),
		        	listeners:{
		        		select:function(view,record,item,index,event,obj){
			        		 extParams["id"]=record.data.id;
			        		 storeRoleUser.reload();
			        	}
		        	}
		        },{
		        	region: 'center',
		        	margins: '2 2 2 2',
		        	store: storeRoleUser,
					//forceFit: true,
					split: true,
					xtype:'grid',
					selModel: sm2,
					listeners:{
	    	        	select:function(el,record, index, eOpts ){
	    	        		addItem(record.data);
	    	        	}
	    	        },
	    	        columns:[
	    	                 { text: '账号',  dataIndex: 'account' },
	    	                 { text: '姓名', dataIndex: 'name' }
					],
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: storeRoleUser,
			            displayInfo:true
			        })
		        }]
		    }/* , {
		        title: '所有'
		    } */
		    ]
		}]
	});
	initItem();
});


function initItem(){
	if(!value)return;
	var arrayValue=value.split(",");
	var arrayText=text.split(",");
	for(var i=0;i<arrayValue.length;i++){
		var o={};
		o[textField]=arrayText[i];
		o[valueField]=arrayValue[i];
		addItem(o);
	}
}
function addItem(data){
	var labels=$(".label");
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		if(o.find(".value").html()==data[valueField]){
			lgxInfo("该项已选择");
			return;
		}
	}
	//console.log(data);
	var div=$("<div class='label'><span class='value' style='display:none;'>"+data[valueField]+"</span><span class='text'>"+data[textField]+"</span><a class='remove' style='float:right;padding:4px;display:none;' href='javascript:;' onclick='delItem(this)'><img src='lingx/js/resources/css/icons/erase.png' width='12'/></a></div>");
	div.bind("mouseover",function(){
		$(this).find("a").show();
	});
	div.bind("mouseout",function(){
		$(this).find("a").hide();
	});
	if(iptType=="1"){
		$("#Value-DIV").empty();
	}
	$("#Value-DIV").prepend(div);
}
function delItem(el){
	var p=$(el).parent();
	p.empty();
	p.remove();
	delete p;
}
</script>
<style type="text/css">
.label{font-size:14px;line-height:28px;text-align:center;width:140px;margin:2px;border:1px solid #99bbe8;background-color:#ffefbb;}
.remove{line-height:24px;height:24px;border:0px none;}
</style>
</head>
<body>

<div id="Value-DIV" style="text-align:center;">
</div>
</body>

</html>