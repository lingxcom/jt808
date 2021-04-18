<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String text="";
	if(request.getParameter("text")!=null)text=java.net.URLDecoder.decode(request.getParameter("text"),"UTF-8");
	String value=request.getParameter("value");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体查看</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<style type="text/css">
.label{font-size:14px;line-height:28px;text-align:center;width:140px;margin:2px;border:1px solid #99bbe8;background-color:#ffefbb;}
.remove{line-height:24px;height:24px;border:0px none;}
</style>
<script type="text/javascript">
var handlerJsp="lingx/common/handler.jsp";
var request_params={e:"tlingx_org",m:"tree"};
var fromPageId='${param.pageid}';
var entityCode=request_params.e;
var methodCode=request_params.m;
var entityId='${SESSION_USER.app.orgRootId }';
var params='${_params}';

var text="<%=text%>";
var value="<%=value%>";

params=request_params;
removeDefaultAttribute(params);
var entityFields={};

var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
if(params&&params.fid){
	entityId=params.fid;
}

var iptType="${param.lingxInputType}"||"1";


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

/**
 * 刷新列表数据
 */
function reloadGrid(){
	var store=Ext.getCmp("datas").getStore();
	store.getRootNode().remove();
	store.setRootNode({
		 text: "ROOT",
         id:entityId,
         expanded: true
	});
	//store.load({node:store.getRootNode()});//{node:store.getRootNode()}
	//var store=Ext.getCmp("datas").getStore();
	//store.getRootNode().removeAll();
	//store.load();//{node:store.getRootNode()}
	/* store.getRootNode().collapseChildren(function(){
		store.load({node:store.getRootNode(),callback:function(){
			lgxInfo(1);
		}});
	}); */ 
	
}
Ext.onReady(function(){
	entityId=entityId?entityId:0;

	Lingx.post("g",{e:entityCode},function(json){
		Ext.each(json.fields.list,function(obj,index,self){
			if(obj.comboType=='ref-display')textField=obj.code;
			if(obj.comboType=='ref-value')valueField=obj.code;
		});
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			region: 'west',
			//title: '对象区',
            split: true,
            autoScroll:true,
            border:false,
            width: 162,
            minWidth: 75,
            maxWidth: 200,
            margins: '0 0 0 0',
            contentEl:"Value-DIV"
		},{
			id:"datas",
			region: 'center',
            animate:false,
            border:false,
            autoScroll: true,
            rootVisible: false,
            selectedAndChecked:true,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: handlerJsp+"?c=orgtree_checkbox",
        	         reader: {
        	             type: 'json'
        	         },
        	     },
        	     root: {
        	         text: "ROOT",
        	         id:"0",
        	         expanded: true
        	    },
        	     autoLoad: true,
        	     listeners:{
        	    	datachanged:function(){
        	    		if(Ext.getCmp("datas")&&Ext.getCmp("datas").getSelectionModel().getCount()==0)
     		    		Ext.getCmp("datas").getSelectionModel().select(0);
     		    	}/* ,
     		    	boforeload:function(store, operation, eOpts){
     		    		console.log(store.parentNode);
     		    		store.parentNode=store.getRootNode();
     		    		console.log(store.parentNode);
     		    		///console.log();
     		    	} */
        	     }
        	}),
			xtype:"treepanel",
			/* dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	}], */
	        listeners:{
	        	itemclick:function(view,record,item,index,event,obj){
	        		addItem(record.data);
	        	}, 
	        	/* itemdblclick:function(view,record,item,index,event,obj){
	        		openViewWindow(entityCode,json.name,record.data.id);
	        	}, */
	        	checkchange:function( node, checked, eOpts ){
	        		if(checked){
	        			addItem(node.data);
	        		}else{
	        			
	        		}
	        	}
	        },
		}]
	});
	initItem();
	});
});
function initItem(){
	textField='text';//因为树型控件的显示值固定为text，所以有此设置；在列表多选里，没有这个限制
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
</head>
<body>
<div id="Value-DIV" style="text-align:center;">
</div>
</body>

</html>