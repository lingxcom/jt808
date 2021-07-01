
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
	//var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	var array=Ext.getCmp("datas").getChecked();
	if(array&&array.length>0){
		var obj,val="",txt="";
		for(var i=0;i<array.length;i++){
			obj=array[i];
			val=val+obj.data[valueField]+",";
			txt=txt+obj.data["text"]+",";
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
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: "e?e="+entityCode+"&m="+methodCode+"&checkbox=1&lgxsn=1",
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
        	    	datachanged:function(){
        	    		if(Ext.getCmp("datas").getSelectionModel().getCount()==0)
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
			xtype:"tree",
			dockedItems: [{
	    	        xtype: 'toolbar',
	    	        //items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	}],
	        listeners:{
	        	itemdblclick:function(view,record,item,index,event,obj){
	        		openViewWindow(entityCode,json.name,record.data.id);
	        	},
	        	checkchange:function( node, checked, eOpts ){
	        		if(checked){
	        			addItem(node.data);
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
	$("#Value-DIV").prepend(div);
}
function delItem(el){
	var p=$(el).parent();
	p.empty();
	p.remove();
	delete p;
}