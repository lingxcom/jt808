//以后的树型级联，不再有增删改查操作，自动生成查询
/**
 * 刷新列表数据
 */
 function reloadGrid(){
	 reloadMethod();
}
 function reloadMethod(){
	 var store=Ext.getCmp("datas").getStore();
		store.getRootNode().remove();
		store.setRootNode({
			 text: "ROOT",
	         id:entityId,
	         expanded: true
		});
 }
Ext.onReady(function(){

	//向下级联
	var tabitems=new Array();
	for(var i=0;i<grid_cascade.length;i++){
		var o=grid_cascade[i];
		//[{"code":"tuserorg","name":"用户组织","refField":"userid"},{"code":"tuserrole","name":"用户角色","refField":"userid"}];

		tabitems.push({
        	id:o.code,//e?e='+o.code+'&m=grid
        	html: "<iframe id='"+o.entity+o.method+"' where='"+o.where+"' scrolling='auto' frameborder='0' width='100%' height='100%' src=''> </iframe>",
            title:o.name,
            border:false,
            autoScroll: true,
			listeners:{
				activate:function(panel,eopts){
					//[{"code":"tuserorg","name":"用户组织","refField":"userid"},{"code":"tuserrole","name":"用户角色","refField":"userid"}];
	        		
	        		var record=Ext.getCmp("datas").getSelectionModel().getSelection()[0];
	        		if(!record)return;
	        		activeTab(record,panel);
				}
			}
        });
	}
	
	entityId=entityId?entityId:0;

	Lingx.post("g",{e:entityCode},function(json){
		Ext.each(json.fields.list,function(obj,index,self){
			if(obj.comboType=='ref-display')textField=obj.code;
			if(obj.comboType=='ref-value')valueField=obj.code;
		});
		//加入刷新按钮
		if(json.toolbar.length>0)
		json.toolbar.push({
			text:"刷新",
			iconCls:"Reload",
			handler:function(){
				window.location.reload();
			}
		});
		//json.toolbar.push({iconCls:"icon-reload",text:"刷新",handler:reloadMethod});
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			id:"datas",
			region:"center",
            animate:false,
            border:false,
            autoScroll: true,
            rootVisible: false,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: Lingx.urlAddParams("e?e="+entityCode+"&m=tree&lgxsn=1", params),//"+methodCode+"// Lingx.urlAddParams("e?e="+entityCode+"&m="+methodCode+"&lgxsn=1", params)
        	         reader: {
        	             type: 'json'
        	         }
        	     },
        	     root: {
        	         text: "ROOT",
        	         id:entityId,
        	         expanded: true
        	    },
        	     autoLoad: true,
        	     listeners:{
        	    	 datachanged:function(){
        	    		if(Ext.getCmp("datas")&&Ext.getCmp("datas").getSelectionModel().getCount()==0)
      		    		Ext.getCmp("datas").getSelectionModel().select(0);
      		    	}
         	     }
        	}),
			xtype:"treepanel",
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
	        	},select:function(view,record,item,index,event,obj){
	        		var tab=Ext.getCmp("tabpanel").getActiveTab();
	        		activeTab(record,tab);
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
	        }
		},{
			id:'tabpanel',
			region:'east',
			xtype:'tabpanel',
			activeTab: 0,
            split: true,
			//width:getCenterWidth()/2,
            width:"77%",
			items:tabitems
		}]
	});

	});
});
function activeTab(record,tab){
	//[{"code":"tuserorg","name":"用户组织","refField":"userid"},{"code":"tuserrole","name":"用户角色","refField":"userid"}];
	//var tab=Ext.getCmp("tabpanel").getActiveTab();
	var frame=Ext.get(tab.getEl()).query("iframe")[0];

	var entity="_none_";
	var field='_refId_';
	var rule="_none_";
	var method="_none_";
	for(var i=0;i<grid_cascade.length;i++){
		if((grid_cascade[i].entity+grid_cascade[i].method)==frame.id){
			entity=grid_cascade[i].entity;
			method=grid_cascade[i].method;
			
			if(grid_cascade[i].refField)
			field=grid_cascade[i].refField;
			
			if(grid_cascade[i].rule)
				rule=grid_cascade[i].rule;
			break;}
	}
	if(rule!="_none_")field='_refId_';//2015-10-08,当二级级联时，被级联对象也有属性指向主体时，特殊处理
	//frame.src='e?e='+entity+'&m='+method+'&ec=true&rule='+rule+'&'+field+'='+record.data.id+"&_refEntity_="+entityCode;//20160408
	if(frame.contentWindow&&frame.contentWindow.cascadeRefersh&&frame.contentWindow.resetExtParams){
		var opt={rule:rule,_refEntity_:entityCode};
		opt[field]=record.data.id;
		frame.contentWindow.cascadeRefersh(opt);
	}else{
		//frame.src='e?e='+entity+'&m='+method+'&ec=true&rule='+rule+'&'+field+'='+record.data.id+"&_refEntity_="+entityCode;
		var where=Ext.JSON.decode(frame.getAttribute("where"));
		var url='e?e='+entity+'&m='+method+'&ec=true&rule='+rule+'&'+field+'='+record.data.id+"&_refEntity_="+entityCode;
		frame.src=Lingx.urlAddParams(url, where);
	}
}