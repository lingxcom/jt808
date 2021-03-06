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
    	         url: handlerJsp+'?c=getWorkflowList',
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
                height: 53,
                minSize: 53,
                maxSize: 53,
                items:{
                	xtype: 'toolbar',
                	border:false,
                	items: [
                	        
                	        {
                	            xtype: 'button',
                	            text : '????????????',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","lingx/workflow/manager/create/upload.jsp?pageid="+pageId);
                	            }
                	        },{
                	            xtype: 'button',
                	            text : '????????????',
                	            handler:function(){
                	            	if(!confirm("???????????????????????????"))return;
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.post(handlerJsp,{c:'removeWorkflow',id:record.data.dId},function(json){
                	            		reloadGrid();
                	            		lgxInfo(json.message);});
                	            	}
                	        },{
                	            xtype: 'button',
                	            text : '??????????????????',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("??????????????????","lingx/workflow/manager/view/viewXml.jsp?id="+record.data.id);
                    	            //alert(handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id;
                	            	}
                	        },/*{
                	            xtype: 'button',
                	            text : '??????????????????',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("??????????????????","lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=image&pid="+record.data.id;
                	            	}
                	        },*/ '-',{
                	            // xtype: 'button', // ????????????????????????
                	            text: '??????????????????',
                	            handler:function(){
                	            	//var node= Ext.getCmp(currentEntityCode).getRootNode();
                	            	Lingx.post(handlerJsp,{c:'synchIdentity'},function(json){lgxInfo(json.message);});
                	            }
                	        },
             	        //{ xtype: 'tbspacer', width: 50 }, // ????????????50???????????????
						 '-',{
							 text: '?????????',
             	            handler:function(){
             	            		            }
							 
						 },
                	        // ?????????????????????
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
                	        'text 2'
                	    ]
                },
                collapsible: false,
                //collapsed: true,
                title: '????????????',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                id: 'west-panel', // see Ext.getCmp() below
                title: '?????????',
                split: true,
                width: 240,
                minWidth: 175,
                maxWidth: 400,
                collapsible: true,
                animCollapse: true,
                margins: '0 0 0 2',
                layout: 'accordion',
                items: [{
                	id: 'entityGrid_1',
                	stateId: 'entityGrid_2',
                    title: '????????????',
                    iconCls: 'icon-7',
                    xtype:'grid',
                    border:false,
            	   
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
                    		//"lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id
                    		var img=document.getElementById("img");
                    		//alert();
                    		img.src="w?_c=image&processDefinitionId="+record.data.id;
                    		
                    	}
                    	
                    }
            	    
                }, {
                    title: '????????????',
                    html: '<p>Some settings in here.</p>',
                    iconCls: 'icon-7'
                }, {
                    title: '????????????',
                    html: '<p>Some info in here.</p>',
                    iconCls: 'icon-7'
                }]
            },
            { region: 'center',
            	title:"?????????",
            	html:"<div style='padding:5px;'><img id='img' src=''/></div>"}]
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