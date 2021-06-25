


/**
 * 数据提交
 */
function lingxSubmit(){
	if(!fromPageId){closeWindow();return;}
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj=array[0];
		var val=obj.data[valueField];
		var txt=obj.data[textField];
		var win=getFromWindow(fromPageId);
		win.lingxSet({cmpId:cmpId,text:txt,value:val});
		closeWindow();
	}else if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){//
    	getFromWindow(fromPageId).reloadGrid();
		closeWindow();
	}else{
		lgxInfo("请选择列表项");
	}
	//win.lingxSet({value:"有志者事竟成"});
}

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
	for(var i=0;i<array.length;i++){
		var temp=array[i];
		for(var t in temp){
			extParams[t]='_'+temp[t];
		}
	}
	//extParams['page']=1;
	//reloadGrid();
	Ext.getCmp("datas").getStore().loadPage(1);
}
/**
 * 刷新列表数据
 */
var selarr = [];//刷新前选中数据的缓存区
var toolbars=[];//工具栏，可多行
var searchFieldCache=[];//查询字段ID缓存
var jsonGrid={};
function getJsonGrid(){
	return jsonGrid;
}
function fdate(date,xtype){
	xtype=xtype||"datetimefield";
	if(!date)return "";
	var str="",temp;
	str+= date.getFullYear();
	temp="00"+(date.getMonth() + 1);
	str+=temp.substring(temp.length-2);

	temp="00"+date.getDate();
	str+=temp.substring(temp.length-2);
	if(xtype=="datetimefield"){
	temp="00"+date.getHours();
	str+=temp.substring(temp.length-2);

	temp="00"+date.getMinutes();
	str+=temp.substring(temp.length-2);

	temp="00"+date.getSeconds();
	str+=temp.substring(temp.length-2);
	}
	return str;
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



function reloadGrid(){
	 var records = Ext.getCmp("datas").getSelectionModel().getSelection();
     selarr.splice(0);
     for (var i in records) {
         selarr.push(records[i].index);
     }
     
	Ext.getCmp("datas").getStore().reload();
}
Ext.onReady(function(){
	extParams=resetExtParams();
	var items=new Array();
	Lingx.post("g",{e:entityCode},function(json){
		jsonGrid=json;
		for(var i=0;i<json.fields.list.length;i++){
			var f=json.fields.list[i];
			if(f.inputType=='hidden'||params[f.code]){//隐藏的不留标签,级联字段
				continue;
			}
			///start 2015-11-19
			var obj=f;
			var ipt={
					id:f.code,
					fieldLabel:f.name,
					name:f.code};
			if(obj.inputType=='radio'||obj.inputType=='checkbox'){
				ipt.xtype=obj.inputType;
				if(obj.refEntity=='tlingx_optionitem'){
					ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code='+obj.inputOptions,reader:{type:'json'}}),
						autoLoad:false});
				}else{
					ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+obj.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
						autoLoad:false});
				}
				////obj.valueAjaxUrl='e?e=toption&m=items&lgxsn=1&code='+field.inputOptions;
			}else if(obj.refEntity){
				ipt.displayField= 'text';
				ipt.valueField= 'value';
				ipt.xtype='combobox';
				ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+obj.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
					autoLoad:false});
			}
			items.push(ipt);
			///end 2015-11-19
			
		}
		// 级联处理
		var tempArray=new Array();
		for(var i=0;i<json.columns.length;i++){
			var c=json.columns[i];
			if(!params[c.dataIndex]){
				tempArray.push(c);
			}
		}
		json.columns=tempArray;
		// 级联处理 end
		Ext.each(json.fields.list,function(obj,index,self){
			if(obj.comboType=='ref-display')textField=obj.code;
			if(obj.comboType=='ref-value')valueField=obj.code;
		});
		if(json.GridConfig.rownumbers){
			json.columns.unshift({ xtype: 'rownumberer',width:26});
		}
		json.toolbar.push("->");
/*		json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){//2017-01-10 改变查询方式
			openSearchWindow(json.GridConfig.queryField,items);
		}});*/
	//,xtype:"cycle"
		var tool=[];
		if(json.queryParams.length==0&&json.GridConfig.queryField){
			
			for(var i=0;i<json.fields.list.length;i++){
				var field=json.fields.list[i];
				if(params[field.code])continue;//当外部传参进来就不需要这个查询字段了
				if((","+json.GridConfig.queryField+",").indexOf(","+field.code+",")>=0){
					searchFieldCache.push(field.code);
					tool.push(field.name);
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
		    	            
		    	            store:store,
								displayField:"text",
								valueField:"value",
		    	            width:110,listeners:{
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
				var w=110;
				var store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:obj.url,reader:{type:'json'}}),
					autoLoad:false});
				searchFieldCache.push(obj.code);
				tool.push(obj.name);
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
				if(obj.xtype=="datetimefield"){//不支持了2021-04-10
					options111.width=150;
				}else if(obj.xtype=="datefield"){
					options111.width=130;
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
		/*
		* Model
		
		Ext.define(entityCode, {
		    extend: 'Ext.data.Model',
		    fields:json.model,
		    idProperty: json.GridConfig.idField
		});
		*/
		/*
		* Store
		*/
		var requestUrl= "e?e="+entityCode+"&m="+methodCode+"&lgxsn=1";
		if(json.GridConfig.requestUrl){requestUrl=json.GridConfig.requestUrl;}
		var store = Ext.create('Ext.data.Store', {
		    pageSize: json.GridConfig.pageSize,
		   // model: entityCode,
		    remoteSort: json.GridConfig.remoteSort,
		    autoLoad:json.GridConfig.autoLoad,
		    proxy: {
		    	actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	            },

		    	type: 'ajax',
		        url:requestUrl,
		        reader: {
		        	type: 'json',
		            root: 'rows',
		            totalProperty: 'total'
		        }//,
		        //simpleSortMode: true
		    },
		    sorters: [{
		        property: json.GridConfig.sortName,
		        direction: json.GridConfig.sortOrder
		    }],
		    listeners:{
		    	beforeload:function(){
		    		Ext.apply(store.proxy.extraParams,extParams);  
		    	},
		    	load:function(){
		    		if(selarr&&selarr.length>0){
		    			for (var i = 0; i < selarr.length; i++) {
		    				Ext.getCmp("datas").getSelectionModel().select(selarr[i], true);
	                    }
		    		}else{
		    			Ext.getCmp("datas").getSelectionModel().select(0);
		    		}
		    		
		    	}
		    }
		});
		
		
		Ext.create("Ext.Viewport",{
			layout:'fit',
			border:json.GridConfig.broder,
			items:[{
				id:"datas",
				border:json.GridConfig.broder,
				loadMask:json.GridConfig.loadMask,
				store: store,
				//forceFit: true,
				split: true,
				xtype:'grid',
				viewConfig : {
					//True表示为自动展开/缩小列的宽度以适应grid的宽度，这样就不会出现水平的滚动条
					//forceFit : true
					},

			    dockedItems: toolbars,
	    	        listeners:{
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		if(LingxOpertor=="input"){//当打开方式为选择对框话时，不显示操作按钮；且双击变选择
								lingxSubmit();
								return;
							}
	    	        		var dblclickMethod=json.GridConfig.dblclickMethod||"view";
	    	        		if("none"==dblclickMethod)return;
	    	        		var id=record.data.id;
	    	        		Lingx.post("d",{c:"method_script",e:entityCode,m:dblclickMethod,id:id},function(json2){
	    						if(json2.ret){
	    							
	    	        		if(dblclickMethod=="view"){
	    	        			openViewWindow(entityCode,json.name,record.data.id,json.GridConfig.winStyle);
	    	        		}else{
	    	        			openWindow(json.name,"e?e="+entityCode+"&m="+dblclickMethod+"&id="+record.data.id,json.GridConfig.winStyle);
	    	        		}

	    						}else{
	    							lgxInfo(json2.msg||"不可操作");
	    						}
	    					});
	    	        	},
	    	        	itemcontextmenu: function(view, record, item, index, e) { 
	    	        		if(LingxOpertor=="input"){//当打开方式为选择对框话时，不显示操作按钮；且双击变选择
								//lingxSubmit();
								return;
							}
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
	    	        },
				  columns:json.columns,
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: store,
			            displayInfo:json.GridConfig.displayInfo
			        })
			}]
		});
	});
});