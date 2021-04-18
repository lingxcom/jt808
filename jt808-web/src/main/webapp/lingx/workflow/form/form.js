	var currentEntityCode='';
	var currentEntityId='';
	var currentScriptId='';
	var extParams={};
	window.onload=function(){
		document.onkeypress=function(){};
		document.onkeydown=function(){};
	};
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
    	         url: handlerJsp+'?c=getFormList',
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
                minSize: 53,
                maxSize: 80,
                items:{
                	xtype: 'toolbar',
                	border:false,
                	items: [
                	        
                	        {
                	            xtype: 'button',
                	            text : '新建表单',
                	            iconCls:'New',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("新建表单","e?e=tlingx_wf_define_form&m=add&pageid="+pageId);
                	            }
                	        },{
                	            xtype: 'button',
                	            text : '删除表单',
                	            iconCls:'Delete',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("删除表单","e?e=tlingx_wf_define_form&m=del&id="+record.data.id+"&pageid="+pageId);}
                	        },"-",{
                	            xtype: 'button',
                	            text : '刷新列表',
                	            iconCls:'icon-reload',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        },//"-",
                	        /*{
                	        	text:"输入控件",
                	        	handler:function(){
                	        		UE.getEditor('editor').focus();  
                	        		var tempId=Lingx.getRandomString(10);
                	        		$("#ueditor_0").contents().find(".lingx-wf").css("border","1px solid #a9a9a9");
                	        		UE.getEditor('editor').execCommand('inserthtml','<input id="'+tempId+'" class="lingx-wf" style="border:1px solid red"  />'); 
                	        		$("#ueditor_0").contents().find(".lingx-wf").bind("click",edit);
                	        		inputId=tempId;

                	        	}
                	        },{
                	        	text:"绑定事件",
                	        	handler:function(){
                	        		$("#ueditor_0").contents().find(".lingx-wf").bind("click",edit);
                	        	}
                	        },*/
                	        // 使用右对齐容器
                	        '->', // 等同 { xtype: 'tbfill' }
                	        /*{
                	            xtype    : 'textfield',
                	            name     : 'field1',
                	            emptyText: '输入搜索词'
                	        },*/
                	        // 添加工具栏项之间的垂直分隔条
                	        '-', // 等同 {xtype: 'tbseparator'} 创建 Ext.toolbar.Separator
                	         // 等同 {xtype: 'tbtext', text: 'text1'} 创建 Ext.toolbar.TextItem
                	        { xtype: 'tbspacer' },// 等同 ' ' 创建 Ext.toolbar.Spacer
                	        {text:'保存表单',
                	        	iconCls:'icon-save',
                	        	handler:function(){
                	        		$("#ueditor_0").contents().find(".lingx-wf").css("border","1px solid #a9a9a9");
                	        		$.post(handlerJsp,{c:"saveForm",id:formId,content:LE.getContent()},function(json){
                	        			lgxInfo(json.message);
                	        		},"json");
                	        	}},{
                    	            xtype: 'button',
                    	            text : '表单预览',
                    	            iconCls:'Magnifier',
                    	            handler:function(){
                    	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                    	            	Lingx.getRootWindow().getApi().openWindow2("表单预览","lingx/workflow/form/view.jsp?code="+record.data.code);
                        	            //alert(handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id);
                    	            	//window.location.href=handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id;
                    	            	}
                    	        }
                	    ]
                },
                collapsible: false,
                //collapsed: true,
                //title: '工具栏区',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                //id: 'west-panel', // see Ext.getCmp() below
                title: '表单区',
                split: true,
                width: 240,
                minWidth: 175,
                maxWidth: 400,
                collapsible: true,
                animCollapse: true,
                margins: '0 0 0 2',

            	id: 'entityGrid_1',
            	stateId: 'entityGrid_2',
                title: '定义表单',
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
        		        		openWindow("编辑对象","e?e=tlingx_wf_define_form&m=edit&id="+record.data.id);
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
                		if(formId){
                			formId=record.data.id;
                			Lingx.post(handlerJsp,{c:"getForm",id:formId},function(json){
                				LE.setContent(json.content);
                				init();
                			});
                		}else{
                			LE=new LingxEditor();
                			LE.init();
                    		formId=record.data.id;
                    		Lingx.post(handlerJsp,{c:"getForm",id:formId},function(json){
                				LE.setContent(json.content);
                				init();
                			});
                		}
                	}
                	
                }
        	    
            },
            { region: 'center',
            	title:"表单设计",
            	autoScroll:true,
            	html:"<div id='editor'  style=''></div>"
            	,listeners:{
            		afterlayout:function(){
            		// initUEditor();
            			
            		}
            	}
            },{

        		id:'propertyGrid',
                title: '基本属性',
                region: 'east',
                width:300,
                animCollapse: true,
                collapsible: true,
                split: true,
                xtype:"propertygrid",
                sortableColumns:false,
                sortPropertyColumn:false,
                source: {
                   name:"",
                   authType:true,
                   authCfg:"",
                   notView:"",
                   notNull:false,
                   refEntity:"",
                   url:"",
                   json:""
                  /*  lname: "param", lnotnull: false,
                    lmaxlength:100,
                    leditor:"text",
                    lparam:"",
                    lrefEntity:"",
                    ldefaultValue:"",
                    lwidth:0,
                    lheight:0*/
                },sourceConfig: {
                	name:{displayName:"参数名称"},
                	authType:{displayName:"权限类型"},
                	authCfg:{displayName:"权限配置"},
                	notView:{displayName:"隐藏设置"},
                	notNull:{displayName:"不可为空"},
                	refEntity:{displayName:"指向对象模型",
                		editor:new Ext.form.field.ComboBox({displayField: 'name',valueField: 'code',store: new Ext.data.Store({proxy:{ model:'Entity',type:'ajax',url:'e?e=tlingx_entity&m=combo&is_ref=1&lgxsn=1',reader:{type:'json'}},autoLoad:true})})},
                	url:{displayName:"URL"},
                	json:{displayName:"扩展参数"}
                	/*lname:{displayName:"参数名称"},
                	lnotnull:{displayName:"不可为空"},
                	lmaxlength:{displayName:"字符长度"},
                	leditor: {
                        editor: new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=BJKKJ',reader:{type:'json'}},autoLoad:true})}),
                        displayName: '输入控件'
                    },
                	lparam:{displayName:"控件参数"},
                    lrefEntity: {
                        editor: new Ext.form.field.ComboBox({displayField: 'name',valueField: 'code',store: new Ext.data.Store({proxy:{ model:'Entity',type:'ajax',url:'e?e=tlingx_entity&m=combo&status=1&lgxsn=1',reader:{type:'json'}},autoLoad:true})}) ,
                        displayName: '指向对象模型'
                    },
                    ldefaultValue:{displayName:"默认值"},

                	lwidth:{displayName:"宽度"},
                	lheight:{displayName:"高度"}*/
                },
                listeners:{
                	propertychange:function(source,recordId,value,oldValue,eOpts){
                		//$("#ueditor_0").contents()
                		lgxInfo("操作成功"+recordId+":"+inputId);
                		if(recordId=="name"){
                    		$("#"+inputId).prop(recordId,value);
                		}else{
                    		$("#"+inputId).attr(recordId,value);
                		}
                	}
                }
            }]
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