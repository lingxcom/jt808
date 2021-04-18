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
                	            text : '部署流程',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("部署流程","lingx/workflow/manager/create/upload.jsp?pageid="+pageId);
                	            }
                	        },{
                	            xtype: 'button',
                	            text : '删除流程',
                	            handler:function(){
                	            	if(!confirm("确定删除该流程吗？"))return;
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.post(handlerJsp,{c:'removeWorkflow',id:record.data.dId},function(json){
                	            		reloadGrid();
                	            		lgxInfo(json.message);});
                	            	}
                	        },{
                	            xtype: 'button',
                	            text : '下载流程文件',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("下载流程文件","lingx/workflow/manager/view/viewXml.jsp?id="+record.data.id);
                    	            //alert(handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id;
                	            	}
                	        },/*{
                	            xtype: 'button',
                	            text : '查看流程图片',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("查看流程图片","lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id);
                	            	//window.location.href=handlerJsp+"?c=resourceRead&type=image&pid="+record.data.id;
                	            	}
                	        },*/ '-',{
                	            // xtype: 'button', // 默认的工具栏类型
                	            text: '同步用户组织',
                	            handler:function(){
                	            	//var node= Ext.getCmp(currentEntityCode).getRootNode();
                	            	Lingx.post(handlerJsp,{c:'synchIdentity'},function(json){lgxInfo(json.message);});
                	            }
                	        },
             	        //{ xtype: 'tbspacer', width: 50 }, // 添加一个50像素的空格
						 '-',{
							 text: '未设置',
             	            handler:function(){
             	            		            }
							 
						 },
                	        // 使用右对齐容器
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
                	        'text 2'
                	    ]
                },
                collapsible: false,
                //collapsed: true,
                title: '工具栏区',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                id: 'west-panel', // see Ext.getCmp() below
                title: '对象区',
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
                    title: '所有流程',
                    iconCls: 'icon-7',
                    xtype:'grid',
                    border:false,
            	   
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
                    		//"lingx/workflow/manager/view/viewPng.jsp?id="+record.data.id
                    		var img=document.getElementById("img");
                    		//alert();
                    		img.src="w?_c=image&processDefinitionId="+record.data.id;
                    		
                    	}
                    	
                    }
            	    
                }, {
                    title: '激活流程',
                    html: '<p>Some settings in here.</p>',
                    iconCls: 'icon-7'
                }, {
                    title: '挂起流程',
                    html: '<p>Some info in here.</p>',
                    iconCls: 'icon-7'
                }]
            },
            { region: 'center',
            	title:"流程图",
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