
var toolbars=[];//工具栏，可多行
var searchFieldCache=[];//查询字段ID缓存

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
 * 数据查询
 */
function lingxSearch(array){
	extParams=resetExtParams();
	var paramNumber=0;
	for(var i=0;i<array.length;i++){
		var temp=array[i];
		for(var t in temp){
			extParams[t]='_'+temp[t];
			console.log(temp[t])
			if(temp[t])paramNumber++;
		}
	}
	//extParams['page']=1;
	//reloadGrid();
	
	if(paramNumber>1){
	Ext.getCmp("datas").getStore().loadPage(1);
	}else{
	//reloadGrid();
	window.location.reload();
	}
}


function search(){
	var arr=[];
	for(var i=0;i<searchFieldCache.length;i++){
		var id=searchFieldCache[i],val;
		var temp={};
		val=Ext.getCmp("id-search-"+id).getValue();
		var xtype=Ext.getCmp("id-search-"+id).getXType();
		if(typeof val =="object"){
			val=""+fdate(val,xtype);

			temp[id]=val||"";
			arr.push(temp);
		}else{

			temp[id]=val||"";
			arr.push(temp);
		}
		
			
	}
	arr.push({isGridSearch:true});
	lingxSearch(arr);
}


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
		
json.toolbar=[];//树型级联操作清空
//查询处理
		var tool=[];
		if(json.queryParams.length==0&&json.GridConfig.queryField){
			
			for(var i=0;i<json.fields.list.length;i++){
				var field=json.fields.list[i];
				if(params[field.code])continue;//当外部传参进来就不需要这个查询字段了
				if((","+json.GridConfig.queryField+",").indexOf(","+field.code+",")>=0){
					searchFieldCache.push(field.code);
					tool.push(field.name+":");
					if(field.refEntity){
						var store=null;
						if("tlingx_optionitem"==field.refEntity){
							store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&issearch=1&code='+field.inputOptions,reader:{type:'json'}}),
								autoLoad:false});
						}else{
							store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+field.refEntity+'&m=combo&lgxsn=1&issearch=1',reader:{type:'json'}}),
								autoLoad:false});
						}
						tool.push({
							id:"id-search-"+field.code,
							xtype    : 'combobox',
		    	            name     : field.code,
		    	            //emptyText: field.name,
		    	            store:store,
								displayField:"text",
								valueField:"value",
		    	            width:100,listeners:{
	    	                	specialkey: function(field, e){
	    	                		if(e.getKey()== e.ENTER){
	    	                			search();
	    	                		}
	    	                	}
	    	                }
		
						});
					}else{
						tool.push({
							id:"id-search-"+field.code,
							xtype    : 'textfield',
		    	            name     : field.code,
		    	            //emptyText: field.name,
		    	            width:100,listeners:{
	    	                	specialkey: function(field, e){
	    	                		if(e.getKey()== e.ENTER){
	    	                			search();
	    	                		}
	    	                	}
	    	                }
		
						});
					}
					
				}
			}
			tool.push({
				text : fieldNames[0]||"查询",
				iconCls:"icon-search",
				handler : function() {
					search();
				}

			});
			if(json.isSearch){
				tool.push("->");
				tool.push({iconCls:'Zoom',text:fieldNames[1]||"高级",handler:function(){
					openWindow4(fieldNames[1]||"高级查询","e?e="+json.code+"&m=search");
				}});
			}
				/*
			toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true
			});*/
		}else if(json.queryParams.length>0){
			var tool=[];
			for(var i=0;i<json.queryParams.length;i++){
				var obj=json.queryParams[i];
				if(params[obj.code])continue;//当外部传参进来就不需要这个查询字段了
				var w=100;
				var store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:obj.url,reader:{type:'json'}}),
					autoLoad:false});
				searchFieldCache.push(obj.code);
				tool.push(obj.name+":");
				var options111={
						id:"id-search-"+obj.code,
						xtype    : obj.xtype,
			            name     : obj.code,
			            //emptyText: obj.name,
			            store:store,
			            displayField:"text",
						valueField:"value",
			           format:"Y-m-d H:i:s",
						altFormats:'Y-m-d H:i:s|m.d.Y',
			            width:w,listeners:{
		                	specialkey: function(field, e){
		                		if(e.getKey()== e.ENTER){
		                			search();
		                		}
		                	}
		                }

					};
				if(obj.xtype=="datetimefield"){
					options111.width=150;
				}else if(obj.xtype=="datefield"){
					options111.width=100;
					options111.format="Y-m-d";
					options111.altFormats='Y-m-d';
				}
				tool.push(options111);
			}
			
			tool.push({
				text : fieldNames[0]||"查询",
				iconCls:"icon-search",
				handler : function() {
					search();
				}

			});
			/*toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true
			});*/
		}
		if(LingxOpertor=="input"){//当打开方式为选择对框话时，不显示操作按钮；且双击变选择
		json.toolbar=[];
		}
		if(json.GridConfig.toolbarSingle){
			if(json.toolbar.length>0){
				tool=tool.concat(json.toolbar);
			}
			if(tool.length>0)
			toolbars.push({
		        xtype: 'toolbar',
		        items:tool,//json.toolbar,
		        dock: 'top',
		        displayInfo: true
		        });
		}else{
			if(tool.length>0)
			toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true
			});
			if(json.toolbar.length>0)
			toolbars.push({
		        xtype: 'toolbar',
		        items:json.toolbar,
		        dock: 'top',
		        displayInfo: true
		        });
		}
		//查询处理 end
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
        	     beforeload:function(){
        	     	//if(window.storeTree)
		    		Ext.apply(this.proxy.extraParams,extParams);  
		    	},
        	    	 datachanged:function(){
        	    		if(Ext.getCmp("datas"))//&&Ext.getCmp("datas").getSelectionModel().getCount()==0
      		    		Ext.getCmp("datas").getSelectionModel().select(0);
      		    	}
         	     }
        	}),
			xtype:"treepanel",
			dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:tool,//json.toolbar,
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