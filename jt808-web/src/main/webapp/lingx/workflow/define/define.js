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
    	            {name: 'name', type: 'string'},
    	            {name: 'code', type: 'string'},

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
    	         url: handlerJsp+'?c=getDefineList',
    	         reader: {
    	             type: 'json'
    	         }
    	     },
    	     autoLoad: true,
    	     listeners:{
    	    	 load:function(){
    	    		 //lgxInfo("load commpited");
    	    		 Ext.getCmp("entityGrid_1").getSelectionModel().select(0);
    	    	 },beforeload:function(){
 		    		Ext.apply(entityStore.proxy.extraParams,extParams);  
 		    	}
    	     }
    	 });
        Ext.QuickTips.init();

        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

        var viewport = Ext.create('Ext.Viewport', {
            id: 'border-example',
            layout: 'border',
            items: [{
                region: 'north',
                split: false,
                height: 54,
                minSize: 53,
                maxSize: 100,
                items:[{
                	xtype: 'toolbar',
                	border:false,
                	items: [
                	        
                	        {
                	        	iconCls:'Newred',
                	            xtype: 'button',
                	            text : '新建流程',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("新建流程","e?e=tlingx_wf_define&m=add&pageid="+pageId);
                	            }
                	        },{iconCls:'Arrowrefresh',
                	            xtype: 'button',
                	            text : '刷新列表',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        },'-'
   						 ,{iconCls:'Arrowup',
   							 text: '上移',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"top",num:-10},function(json){WF.reload();},"json");
   							 }
   						 },{iconCls:'Arrowdown',
   							 text: '下移',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"top",num:10},function(json){WF.reload();},"json");
   							 }
   						 },{iconCls:'Arrowleft',
   							 text: '左移',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"left",num:-10},function(json){WF.reload();},"json");}
   						 },{iconCls:'Arrowright',
   							 text: '右移',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"left",num:10},function(json){WF.reload();},"json");}
   						 },{
                	           // xtype: 'splitbutton',
               	            text : '事件设置',
               	            iconCls:'Pagewhitecode',
               	           
               	            menu:new Ext.menu.Menu({
               	            items:[{text:'保存事件',handler:function(e){
               	            		openWindow("保存事件定义","e?e=tlingx_wf_define&m=eventSaveSet&id="+WF.getDefineId());
               	            	}},
               	                   {text:'委托事件',disabled:true,handler:function(e){
               	                	 //	Lingx.getRootWindow().getApi().openWindow("创建视图对象","lingx/model/editor/create/view_entity.jsp?pageid="+pageId);
                           	            
               	                   }},
               	                   {text:'审批事件',disabled:true,handler:function(e){
               	                	  // openWindow("创建克隆对象","lingx/model/editor/create/clone.jsp?code="+currentEntityCode);
               	                   }}],
               	            
               	            })
               	        },"-",{
               	        	iconCls:"Basketput",
           	            	text:"导入",
           	            	handler:function(){
           	            		openWindow("导入流程","e?e=tlingx_wf_define&m=input");
           	            	}
           	            },{
               	        	iconCls:"Basketremove",
           	            	text:"导出",
           	            	handler:function(e){
           	            		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		//openWindow("编辑对象","e?e=tlingx_wf_define&m=edit&id="+record.data.id);
           	            		window.location.href="lingx/workflow/define/output.jsp?id="+record.data.id+"&code="+record.data.code;
           	            	}
           	            }
                	        ]
                	
                },{
                	xtype: 'toolbar',
                	border:false,
                	items: [
                	       /* 
                	        {
                	        	iconCls:'Newred',
                	            xtype: 'button',
                	            text : '新建流程',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("新建流程","e?e=tlingx_wf_define&m=add&pageid="+pageId);
                	            }
                	        }*/
/*                	        ,{
                	            xtype: 'button',
                	            text : '删除流程',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("删除流程","e?e=tlingx_wf_define&m=del&id="+record.data.id+"&pageid="+pageId);}
                	        }*/
                	        /*,{iconCls:'Arrowrefresh',
                	            xtype: 'button',
                	            text : '刷新列表',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        }*/,/*{
                	            xtype: 'button',
                	            text : '下载流程文件',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("下载流程文件","http://127.0.0.1:8080/lingx-web/lingx/workflow/define/handler.jsp?c=getDefineTaskAndLine&id="+record.data.id);
                	            	}
                	        },{
                	            xtype: 'button',
                	            text : '查看流程图片',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("查看流程图片","lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=image&pid="+record.data.id;
                	            	}
                	        },*/
             	        //{ xtype: 'tbspacer', width: 50 }, // 添加一个50像素的空格
						 
                	     {
                	         iconCls:'Tagblue',
							 text: '指针',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 pressed:true,
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(0);
								 }
             	            }
						 },{
							 iconCls:'Arrownwneswse',
							 text: '移动',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(1);
								 }
             	            }
						 },{iconCls:'Delete',
							 text: '删除',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(4);
								 }
             	            }
						 } ,'-'
						/* ,{
							 text: '任务框',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(2);
								 }
             	            }
						 }*/
						 ,{
							 iconCls:'Playgreen',
							 text: '开始节点',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(5);
								 }
             	            }
						 },{
							 iconCls:'Stop',
							 text: '结束节点',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(6);
								 }
             	            }
						 },{
							 iconCls:'Script',
							 text: '手动任务',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(2);
								 }
             	            }
						 },{
							 iconCls:'Scriptcode',
							 text: '自动任务',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(7);
								 }
             	            }
						 },{
							 iconCls:'Shapeungroup',
							 text: '会签任务',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(8);
								 }
             	            }
						 },{
							 iconCls:'Arrowswitchbluegreen',
							 text: '子流程任务',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(10);
								 }
             	            }
						 } ,'-'
						/* ,{
							 text: '连接线',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(3);
								 }
             	            }
						 }*/
						 ,{
							 iconCls:'Bullettick',
							 text: '前进连接线',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(3);
								 }
             	            }
						 },{
							 iconCls:'Bulletcross',
							 text: '后退连接线',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(9);
								 }
             	            }
						 }
/*						 ,'-',{
							 text: '显示连接线',
							 enableToggle:true,
							 pressed:true,
							 handler:function(btn,pressed,options){
								WF.setDisplayLine(btn.pressed);
								WF.reload();
             	            }
						 }
						 */
						 
                	       /* // 使用右对齐容器
                	        '->', // 等同 { xtype: 'tbfill' }
                	        {
                	            xtype    : 'textfield',
                	            name     : 'field1',
                	            emptyText: '输入搜索词'
                	        },
                	        // 添加工具栏项之间的垂直分隔条
                	        '-', // 等同 {xtype: 'tbseparator'} 创建 Ext.toolbar.Separator
                	        'text 1', // 等同 {xtype: 'tbtext', text: 'text1'} 创建 Ext.toolbar.TextItem
                	        { xtype: 'tbspacer' },// 等同 ' ' 创建 Ext.toolbar.Spacer
                	        'text 2'*/
                	    ]
                }],
                collapsible: false,
                //collapsed: true,
                //title: '工具栏区',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                title: '流程区',
                split: true,
                width: 240,
                minWidth: 175,
                maxWidth: 400,
                collapsible: true,
                animCollapse: true,
                margins: '0 0 0 2',

            	id: 'entityGrid_1',
            	stateId: 'entityGrid_2',
                title: '定义流程',
                iconCls: 'icon-7',
                xtype:'grid',

                dockedItems: [{
        	        xtype: 'toolbar',
        	        items: [
        	                {id:"text", xtype: 'textfield',width:140, emptyText: '查询条件',listeners:{
        	                	specialkey: function(field, e){
        	                		if(e.getKey()== e.ENTER){
        	                			extParams={param:Ext.getCmp("text").getValue()};
                	                	entityStore.load();
        	                		}
        	                	}
        	                }},
        	                { xtype: 'button', text: '查询' ,handler:function(){
        	                	extParams={param:Ext.getCmp("text").getValue()};
        	                	entityStore.load();
        	                },listeners:{
        							specialkey: function(field, e){
        			                    if (e.getKey() == e.ENTER) {
        			                    	extParams={param:Ext.getCmp("text").getValue()};
                    	                	entityStore.load();
        			                    }
        			                }
        					}},
        	                { xtype: 'button', text: '编辑' ,handler:function(){
        		        		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		openWindow("编辑对象","e?e=tlingx_wf_define&m=edit&id="+record.data.id);
        	                }}
        	            ],
        	        dock: 'top',
        	        displayInfo: true
        	    }],
                columns: [{ xtype: 'rownumberer'}, 
                          { header: '代码',  dataIndex: 'code' },
                          { header: '名称', dataIndex: 'name' }
                      ],
                store: entityStore,
              /*
                dockedItems: [{
        	        xtype: 'toolbar',
                    border:false,
        	        items: [
        	                {id:"text", xtype: 'textfield', emptyText: '查询条件'},
        	                { xtype: 'button', text: '查询' ,handler:function(){
        	                	extParams={param:Ext.getCmp("text").getValue()};
        	                	entityStore.load();
        	                }},
        	                { xtype: 'button', text: '编辑' ,handler:function(){
        		        		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		openWindow("编辑对象","e?e=tsysentity&m=edit&id="+record.data.id);
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
                ],*/
                listeners:{
                	select:function(view,record,item,index,event,obj){
                		WF.setDefineId(record.data.id);
                		WF.reload();
                		
                	}
                	
                }
        	    
            },
            { region: 'center',
            	title:"流程图",
            	html:"<div id='pad' style='padding:0px;margin:0px;width:100%;height:100%;'></div>"}]
        });
        
/*        Ext.getCmp("entityGrid_1").on("itemdblclick",function(view,record,item,index,event,obj){
	    	alert(index);
	    });*/
    });
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
	Ext.getCmp("entityGrid_1").getStore().load();
}