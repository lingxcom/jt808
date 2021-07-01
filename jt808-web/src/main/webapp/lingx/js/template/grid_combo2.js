
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
 * 数据提交
 */
function lingxSubmit(){
	var labels=$(".label");
	var val="",txt="";
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		val+=o.find(".value").html()+",";
		txt+=o.find(".text").html()+",";
	}
	if(labels.length>0){
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
	}
	var win=getFromWindow(fromPageId);
	win.lingxSet({cmpId:cmpId,text:txt,value:val});
	closeWindow();
}

function lingxSubmit_bak(){
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj,val="",txt="";
		for(var i=0;i<array.length;i++){
			obj=array[i];
			val=val+obj.data[valueField]+",";
			txt=txt+obj.data[textField]+",";
		}
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
		var win=getFromWindow(fromPageId);
		win.lingxSet({cmpId:cmpId,text:txt,value:val});
		closeWindow();
	}else{
		lgxInfo("请选择列表项");
	}
	//win.lingxSet({value:"有志者事竟成"});
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
			if(request_params.valueField)valueField=request_params.valueField;
			if(request_params.valueField)valueField=request_params.valueField;
		});
		if(json.GridConfig.rownumbers){
			json.columns.unshift({ xtype: 'rownumberer',width:26});
		}
		json.toolbar.push("->");
		/*json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){
			openSearchWindow(json.GridConfig.queryField,items);
		}});//,xtype:"cycle"
*/
		
		if(json.queryParams.length==0&&json.GridConfig.queryField){
			var tool=[];
			for(var i=0;i<json.fields.list.length;i++){
				if(i==2)break;//选择框宽度不够，只显示两项
				var field=json.fields.list[i];
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
		    	            emptyText: field.name,
		    	            store:store,
		    	            displayField:"text",
							valueField:"value",
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
		    	            emptyText: field.name,
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
		/*	if(json.isSearch){
				tool.push("->");
				tool.push({iconCls:'Zoom',text:fieldNames[1]||"高级",handler:function(){
					openWindow4(fieldNames[1]||"高级查询","e?e="+json.code+"&m=search");
				}});
			}*/
				
			toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true,border:false,
			});
		}else if(json.queryParams.length>0){
			var tool=[];
			for(var i=0;i<json.queryParams.length;i++){
				var obj=json.queryParams[i];
				var w=100;
				var store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:obj.url,reader:{type:'json'}}),
					autoLoad:false});
				searchFieldCache.push(obj.code);
				tool.push(obj.name+":");
				var options111={
						id:"id-search-"+obj.code,
						xtype    : obj.xtype,
			            name     : obj.code,
			            emptyText: obj.name,
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
					options111.width=180;
				}else if(obj.xtype=="datefield"){
					options111.width=120;
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
			toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true
			});
		}
		/*toolbars.push({
	        xtype: 'toolbar',
	        items:json.toolbar,
	        dock: 'top',
	        displayInfo: true
	        });*/
		
		/*
		* Model
		*/
		Ext.define(entityCode, {
		    extend: 'Ext.data.Model',
		    fields:json.model,
		    idProperty: json.GridConfig.idField
		});
		
		/*
		* Store
		*/
		var store = Ext.create('Ext.data.Store', {
		    pageSize: json.GridConfig.pageSize,
		    model: entityCode,
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
		        url: "e?e="+entityCode+"&m="+methodCode+"&lgxsn=1",
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
		    		//Ext.getCmp("datas").getSelectionModel().select(0);
		    	}
		    }
		});
		var sm = Ext.create('Ext.selection.CheckboxModel');   
		Ext.create("Ext.Viewport",{
			layout:'border',
			border:json.GridConfig.broder,
			items:[{
				region: 'west',
				//title: '对象区',
	            split: true,
	            autoScroll:true,
	            border:false,
	            width: 162,
	            minWidth: 75,
	            maxWidth: 200,
	            margins: '0 0 0 0',
	            contentEl:"Value-DIV"
			},{
				id:"datas",
				region:"center",
				border:json.GridConfig.broder,
				loadMask:json.GridConfig.loadMask,
				store: store,
				//forceFit: true,
				split: true,
				xtype:'grid',
				selModel: sm,
				viewConfig : {
					//True表示为自动展开/缩小列的宽度以适应grid的宽度，这样就不会出现水平的滚动条
					//forceFit : true
					},

			    dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:toolbars,
	    	        dock: 'top',
	    	        displayInfo: true
	    	        }],
	    	        listeners:{
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		openViewWindow(entityCode,json.name,record.data.id);
	    	        	},
	    	        	select:function(el,record, index, eOpts ){
	    	        		addItem(record.data);
	    	        	}
	    	        },
				  columns:json.columns,
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: store,
			            displayInfo:json.GridConfig.displayInfo
			        })
			}]
		});
		initItem();
	});
});


function initItem(){
	if(!value)return;
	var arrayValue=value.split(",");
	var arrayText=text.split(",");
	for(var i=0;i<arrayValue.length;i++){
		var o={};
		o[textField]=arrayText[i];
		o[valueField]=arrayValue[i];
		addItem(o);
	}
}
function addItem(data){
	var labels=$(".label");
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		if(o.find(".value").html()==data[valueField]){
			lgxInfo("该项已选择");
			return;
		}
	}
	//console.log(data);
	var div=$("<div class='label'><span class='value' style='display:none;'>"+data[valueField]+"</span><span class='text'>"+data[textField]+"</span><a class='remove' style='float:right;padding:4px;display:none;' href='javascript:;' onclick='delItem(this)'><img src='lingx/js/resources/css/icons/erase.png' width='12'/></a></div>");
	div.bind("mouseover",function(){
		$(this).find("a").show();
	});
	div.bind("mouseout",function(){
		$(this).find("a").hide();
	});
	$("#Value-DIV").prepend(div);
}
function delItem(el){
	var p=$(el).parent();
	p.empty();
	p.remove();
	delete p;
}