	var currentEntityCode='';
	var currentEntityId='';
	var currentScriptId='';
	var extParams={};
	
    Ext.onReady(function() {
    	
    	 Ext.define('Option',{
 	        extend: 'Ext.data.Model',
 	        fields: [
 	            {name: 'text', type: 'string'},
 	            {name: 'value', type: 'string'}
 	        ]
 	    });
    	 Ext.define('Entity',{
    	        extend: 'Ext.data.Model',
    	        fields: [
    	    	    {name: 'workflow', type: 'string'},
    	            {name: 'name', type: 'string'},
    	            {name: 'user_name', type: 'string'},
    	            {name: 'task', type: 'string'},
    	            {name: 'date', type: 'string'},
    	            {name: 'code', type: 'string'},
    	            {name:'processInstanceId',type:'string'},
    	            {name: 'dId', type: 'string'}
    	        ]
    	    });
    	
    	 var entityStore=Ext.create('Ext.data.Store', {
    	     model: 'Entity',
    	     
    	     proxy: {
    	    	 actionMethods: {
 	                create : 'POST',
 	                read   : 'POST', // by default GET
 	                update : 'POST',
 	                destroy: 'POST'
 	            },
    	         type: 'ajax',
    	         url: handlerJsp+'?c=getWorkflowListByDaiban',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		 //lgxInfo("load commpited");
    	    		// Ext.getCmp("entityGrid_1").getSelectionModel().select(0);
    	    	 },beforeload:function(){
 		    		//Ext.apply(entityStore.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });
    	 
    	 var entityStore2=Ext.create('Ext.data.Store', {
    	     model: 'Entity',
    	     
    	     proxy: {
    	    	 actionMethods: {
 	                create : 'POST',
 	                read   : 'POST', // by default GET
 	                update : 'POST',
 	                destroy: 'POST'
 	            },
    	         type: 'ajax',
    	         url: handlerJsp+'?c=getWorkflowListByDL',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		
    	    	 },beforeload:function(){
 		    		//Ext.apply(entityStore2.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });

    	 var entityStore3=Ext.create('Ext.data.Store', {
    	     model: 'Entity',
    	     
    	     proxy: {
    	    	 actionMethods: {
 	                create : 'POST',
 	                read   : 'POST', // by default GET
 	                update : 'POST',
 	                destroy: 'POST'
 	            },
    	         type: 'ajax',
    	         url: handlerJsp+'?c=getWorkflowListByFQ',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		
    	    	 },beforeload:function(){
 		    		//Ext.apply(entityStore2.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });
    	 var entityStore4=Ext.create('Ext.data.Store', {
    	     model: 'Entity',
    	     
    	     proxy: {
    	    	 actionMethods: {
 	                create : 'POST',
 	                read   : 'POST', // by default GET
 	                update : 'POST',
 	                destroy: 'POST'
 	            },
    	         type: 'ajax',
    	         url: handlerJsp+'?c=getWorkflowListByXG',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		
    	    	 },beforeload:function(){
 		    		//Ext.apply(entityStore2.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });

    	 var entityStore5=Ext.create('Ext.data.Store', {
    	     model: 'Entity',
    	     
    	     proxy: {
    	    	 actionMethods: {
 	                create : 'POST',
 	                read   : 'POST', // by default GET
 	                update : 'POST',
 	                destroy: 'POST'
 	            },
    	         type: 'ajax',
    	         url: handlerJsp+'?c=getWorkflowListByCC',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		
    	    	 },beforeload:function(){
 		    		//Ext.apply(entityStore2.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });
       // Ext.QuickTips.init();

       // Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

        var viewport = Ext.create('Ext.Viewport', {
            id: 'border-example',
            layout: 'border',
            border:false,
            items: [{
                region: 'west',
                stateId: 'navigation-panel',
                id: 'west-panel', // see Ext.getCmp() below
                title: '流程列表',
                split: true,
                width: 340,
                minWidth: 100,
                maxWidth: 600,
                collapsible: true,
                animCollapse: true,
               margins: '2 0 2 2',
                layout: 'accordion',
                items: [{
                	id: 'entityGrid_1',
                    title: '待办任务',
                    iconCls:'Pageedit',
                    xtype:'grid',
                    border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '工作流程',  dataIndex: 'workflow',width:120},
                              { header: '当前节点', dataIndex: 'name' },
                              { header: '发起人', dataIndex: 'user_name' }
                          ],
                    store: entityStore,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        border:false,
            	        items: [

            	                { xtype: 'button', text: '办理' ,iconCls:'Pagewhiteedit',handler:function(){
            		        		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要处理的任务");
            		        			return;
            		        		}
            		        		openFullWindow("w?m=form&_TASK_ID="+record.data.id);
            	                }},
								
								{xtype: 'button',text: '刷新',iconCls:'icon-reload',handler:function(){
									reloadAll();//Ext.getCmp("entityGrid_1").getStore().reload();
								}},
								{xtype: 'button',text: '删除',iconCls:'Delete',handler:function(){
									var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要处理的任务");
            		        			return;
            		        		}else{
            		        			if(confirm("确认删除吗？")){
            		        				Lingx.post("e?e=tlingx_wf_instance&m=del",{lgxsn:1,id:record.data.processInstanceId},function(json){
            		        					lgxInfo(json.message);
            		        					reloadAll();
            		        				});
            		        			}
            		        		}
								}},
								{xtype: 'button',text: '新流程',iconCls:'New',handler:function(){
									var frame=document.getElementById("INDEX_FRAME");
									frame.src="lingx/workflow/center/index.jsp";
								}}
            	            ],
            	        dock: 'top',
            	        displayInfo: true
            	    }
                    //,{
                    //    	        xtype: 'pagingtoolbar',
                    //    	        store: entityStore,   // GridPanel使用相同的数据源
                    //    	        dock: 'bottom',
                    //    	        displayInfo: true
                    //    	    }
                    ],
                    listeners:{

        	        	afterrender:function(panel){
        	        		Lingx.post(handlerJsp,{c:"centerInit"},function(json){
        	        			Ext.getCmp("entityGrid_1").setTitle('待办任务('+json.db+')');
        	        			Ext.getCmp("entityGrid_2").setTitle('待领任务('+json.dl+')');
        	        			Ext.getCmp("entityGrid_3").setTitle('我的流程('+json.fq+')');
        	        			Ext.getCmp("entityGrid_4").setTitle('已办事宜('+json.xg+')');
        	        			Ext.getCmp("entityGrid_5").setTitle('抄送任务('+json.cc+')');
        	        			//lgxInfo(json.dl);
        	        		});
        	        	},
                    	select:function(view,record,item,index,event,obj){
                    		var frame=document.getElementById("INDEX_FRAME");
                    		frame.src="lingx/workflow/center/main.jsp?taskid="+record.data.id+"&processInstanceId="+record.data.processInstanceId;
                    	},
                    	itemdblclick:function(view,record,item,index,event,obj){
                    		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
    		        		if(!record){
    		        			lgxInfo("请选择要处理的任务");
    		        			return;
    		        		}
    		        		openFullWindow("w?m=form&_TASK_ID="+record.data.id);
                    	}
                    	
                    }
            	    
                },{

                	id: 'entityGrid_2',
                    title: '待领任务',
                    iconCls: 'Pagekey',
                    xtype:'grid',
                    border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '工作流程',  dataIndex: 'workflow',width:120 },
                              { header: '当前节点', dataIndex: 'name' },
                              { header: '发起人', dataIndex: 'user_name' }
                          ],
                    store: entityStore2,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        border:false,
            	        items: [
/*            	                {id:"text", xtype: 'textfield', emptyText: '查询条件'},
            	                { xtype: 'button', text: '查询' ,handler:function(){
            	                	extParams={param:Ext.getCmp("text").getValue()};
            	                	entityStore.load();
            	                }},*/
								
								{xtype: 'button',text: '签收',iconCls:'Pagewhiteput',handler:function(){
									var record=Ext.getCmp("entityGrid_2").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要签收的任务");
            		        			return;
            		        		}
									if(!confirm("确定签收该任务["+record.data.name+"]吗？"))return;
									//openFullWindow("w?m=claim&_TASK_ID="+record.data.id);
									Lingx.post("w",{m:"claim",_TASK_ID:record.data.id},function(json){
										alert(json.message);
										reloadAll();
									},"json");
            	                }},
            	                {xtype: 'button',text: '查看',iconCls:'Pagewhitetext',handler:function(){
									var record=Ext.getCmp("entityGrid_2").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要查看的任务");
            		        			return;
            		        		}
            		        		openFullWindow("w?m=view&_TASK_ID="+record.data.id);
								}},
								{xtype: 'button',text: '刷新',iconCls:'icon-reload',handler:function(){
									reloadAll();//Ext.getCmp("entityGrid_2").getStore().reload();
								}}
            	        
            	            ],
            	        dock: 'top',
            	        displayInfo: true
            	    }

                    ],
                    listeners:{
                    	select:function(view,record,item,index,event,obj){
                    		var frame=document.getElementById("INDEX_FRAME");
                    		frame.src="lingx/workflow/center/main.jsp?taskid="+record.data.id+"&processInstanceId="+record.data.processInstanceId;
                    	},
                    	itemdblclick:function(view,record,item,index,event,obj){
                    		var record=Ext.getCmp("entityGrid_2").getSelectionModel().getSelection()[0];
    		        		if(!record){
    		        			lgxInfo("请选择要签收的任务");
    		        			return;
    		        		}
							if(!confirm("确定签收该任务["+record.data.name+"]吗？"))return;
							//openFullWindow("w?m=claim&_TASK_ID="+record.data.id);
							Lingx.post("w",{m:"claim",_TASK_ID:record.data.id},function(json){
								alert(json.message);
								reloadAll();
							},"json");
                    	}
                    	
                    }
                },{

                	id: 'entityGrid_5',
                    title: '抄送任务',
                    iconCls: 'Pageattach',
                    xtype:'grid',
                    border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '工作流程',  dataIndex: 'workflow',width:120 },
                              { header: '当前节点', dataIndex: 'name' },
                              { header: '发起人', dataIndex: 'user_name' }
                          ],
                    store: entityStore5,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        border:false,
            	        items: [
            	                
            	                {xtype: 'button',text: '查看',iconCls:'Pagewhitetext',handler:function(){
									var record=Ext.getCmp("entityGrid_5").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要查看的任务");
            		        			return;
            		        		}
            		        		openFullWindow("w?m=view&_TASK_ID="+record.data.id);
								}},
								{xtype: 'button',text: '刷新',iconCls:'icon-reload',handler:function(){
									reloadAll();//Ext.getCmp("entityGrid_2").getStore().reload();
								}}
            	        
            	            ],
            	        dock: 'top',
            	        displayInfo: true
            	    }

                    ],
                    listeners:{
                    	select:function(view,record,item,index,event,obj){
                    		var frame=document.getElementById("INDEX_FRAME");
                    		frame.src="lingx/workflow/center/main.jsp?taskid="+record.data.id+"&processInstanceId="+record.data.processInstanceId;
                    	}
                    	
                    }
                }, {


                	id: 'entityGrid_3',
                    title: '发起流程',
                    iconCls: 'Pageforward',
                    xtype:'grid',
                    border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '工作流程',  dataIndex: 'workflow',width:120 },
                              { header: '当前节点', dataIndex: 'task' },
                              { header: '发起日期', dataIndex: 'name',renderer:function(value,p,record){
          			            
      			            	return Lingx.formatTime8(value);
      			            } }
                          ],
                    store: entityStore3,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        border:false,
            	        items: [
/*            	                {id:"text", xtype: 'textfield', emptyText: '查询条件'},
            	                { xtype: 'button', text: '查询' ,handler:function(){
            	                	extParams={param:Ext.getCmp("text").getValue()};
            	                	entityStore.load();
            	                }},*/
								{xtype: 'button',text: '查看',iconCls:'Pagewhitetext',handler:function(){
									var record=Ext.getCmp("entityGrid_3").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要查看的任务");
            		        			return;
            		        		}
            		        		openFullWindow("w?m=view&_TASK_ID="+record.data.id);
            	                }},
								{xtype: 'button',text: '刷新',iconCls:'icon-reload',handler:function(){
									reloadAll();//Ext.getCmp("entityGrid_3").getStore().reload();
								}}
            	            ],
            	        dock: 'top',
            	        displayInfo: true
            	    }

                    ],
                    listeners:{
                    	select:function(view,record,item,index,event,obj){
                    		var frame=document.getElementById("INDEX_FRAME");
                    		frame.src="lingx/workflow/center/main.jsp?taskid="+record.data.id+"&processInstanceId="+record.data.processInstanceId;
                    	}
                    	
                    }
                }, {


                	id: 'entityGrid_4',
                    title: '已办事宜',
                    iconCls: 'Pagemagnify',
                    xtype:'grid',
                    border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '工作流程',  dataIndex: 'workflow',width:120 },
                              { header: '当前节点', dataIndex: 'name'},
                              { header: '发起人', dataIndex: 'user_name' }
                          ],
                    store: entityStore4,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        border:false,
            	        items: [
/*            	                {id:"text", xtype: 'textfield', emptyText: '查询条件'},
            	                { xtype: 'button', text: '查询' ,handler:function(){
            	                	extParams={param:Ext.getCmp("text").getValue()};
            	                	entityStore.load();
            	                }},*/
								{xtype: 'button',text: '查看',iconCls:'Pagewhitetext',handler:function(){
									var record=Ext.getCmp("entityGrid_4").getSelectionModel().getSelection()[0];
            		        		if(!record){
            		        			lgxInfo("请选择要查看的任务");
            		        			return;
            		        		}
            		        		openFullWindow("w?m=view&_TASK_ID="+record.data.id);
            	                }},
								{xtype: 'button',text: '刷新',iconCls:'icon-reload',handler:function(){
									reloadAll();//Ext.getCmp("entityGrid_4").getStore().reload();
								}}
            	            ],
            	        dock: 'top',
            	        displayInfo: true
            	    }

                    ],
                    listeners:{
                    	select:function(view,record,item,index,event,obj){
                    		var frame=document.getElementById("INDEX_FRAME");
                    		frame.src="lingx/workflow/center/main.jsp?taskid="+record.data.id+"&processInstanceId="+record.data.processInstanceId;
                    	}
                    	
                    }
                }
                /*, {
                    title: '参与工作流程',
                    html: '<p>Some info in here.</p>',
                    iconCls: 'icon-7'
                }*/
                ]
            },{
            	id:"INDEX",
            	region:"center",
                margins: '2 2 2 0',
            	title:"工作区域",
            	html: '<iframe id="INDEX_FRAME" scrolling="auto" frameborder="0" width="100%" height="100%" src="lingx/workflow/center/index.jsp"> </iframe>',
            	autoScroll: true
            }]
        });
        
/*        Ext.getCmp("entityGrid_1").on("itemdblclick",function(view,record,item,index,event,obj){
	    	alert(index);
	    });*/
    });
    
    function reloadAll(){
    	Ext.getCmp("entityGrid_1").getStore().reload();
    	Ext.getCmp("entityGrid_2").getStore().reload();
    	Ext.getCmp("entityGrid_3").getStore().reload();
    	Ext.getCmp("entityGrid_4").getStore().reload();
    	Ext.getCmp("entityGrid_5").getStore().reload();
    	Lingx.post(handlerJsp,{c:"centerInit"},function(json){
			Ext.getCmp("entityGrid_1").setTitle('待办任务('+json.db+')');
			Ext.getCmp("entityGrid_2").setTitle('待领任务('+json.dl+')');
			Ext.getCmp("entityGrid_3").setTitle('我的流程('+json.fq+')');
			Ext.getCmp("entityGrid_4").setTitle('已办事宜('+json.xg+')');
			Ext.getCmp("entityGrid_5").setTitle('抄送任务('+json.cc+')');
			//lgxInfo(json.dl);
		});
    }
function openCheck(node){
	if(node.data.id.length==16)node.set("checked",false);
	for(var i=0;i< node.childNodes.length;i++){
		openCheck(node.childNodes[i]);
	}
}
function closeCheck(node){
	if(node.data.id.length==16)node.set("checked",null);
	for(var i=0;i< node.childNodes.length;i++){
		closeCheck(node.childNodes[i]);
	}
}
function reloadCurrentTree(){
		 var tree=Ext.getCmp(currentEntityCode);
		 tree.collapseAll(function(){
			 tree.getStore().load({
				node: tree.getRootNode(),
				callback: function () {
					tree.expandPath('/00/01','id'); 
					tree.expandPath('/00/02','id'); 
					tree.expandPath('/00/03','id'); 
					tree.expandPath('/00/04','id'); 
					}
			});
		 });
}
function getRule(){
	var temp=Ext.getCmp("propertyGrid").getSource();
	return temp["z_rule"];
}
function setRule(text){
	var temp=Ext.getCmp("propertyGrid").getSource();
	temp["z_rule"]=text;
	Ext.getCmp("propertyGrid").setSource(temp);
}
function reloadGrid(){
	 reloadAll();
}