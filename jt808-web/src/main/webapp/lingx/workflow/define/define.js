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
                	            text : '????????????',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","e?e=tlingx_wf_define&m=add&pageid="+pageId);
                	            }
                	        },{iconCls:'Arrowrefresh',
                	            xtype: 'button',
                	            text : '????????????',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        },'-'
   						 ,{iconCls:'Arrowup',
   							 text: '??????',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"top",num:-10},function(json){WF.reload();},"json");
   							 }
   						 },{iconCls:'Arrowdown',
   							 text: '??????',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"top",num:10},function(json){WF.reload();},"json");
   							 }
   						 },{iconCls:'Arrowleft',
   							 text: '??????',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"left",num:-10},function(json){WF.reload();},"json");}
   						 },{iconCls:'Arrowright',
   							 text: '??????',handler:function(){
   								 $.post(handlerJsp,{c:"move",id:WF.getDefineId(),t:"left",num:10},function(json){WF.reload();},"json");}
   						 },{
                	           // xtype: 'splitbutton',
               	            text : '????????????',
               	            iconCls:'Pagewhitecode',
               	           
               	            menu:new Ext.menu.Menu({
               	            items:[{text:'????????????',handler:function(e){
               	            		openWindow("??????????????????","e?e=tlingx_wf_define&m=eventSaveSet&id="+WF.getDefineId());
               	            	}},
               	                   {text:'????????????',disabled:true,handler:function(e){
               	                	 //	Lingx.getRootWindow().getApi().openWindow("??????????????????","lingx/model/editor/create/view_entity.jsp?pageid="+pageId);
                           	            
               	                   }},
               	                   {text:'????????????',disabled:true,handler:function(e){
               	                	  // openWindow("??????????????????","lingx/model/editor/create/clone.jsp?code="+currentEntityCode);
               	                   }}],
               	            
               	            })
               	        },"-",{
               	        	iconCls:"Basketput",
           	            	text:"??????",
           	            	handler:function(){
           	            		openWindow("????????????","e?e=tlingx_wf_define&m=input");
           	            	}
           	            },{
               	        	iconCls:"Basketremove",
           	            	text:"??????",
           	            	handler:function(e){
           	            		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		//openWindow("????????????","e?e=tlingx_wf_define&m=edit&id="+record.data.id);
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
                	            text : '????????????',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","e?e=tlingx_wf_define&m=add&pageid="+pageId);
                	            }
                	        }*/
/*                	        ,{
                	            xtype: 'button',
                	            text : '????????????',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","e?e=tlingx_wf_define&m=del&id="+record.data.id+"&pageid="+pageId);}
                	        }*/
                	        /*,{iconCls:'Arrowrefresh',
                	            xtype: 'button',
                	            text : '????????????',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        }*/,/*{
                	            xtype: 'button',
                	            text : '??????????????????',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("??????????????????","http://127.0.0.1:8080/lingx-web/lingx/workflow/define/handler.jsp?c=getDefineTaskAndLine&id="+record.data.id);
                	            	}
                	        },{
                	            xtype: 'button',
                	            text : '??????????????????',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("??????????????????","lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=image&pid="+record.data.id;
                	            	}
                	        },*/
             	        //{ xtype: 'tbspacer', width: 50 }, // ????????????50???????????????
						 
                	     {
                	         iconCls:'Tagblue',
							 text: '??????',
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
							 text: '??????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(1);
								 }
             	            }
						 },{iconCls:'Delete',
							 text: '??????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(4);
								 }
             	            }
						 } ,'-'
						/* ,{
							 text: '?????????',
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
							 text: '????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(5);
								 }
             	            }
						 },{
							 iconCls:'Stop',
							 text: '????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(6);
								 }
             	            }
						 },{
							 iconCls:'Script',
							 text: '????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(2);
								 }
             	            }
						 },{
							 iconCls:'Scriptcode',
							 text: '????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(7);
								 }
             	            }
						 },{
							 iconCls:'Shapeungroup',
							 text: '????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(8);
								 }
             	            }
						 },{
							 iconCls:'Arrowswitchbluegreen',
							 text: '???????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(10);
								 }
             	            }
						 } ,'-'
						/* ,{
							 text: '?????????',
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
							 text: '???????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(3);
								 }
             	            }
						 },{
							 iconCls:'Bulletcross',
							 text: '???????????????',
							 enableToggle:true,
							 toggleGroup:'toggleGroup',
							 handler:function(btn,pressed,options){
								 if(btn.pressed){
									 WF.setPointer(9);
								 }
             	            }
						 }
/*						 ,'-',{
							 text: '???????????????',
							 enableToggle:true,
							 pressed:true,
							 handler:function(btn,pressed,options){
								WF.setDisplayLine(btn.pressed);
								WF.reload();
             	            }
						 }
						 */
						 
                	       /* // ?????????????????????
                	        '->', // ?????? { xtype: 'tbfill' }
                	        {
                	            xtype    : 'textfield',
                	            name     : 'field1',
                	            emptyText: '???????????????'
                	        },
                	        // ??????????????????????????????????????????
                	        '-', // ?????? {xtype: 'tbseparator'} ?????? Ext.toolbar.Separator
                	        'text 1', // ?????? {xtype: 'tbtext', text: 'text1'} ?????? Ext.toolbar.TextItem
                	        { xtype: 'tbspacer' },// ?????? ' ' ?????? Ext.toolbar.Spacer
                	        'text 2'*/
                	    ]
                }],
                collapsible: false,
                //collapsed: true,
                //title: '????????????',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                title: '?????????',
                split: true,
                width: 240,
                minWidth: 175,
                maxWidth: 400,
                collapsible: true,
                animCollapse: true,
                margins: '0 0 0 2',

            	id: 'entityGrid_1',
            	stateId: 'entityGrid_2',
                title: '????????????',
                iconCls: 'icon-7',
                xtype:'grid',

                dockedItems: [{
        	        xtype: 'toolbar',
        	        items: [
        	                {id:"text", xtype: 'textfield',width:140, emptyText: '????????????',listeners:{
        	                	specialkey: function(field, e){
        	                		if(e.getKey()== e.ENTER){
        	                			extParams={param:Ext.getCmp("text").getValue()};
                	                	entityStore.load();
        	                		}
        	                	}
        	                }},
        	                { xtype: 'button', text: '??????' ,handler:function(){
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
        	                { xtype: 'button', text: '??????' ,handler:function(){
        		        		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		openWindow("????????????","e?e=tlingx_wf_define&m=edit&id="+record.data.id);
        	                }}
        	            ],
        	        dock: 'top',
        	        displayInfo: true
        	    }],
                columns: [{ xtype: 'rownumberer'}, 
                          { header: '??????',  dataIndex: 'code' },
                          { header: '??????', dataIndex: 'name' }
                      ],
                store: entityStore,
              /*
                dockedItems: [{
        	        xtype: 'toolbar',
                    border:false,
        	        items: [
        	                {id:"text", xtype: 'textfield', emptyText: '????????????'},
        	                { xtype: 'button', text: '??????' ,handler:function(){
        	                	extParams={param:Ext.getCmp("text").getValue()};
        	                	entityStore.load();
        	                }},
        	                { xtype: 'button', text: '??????' ,handler:function(){
        		        		var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
        		        		openWindow("????????????","e?e=tsysentity&m=edit&id="+record.data.id);
        	                }}
        	            ],
        	        dock: 'top',
        	        displayInfo: true
        	    }
                //,{
                //    	        xtype: 'pagingtoolbar',
                //    	        store: entityStore,   // GridPanel????????????????????????
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
            	title:"?????????",
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