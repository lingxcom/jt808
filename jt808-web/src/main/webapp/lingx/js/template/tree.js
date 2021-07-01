
/**
 * 数据提交
 */
function lingxSubmit(){
	if(!fromPageId){closeWindow();return;}
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj=array[0];
		var val=obj.data[valueField];
		var txt=obj.data["text"];//textField
		var win=getFromWindow(fromPageId);
		//lgxInfo(textField);
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
		layout:'fit',
		items:[{
			id:"datas",
            animate:false,
            border:false,
            autoScroll: true,
            rootVisible: false,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: Lingx.urlAddParams("e?e="+entityCode+"&m="+methodCode+"&lgxsn=1", params),
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
	    	        items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	}],
	        listeners:{
	        	itemdblclick:function(view,record,item,index,event,obj){
	        		//openViewWindow(entityCode,json.name,record.data.id);
	        		var dblclickMethod=json.GridConfig.dblclickMethod||"view";
	        		if(dblclickMethod=="view"){
	        			openViewWindow(entityCode,json.name,record.data.id);
	        		}else{
	        			openWindow(json.name,"e?e="+entityCode+"&m="+dblclickMethod+"&id="+record.data.id);
	        		}
	        	},
	        	itemcontextmenu: function(view, record, item, index, e) { 
	        		if(json.rightmenu.length>0){
	        			e.preventDefault();  
  		                e.stopEvent();  
  		                var menu=Ext.create("Ext.menu.Menu",{
     						 floating : true,  
     						 items:json.rightmenu
  		                });
  		                menu.showAt(e.getXY());
	        		}
		                
		            }
	        },
		}]
	});

	});
});