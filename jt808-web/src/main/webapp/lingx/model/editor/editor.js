	var currentEntityCode='';
	var currentEntityId='';
	var currentScriptId='';
	var extParams={};
	var textareaCodeCache='';
	var currentEntityIdCache='';
	var currentEntityCodeCache='';
	var isCheckbox=true;
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
    	            {name: 'modelId', type: 'string'},
    	            {name: 'name', type: 'string'},
    	            {name: 'code', type: 'string'}
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
    	         url: handlerJsp+'?c=getEntityList',
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
                height: 28,
                minSize: 50,
                maxSize: 100,
                items:{
                	xtype: 'toolbar',
                	border:false,
                	//height:60,
                	items: [
                	        
                	        {
                	            xtype: 'splitbutton',
                	            text : '创建',
                	            iconCls:'Add',
                	            handler:function(){
                	            	openWindow("创建表对象","lingx/model/editor/create/entity.jsp?pageid="+pageId);
                	            	//Lingx.getRootWindow().getApi().openWindow("创建对象","lingx/model/editor/create/auto_step1.jsp?pageid="+pageId);
                	            },
                	            menu:new Ext.menu.Menu({
                	            items:[
                	                   {text:'创建表对象',handler:function(e){
										openWindow("创建表对象","lingx/model/editor/create/entity.jsp?pageid="+pageId);
									   }},
                	                   {text:'创建视图对象',handler:function(e){
                	                	 	Lingx.getRootWindow().getApi().openWindow("创建视图对象","lingx/model/editor/create/view_entity.jsp?pageid="+pageId);
                            	            
                	                   }},
                	                   
                	                   /*
                	                   {text:'创建查询对象',handler:function(e){
                	            		openWindow("创建查询对象","lingx/model/editor/create/query_entity.jsp?pageid="+pageId);
                	                   }},
                	                   */
                	                   
                	                   {text:'创建克隆对象',handler:function(e){
                	                	   openWindow("创建克隆对象","lingx/model/editor/create/clone.jsp?code="+currentEntityCode);
                	                   }}]
                	            
                	            })
                	        },{
                	            xtype: 'button', // 默认的工具栏类型
                	            text: '删除',
                	            iconCls:'Delete',
                	            handler:function(){
                	            	if(!confirm("删除["+currentEntityCode+"]的对象与权限，确定删除吗？"))return;
                	            	
                	            	Lingx.post(handlerJsp,{c:'deleteEntity',code:currentEntityCode},function(json){
   									 lgxInfo(json.message);
   									 if(json.code==1){
   										// reloadCurrentTree();
   										entityStore.load();
   									 }
   								 });
                	            	//openCheck(node);
                	            	//node.set("checked",true);
                	            }
                	        },"-",{
                	            xtype: 'button', // 默认的工具栏类型
                	            text: '复选框',
                	            iconCls:'Image',
                	            handler:function(){
                	            	var node= Ext.getCmp(currentEntityCode).getRootNode();
                	            	if(isCheckbox){
                    	            	openCheck(node);
                    	            	isCheckbox=false;
                	            	}else{
                	            		closeCheck(node);
                    	            	isCheckbox=true;
                	            	}
                	            	//openCheck(node);
                	            	//node.set("checked",true);
                	            }
                	        },
                	       /* {
                	            text: '关闭复选框',
                	            handler:function(){
                	            	var node= Ext.getCmp(currentEntityCode).getRootNode();
                	            	closeCheck(node);
                	            }
                	        },*/
                	        {
   							 text:"上移",
   							 iconCls:"Arrowup",
							 handler:function(){

								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
								 if(node.data.id.length!=16){
									 lgxInfo("“"+node.data.text+"”该节点不可移动");
  									return;
								 }
								 Lingx.post(handlerJsp,{c:'move',code:currentEntityCode,id:node.data.id,type:-1},function(json){
									 lgxInfo(json.message);
									 if(json.code==1){
										// reloadCurrentTree();
									 }
								 });
							 }
						 },{
							 text:"下移",
   							 iconCls:"Arrowdown",
							 handler:function(){

								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
								 if(node.data.id.length!=16){
									 lgxInfo("“"+node.data.text+"”该节点不可移动");
  									return;
								 }
								 Lingx.post(handlerJsp,{c:'move',code:currentEntityCode,id:node.data.id,type:1},function(json){
									 lgxInfo(json.message);
									 if(json.code==1){
										// reloadCurrentTree();
									 }
								 });
							 }
						 }
						 ,{
							 text:"数据权限",
							 iconCls:'Applicationviewdetail',
							 handler:function(){
								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
								 if(node.data.text.indexOf('权限规则')==-1){
									 lgxInfo("请选择“权限规则”再进行规则编辑");
  									return;
								 }
								 var rule=getRule();
								 //console.log(rule);
								openWindow("规则编辑","lingx/model/editor/rule_editor.jsp?code="+currentEntityCode+"&id="+currentEntityId);
							 }
						 },{
							 text:"纵向权限",
							 iconCls:'Shading',
							 handler:function(){
								 openWindow2("纵向权限","lingx/model/editor/field_editor.jsp?code="+currentEntityCode);
								 // openWindow("纵向权限","e?e=trole_clone_field&m=grid&ec=true");
							 }
						 },{
							 text:'对象预览',
							 iconCls:'World',
							 handler:function(){
								 Lingx.post(handlerJsp,{c:"getDisplayModel",code:currentEntityCode},function(json){
									 openWindow2("对象预览","e?e="+currentEntityCode+"&m="+json.method);
								 });
								  
							 }
						 },
             	        //{ xtype: 'tbspacer', width: 50 }, // 添加一个50像素的空格
						 '-',
            	        {
							 text:'写入功能树',
							 iconCls:'Chartorganisation',
							 handler:function(){
								 openWindow2("写入功能树","lingx/model/editor/func_editor.jsp?code="+currentEntityCode);
								  
							 }
						 }
						 ,
						 {
							 text:'自定义查询',
							 iconCls:'Magnifierzoomin',
							 handler:function(){
								 openWindow2("自定义查询","lingx/model/editor/query_editor.jsp?code="+currentEntityCode);
								  
							 }
						 }
						 ,
						 '-',
						 {	text:"刷新",
							 iconCls:"Reload",
							 handler:function(){
								 window.location.reload();
							 }
							 },
                	        // 使用右对齐容器
                	        '->', // 等同 { xtype: 'tbfill' }
                	        /*{
                	            xtype    : 'textfield',
                	            name     : 'field1',
                	            emptyText: '输入搜索词[未处理]'
                	        },*/
                	        // 添加工具栏项之间的垂直分隔条
                	        {xtype: 'splitbutton',
            	            text : '参数配置',
            	            iconCls:'Outline',
            	            handler:function(){
            	            	openWindow2("参数配置","e?e=tlingx_config&m=grid");
            	            },
            	            menu:new Ext.menu.Menu({
            	            items:[ 
            	                    {iconCls:"Plugin",text: '插件管理',handler:function(){
                	        	openWindow2("插件管理","e?e=tlingx_plugin&m=grid");
                	        }}
                	        ,{iconCls:"Outline",text: '参数配置',handler:function(){
                	        	openWindow2("参数配置","e?e=tlingx_config&m=grid");
                	        }},
                	        '-', // 等同 {xtype: 'tbseparator'} 创建 Ext.toolbar.Separator
                	        ,{iconCls:"Chartline",text: '超管授权',handler:function(){
                	        	Lingx.post(handlerJsp,{c:"supermanAuth"},function(json){
                	        		lgxInfo(json.message);
                	        	});
                	        }},{iconCls:"Bin",text: '模型备份',handler:function(){
                	        	Lingx.post(handlerJsp,{c:"saveModelToDisk"},function(json){
                	        		lgxInfo(json.message);
                	        	});
                	        }},{
							 text:'执行SQL',
							 iconCls:'Databaseedit',
							 handler:function(){
								 openWindow("执行SQL","lingx/model/editor/sql_editor.jsp?code="+currentEntityCode);
								  
							 }
						 },{
							 text:"上传对象",
							 iconCls:'Tableadd',
							 handler:function(){
								 openWindow("上传对象","e?e=tlingx_tools&m=updateLingx");
							 }
						 },'-',{
							 text:"导入",
							 handler:function(){
								 openWindow("导入","lingx/model/update/download_step2.jsp");
							 }
						 },{
							 text:"导出",
							 handler:function(){
								 openWindowFull("导出","e?e=tlingx_tools&m=package");
							 }
						 }]
            	            })
            	            },
                	       // 等同 {xtype: 'tbtext', text: 'text1'} 创建 Ext.toolbar.TextItem
                	        { xtype: 'tbspacer' },// 等同 ' ' 创建 Ext.toolbar.Spacer
                	        {
      							 text:'技术文档',
      							 iconCls:'Pagewhitefont',
      							 handler:function(){
      								 openFullWindow("http://docs.lingx.com");
      							 }
      						 }
                	       
                	    ]
                },
                collapsible: false,
                //collapsed: true,
                //title: '工具栏区',
                margins: '2 2 2 2'
            }, {
                region: 'south',
                layout: 'border',
               // html:'south',
                split: true,
                height: 150,
                minSize: 100,
                maxSize: 300,
                collapsible: true,
                //collapsed: true,
                title: '代码编辑区',
                iconCls:"Pagewhitecode",
                margins: '0 2 2 2',
                items:[
                       {
                    	   region: 'center',
                    	   layout:"fit",
                    	   border:false,
                    	   items:[{
                    		   id:"script",
                    		   xtype:"textarea",
                    		   border:false,
                    		   disabled:true,
                    		   enableKeyEvents:true,
                    		   listeners:{
                    			   keyup:function(textarea){
                    				   textareaCodeCache=textarea.getValue();
                    				   currentEntityIdCache=currentEntityId;
                    				   currentEntityCodeCache=currentEntityCode;
                    			   },
                    			   blur:function(textarea){
                    				   if(textareaCodeCache)
                    				   Lingx.post(handlerJsp,{c:"saveProperty",code:currentEntityCodeCache,id:currentEntityIdCache,prop:"script",value:textareaCodeCache,oldvalue:""},function(json){
                    					   lgxInfo(json.message);
                    					   textareaCodeCache='';
                    					   currentEntityIdCache='';
                    					   currentEntityCodeCache='';
                    				   });
                    			   }
                    		   }
                    	   }]
                    	  // html:"API"
                       },{
                    	   region: 'east',
                    	   split: true,
                    	   border:false,
                           //collapsible: true,
                           width:300,
                           //title:"AAAA",
                           contentEl:"API"
                       }]
            }, {
                xtype: 'tabpanel',
                region: 'east',
                iconCls:"Applicationviewlist",
                title: '属性区',
              /*  dockedItems: [{
                    dock: 'top',
                    xtype: 'toolbar',
                    items: [ '->', {
                       xtype: 'button',
                       text: 'test',
                       tooltip: 'Test Button'
                    }]
                }],*/
                animCollapse: true,
                collapsible: true,
                split: true,
                width: 300, // give east and west regions a width
                minSize: 175,
                maxSize: 400,
                margins: '0 2 0 0',
                activeTab: 0,
                tabPosition: 'bottom',
                items: [Ext.create('Ext.grid.property.Grid', {
                		id:'propertyGrid',
                        title: '基本属性',
                        sortableColumns:false,
                        sortPropertyColumn:false,
                        listeners:{
                        	propertychange:function(source,recordId,value,oldValue,eOpts){
                        		Ext.Ajax.request({url:handlerJsp,method:"post",params:{c:"saveProperty",code:currentEntityCode,id:currentEntityId,prop:recordId,value:value,oldvalue:oldValue},success:function(res){
                        			var json=Ext.JSON.decode(res.responseText);
                        			lgxInfo(json.message);
                        			if(json.code==1){
                        				var o=Ext.getCmp(currentEntityCode).getSelectionModel().getSelection()[0];
                        				//lgxInfo(recordId+">"+o.data.text);
                        				var text=o.data.text;
                        				if('11_code'==recordId||'1_code'==recordId){

                        					var txt=text.substring(0,text.indexOf(' '))+" ["+value+"]";
                            				o.set('text',txt);
                        				}else if("22_name"==recordId||"2_name"==recordId){
                        					var txt="";
                        					if(text.indexOf(' ')==-1){
                        						txt=value;
                        					}else
                        					txt=value+text.substring(text.indexOf(' '));
                        					o.set('text',txt);
                        				}else if("1_name"==recordId){
                        					o.set('text',value);
                        				}else{

                        					//lgxInfo("未处理："+recordId);
                        					//o.set('text',value);
                        				}
                        			}
                        		}});
                        	}
                        }
                    })]
            }, {
                region: 'west',
                stateId: 'navigation-panel',
                id: 'west-panel', // see Ext.getCmp() below
                title: '对象区',
                iconCls:"Database",
                split: true,
                width: 240,
                minWidth: 175,
                maxWidth: 400,
                collapsible: true,
                animCollapse: true,
                margins: '0 0 0 2',
               
                	id: 'entityGrid_1',
                	stateId: 'entityGrid_2',
                    title: '对象区',
                   // iconCls: 'icon-7',
                    xtype:'grid',
                    //border:false,
            	   
                    columns: [{ xtype: 'rownumberer',width:26}, 
                              { header: '代码',  dataIndex: 'code' },
                              { header: '名称', dataIndex: 'name' }
                          ],
                    store: entityStore,
                    dockedItems: [{
            	        xtype: 'toolbar',
                        //border:false,
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
            		        		openWindow("编辑对象","e?e=tlingx_entity&m=edit&id="+record.data.id);
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
                    	select:function(view,record,item,index,event,obj){
                    		currentEntityCode=record.data.code;
                    		var tab=Ext.getCmp("tabpanel").queryById(record.data.code);
                    		if(tab){
                    			tab.show();
                    		}else{
                    		Ext.getCmp("tabpanel").add( {
                    			id:record.data.code,
                            	//html: record.data.code,
                                title: record.data.name,
                                closable: true,
                                animate:false,
                                autoScroll: true,
                                store: Ext.create('Ext.data.TreeStore', {
                            		proxy: {
                            	         type: 'ajax',
                            	         url: handlerJsp+'?c=getEntityTree&code='+record.data.code,
                            	         reader: {
                            	             type: 'json'
                            	         }
                            	     },
                            	     root: {
                            	         text: record.data.name+' ['+record.data.code+']',
                            	         id: record.data.modelId,
                            	         iconCls:'icon-1',
                            	         expanded: true
                            	    },
                            	     autoLoad: true
                            	}),
                    			xtype:"tree",
                    			listeners:{
                    				activate:function(panel,eopts){
                    					
                    					currentEntityCode=(panel.getId());
                    					this.getSelectionModel().deselectAll();
                    					this.getSelectionModel().select(0);
                    					
                    					var r=Ext.getCmp("entityGrid_1").getStore().findBy(function(record,id){
                    						if(record.data.code==currentEntityCode){
                    							Ext.getCmp("entityGrid_1").getSelectionModel().select(record);
                    						}
                    					});
                    					
                    				},
                    				itemcollapse:function(node,o){
                    					//lgxInfo(node.getPath());
                    					//EXPAND_PATH_ARRAY.
                    				},
                    				itemexpand:function(node,o){
                    					//EXPAND_PATH_ARRAY.push(node.getPath());
                    				},
                    				afterrender:function(){
                    					//var root=this.getRootNode();
                    					this.getSelectionModel().select(0);
                    					//console.log(this);
                    				},
                    				select:function(model,record,index,opts){
                    					var code=currentEntityCode;
                    					currentEntityId=record.data.id;
                    					Ext.Ajax.request({url:handlerJsp,params:{c:"getProperty",code:code,id:record.data.id},success:function(res){
                    						var json=Ext.JSON.decode(res.responseText);
                    						for(var temp in json.propAnno){
                    							var obj=json.propAnno[temp];
                    							if(obj.editor){
                    								obj.editor=eval(obj.editor);
                    							}
                    						}
                        					Ext.getCmp('propertyGrid').setSource(json.prop,json.propAnno);
                        					//console.log(Ext.getCmp('propertyGrid').getSource());
                    					}});
                    					if("脚本代码"==record.data.text||"查询脚本"==record.data.text){
                    						Lingx.post(handlerJsp,{c:"getScript",code:code,id:record.data.id},function(json){
                    							var textarea=Ext.getCmp("script");
                    							textarea.setValue(json.script);
                    							//textarea.setActive(true);
                    							textarea.setDisabled(false);
                    							currentScriptId=record.data.id;
                    							//lgxInfo(json.script);blur
                    						});
                    					}else{
                    						var textarea=Ext.getCmp("script");
                							textarea.setValue("");
                							textarea.setActive(false);
                							textarea.setDisabled(true);
                							currentScriptId='';
                    					}
                    				},
                    				 itemcontextmenu: function(menutree, record, items, index, e) { 
                 		                e.preventDefault();  
                 		                e.stopEvent();  
                    					 var menu=Ext.create("Ext.menu.Menu",{
                    						 floating : true,  
                    						 items:[{text:"刷新",
                    							 iconCls:'icon-reload',
                    							 //disabled:true,
                    							 handler:reloadCurrentTree
                    						 }, '-',{
                    							 text:"添加属性",
                    							 iconCls:'icon-2',
                    							 handler:function(){
                    								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								if(node.data.text!='属性'&&node.data.text!='查询参数'){
                    									lgxInfo("请选择“属性”分组添加属性");
                    									return;
                    								}
                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addField",code:currentEntityCode,id:node.data.id},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							//Ext.getCmp(currentEntityCode).getStore().reload(true);
                                							//Ext.getCmp(currentEntityCode).getStore().sync();
                                							//Ext.getCmp(currentEntityCode).getStore().reload(true);
                                    						//node.parentNode.expand(true,true);
                                							//console.log(node.parentNode);
                                							
                                							///node.store.reload();
                                							//node.expand();
                                							node.appendChild({id:json.id,iconCls:'icon-2',text:' []'});
                                						}
                                					}});
                    							 }
                    						 },{
                    							 text:"添加属性(全局)",
                    							 iconCls:'icon-2',
                    							 handler:function(){
                    								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								/*if(node.data.text!='属性'&&node.data.text!='查询参数'){
                    									lgxInfo("请选择“属性”分组添加属性");
                    									return;
                    								}*/
                    								var p1=prompt("字段代码","");
                    								var p2=prompt("该字段放于哪字段之下，空为放最后","");
                    								if(!p1){
                    									lgxInfo("字段代码不可为空");
                    									return;
                    								}
                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addFieldGlobal",code:currentEntityCode,id:node.data.id,fieldCode:p1,order:p2},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							reloadCurrentTree();
                                							//Ext.getCmp(currentEntityCode).getStore().reload(true);
                                							//Ext.getCmp(currentEntityCode).getStore().sync();
                                							//Ext.getCmp(currentEntityCode).getStore().reload(true);
                                    						//node.parentNode.expand(true,true);
                                							//console.log(node.parentNode);
                                							
                                							///node.store.reload();
                                							//node.expand();
                                							//node.appendChild({id:json.id,iconCls:'icon-2',text:' []'});
                                						}
                                					}});
                    							 }
                    						 },{
                    							 text:"添加方法",
                    							 iconCls:'icon-3',
                    							 handler:function(){
                    								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								if(node.data.text!='方法'){
                    									lgxInfo("请选择“方法”分组添加属性");
                    									return;
                    								}
                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addMethod",code:currentEntityCode,id:node.data.id},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							//Ext.getCmp(currentEntityCode).getStore().reload();

                                							node.appendChild({id:json.id,iconCls:'icon-3',text:' []'});
                                						}
                                					}}); 
                    							 }
                    						 },{
                    							 text:"添加验证器",
                    							 iconCls:'icon-4',
                    							 handler:function(){
                     								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                     								if(node.data.text!='验证器'){
                     									lgxInfo("请选择“验证器”分组添加属性");
                     									return;
                     								}
                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addValidator",code:currentEntityCode,id:node.data.id},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							node.appendChild({id:json.id,iconCls:'icon-4',text:'验证器'});
                                						}
                                					}}); 
                    							 }
                    						 },{
                    							 text:"添加解释器",
                    							 iconCls:'icon-4',
                    							 handler:function(){
                     								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                     								if(node.data.text!='解释器'){
                     									lgxInfo("请选择“解释器”分组添加属性");
                     									return;
                     								}

                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addInterpreter",code:currentEntityCode,id:node.data.id},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							node.appendChild({id:json.id,iconCls:'icon-4',text:'解释器'});
                                						}
                                					}}); 
                    							 }
                    						 },{
                    							 text:"添加执行器",
                    							 iconCls:'icon-5',
                    							 handler:function(){
                     								var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                     								if(node.data.text!='执行器'){
                     									lgxInfo("请选择“属性”分组添加属性");
                     									return;
                     								}

                    								Ext.Ajax.request({url:handlerJsp,params:{c:"addExecutor",code:currentEntityCode,id:node.data.id},success:function(res){
                                						var json=Ext.JSON.decode(res.responseText);
                                						lgxInfo(json.message);
                                						if(json.code==1){
                                							node.appendChild({id:json.id,iconCls:'icon-5',text:'表达式执行器'});
                                						}
                                					}}); 
                    							 }
                    						 }, '-',{
                    							 text:"复制",
                    							 handler:function(){
                    								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								 if(node.data.id.length!=16){
                    									 lgxInfo("“"+node.data.text+"”该节点不可复制");
                      									return;
                    								 }
                    								 var array=Ext.getCmp(currentEntityCode).getChecked();
                    								 if(array&&array.length>0){
                    									 var temp="";
                    									 for(var i=0;i<array.length;i++){
                    										 var n=array[i];
                    										 temp=temp+(n.data.id)+",";
                    									 }

                        								 Lingx.post(handlerJsp,{c:'copyEntity',code:currentEntityCode,ids:temp},function(json){
                        									 lgxInfo(json.message);
                        								 });
                    								 }else
                    								 Lingx.post(handlerJsp,{c:'copyEntity',code:currentEntityCode,ids:node.data.id},function(json){
                    									 lgxInfo(json.message);
                    								 });
                    							 }
                    						 },{
                    							 text:"粘贴",
                    							 handler:function(){
                    								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								 /*if(node.data.id.length==16){
                    									 lgxInfo("“"+node.data.text+"”该节点不可粘贴");
                      									return;
                    								 }*/
                    								 Lingx.post(handlerJsp,{c:'pasteEntity',code:currentEntityCode,id:node.data.id},function(json){
                    									 lgxInfo(json.message);
                    									 if(json.code==1){
                    										 //reloadCurrentTree();
                    									 }
                    								 });
                    							 }
                    						 },{
                    							 text:"上移",
                    							 handler:function(){

                    								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								 if(node.data.id.length!=16){
                    									 lgxInfo("“"+node.data.text+"”该节点不可移动");
                      									return;
                    								 }
                    								 Lingx.post(handlerJsp,{c:'move',code:currentEntityCode,id:node.data.id,type:-1},function(json){
                    									 lgxInfo(json.message);
                    									 if(json.code==1){
                    										// reloadCurrentTree();
                    									 }
                    								 });
                    							 }
                    						 },{
                    							 text:"下移",
                    							 handler:function(){

                    								 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                    								 if(node.data.id.length!=16){
                    									 lgxInfo("“"+node.data.text+"”该节点不可移动");
                      									return;
                    								 }
                    								 Lingx.post(handlerJsp,{c:'move',code:currentEntityCode,id:node.data.id,type:1},function(json){
                    									 lgxInfo(json.message);
                    									 if(json.code==1){
                    										// reloadCurrentTree();
                    									 }
                    								 });
                    							 }
                    						 },'-',{
                    							 text:"删除",
                    							 iconCls:'icon-delete',
                    							 handler:function(){
                    								 Ext.MessageBox.confirm("系统消息","确定删除吗？",function(a,b,c){
                    									 if(a=='yes'){
                    										 var node= Ext.getCmp(currentEntityCode).getSelectionModel().getLastSelected();
                             								if(node.data.id.length!=16){
                             									lgxInfo("该节点不可删除");
                             									return;
                             								}
                             							var array=Ext.getCmp(currentEntityCode).getChecked();
                           								 if(array&&array.length>0){
                           									 var temp="";
                           									 for(var i=0;i<array.length;i++){
                           										 var n=array[i];
                           										 temp=temp+(n.data.id)+",";
                           									 }
                               								 Lingx.post(handlerJsp,{c:'remove',code:currentEntityCode,id:temp},function(json){
                               									 lgxInfo(json.message);
                               								 });
                           								 }else
                    										 Ext.Ajax.request({url:handlerJsp,params:{c:"remove",code:currentEntityCode,id:node.data.id},success:function(res){
                                         						var json=Ext.JSON.decode(res.responseText);
                                         						lgxInfo(json.message);
                                         						if(json.code==1){
                                             						node.remove();
                                         						}
                                         					}}); 
                    									 }
                    								 });
                    							 }
                    						 }]
                    					 });
                    					 menu.showAt(e.getXY());
                    		            }

                    			}
                            }).show();
                    		}
                    	}
                    	
                    }
            	    
               
            },
            // in this instance the TabPanel is not wrapped by another panel
            // since no title is needed, this Panel is added directly
            // as a Container
            Ext.create('Ext.tab.Panel', {
            	id:'tabpanel',
                region: 'center', // a center region is ALWAYS required for border layout
                deferredRender: false,
                //activeTab: 0,     // first tab initially active
                items: []
            })]
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
		 var id=tree.getRootNode().data.id;
		 var text=tree.getRootNode().data.text;
		 var iconCls=tree.getRootNode().data.iconCls;
		 var store=tree.getStore();
			store.getRootNode().remove();
			store.setRootNode({
				 text: text,
		         id:id,
		         iconCls:iconCls,
		         expanded: true
			});
		 /*tree.collapseAll(function(){
			 tree.getStore().load({
				node: tree.getRootNode(),
				callback: function () {
					tree.expandPath('/00/01','id'); 
					tree.expandPath('/00/02','id'); 
					tree.expandPath('/00/03','id'); 
					tree.expandPath('/00/04','id'); 
					}
			});
		 });*/
}
function getRule(){
	var temp=Ext.getCmp("propertyGrid").getSource();
	return temp["z_dataRule"];
}
function setRule(text){
	var temp=Ext.getCmp("propertyGrid").getSource();
	temp["z_dataRule"]=text;
	Ext.getCmp("propertyGrid").setSource(temp);
}
function reloadGrid(){
	Ext.getCmp("entityGrid_1").getStore().load();
}