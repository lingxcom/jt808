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
                	            text : '????????????',
                	            iconCls:'New',
                	            handler:function(){
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","e?e=tlingx_wf_define_form&m=add&pageid="+pageId);
                	            }
                	        },{
                	            xtype: 'button',
                	            text : '????????????',
                	            iconCls:'Delete',
                	            handler:function(){
                	            	var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                	            	Lingx.getRootWindow().getApi().openWindow("????????????","e?e=tlingx_wf_define_form&m=del&id="+record.data.id+"&pageid="+pageId);}
                	        },"-",{
                	            xtype: 'button',
                	            text : '????????????',
                	            iconCls:'icon-reload',
                	            handler:function(){
                	            	Ext.getCmp("entityGrid_1").getStore().reload();
                	            
                	            }
                	        },//"-",
                	        /*{
                	        	text:"????????????",
                	        	handler:function(){
                	        		UE.getEditor('editor').focus();  
                	        		var tempId=Lingx.getRandomString(10);
                	        		$("#ueditor_0").contents().find(".lingx-wf").css("border","1px solid #a9a9a9");
                	        		UE.getEditor('editor').execCommand('inserthtml','<input id="'+tempId+'" class="lingx-wf" style="border:1px solid red"  />'); 
                	        		$("#ueditor_0").contents().find(".lingx-wf").bind("click",edit);
                	        		inputId=tempId;

                	        	}
                	        },{
                	        	text:"????????????",
                	        	handler:function(){
                	        		$("#ueditor_0").contents().find(".lingx-wf").bind("click",edit);
                	        	}
                	        },*/
                	        // ?????????????????????
                	        '->', // ?????? { xtype: 'tbfill' }
                	        /*{
                	            xtype    : 'textfield',
                	            name     : 'field1',
                	            emptyText: '???????????????'
                	        },*/
                	        // ??????????????????????????????????????????
                	        '-', // ?????? {xtype: 'tbseparator'} ?????? Ext.toolbar.Separator
                	         // ?????? {xtype: 'tbtext', text: 'text1'} ?????? Ext.toolbar.TextItem
                	        { xtype: 'tbspacer' },// ?????? ' ' ?????? Ext.toolbar.Spacer
                	        {text:'????????????',
                	        	iconCls:'icon-save',
                	        	handler:function(){
                	        		$("#ueditor_0").contents().find(".lingx-wf").css("border","1px solid #a9a9a9");
                	        		$.post(handlerJsp,{c:"saveForm2",id:formId,content:LE.getContent()},function(json){
                	        			lgxInfo(json.message);
                	        		},"json");
                	        	}},{
                    	            xtype: 'button',
                    	            text : '????????????',
                    	            iconCls:'Magnifier',
                    	            handler:function(){
                    	            	lgxInfo("????????????????????????????????????");
                    	            	//var record=Ext.getCmp("entityGrid_1").getSelectionModel().getSelection()[0];
                    	            	//Lingx.getRootWindow().getApi().openWindow2("????????????","lingx/workflow/form/view.jsp?code="+record.data.code);
                        	            //alert(handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id);
                    	            	//window.location.href=handlerJsp+"?c=resourceRead&type=xml&pid="+record.data.id;
                    	            	}
                    	        }
                	    ]
                },
                collapsible: false,
                //collapsed: true,
                //title: '????????????',
                margins: '2 2 2 2'
            },{
                region: 'west',
                stateId: 'navigation-panel',
                //id: 'west-panel', // see Ext.getCmp() below
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
        		        		openWindow("????????????","e?e=tlingx_wf_define_form&m=edit&id="+record.data.id);
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
                		if(formId){
                			formId=record.data.id;
                			Lingx.post(handlerJsp,{c:"getForm2",id:formId},function(json){
                				LE.setContent(json.content);
                				init();
                			});
                		}else{
                			LE=new LingxEditor();
                			LE.init();
                    		formId=record.data.id;
                    		Lingx.post(handlerJsp,{c:"getForm2",id:formId},function(json){
                				LE.setContent(json.content);
                				init();
                			});
                		}
                	}
                	
                }
        	    
            },
            { region: 'center',
            	title:"????????????",
            	autoScroll:true,
            	html:"<div id='editor'  style=''></div>"
            	,listeners:{
            		afterlayout:function(){
            		// initUEditor();
            			
            		}
            	}
            },{

        		id:'propertyGrid',
                title: '????????????',
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
                   json:"",
                   placeholder:""
                  /*  lname: "param", lnotnull: false,
                    lmaxlength:100,
                    leditor:"text",
                    lparam:"",
                    lrefEntity:"",
                    ldefaultValue:"",
                    lwidth:0,
                    lheight:0*/
                },sourceConfig: {
                	name:{displayName:"????????????"},
                	authType:{displayName:"????????????"},
                	authCfg:{displayName:"????????????"},
                	notView:{displayName:"????????????"},
                	notNull:{displayName:"????????????"},
                	refEntity:{displayName:"??????????????????",
                		editor:new Ext.form.field.ComboBox({displayField: 'name',valueField: 'code',store: new Ext.data.Store({proxy:{ model:'Entity',type:'ajax',url:'e?e=tlingx_entity&m=combo&is_ref=1&lgxsn=1',reader:{type:'json'}},autoLoad:true})})},
                	url:{displayName:"URL"},
                	json:{displayName:"????????????"},
                	placeholder:{displayName:"????????????"}
                	/*lname:{displayName:"????????????"},
                	lnotnull:{displayName:"????????????"},
                	lmaxlength:{displayName:"????????????"},
                	leditor: {
                        editor: new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=BJKKJ',reader:{type:'json'}},autoLoad:true})}),
                        displayName: '????????????'
                    },
                	lparam:{displayName:"????????????"},
                    lrefEntity: {
                        editor: new Ext.form.field.ComboBox({displayField: 'name',valueField: 'code',store: new Ext.data.Store({proxy:{ model:'Entity',type:'ajax',url:'e?e=tlingx_entity&m=combo&status=1&lgxsn=1',reader:{type:'json'}},autoLoad:true})}) ,
                        displayName: '??????????????????'
                    },
                    ldefaultValue:{displayName:"?????????"},

                	lwidth:{displayName:"??????"},
                	lheight:{displayName:"??????"}*/
                },
                listeners:{
                	propertychange:function(source,recordId,value,oldValue,eOpts){
                		//$("#ueditor_0").contents()
                		lgxInfo("????????????"+recordId+":"+inputId);
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