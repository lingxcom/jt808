<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传文件</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss4.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>

	<script type="text/javascript" src="<%=basePath %>js/plupload/Basic.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/plupload/Button.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/plupload/plupload.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/plupload/plupload.html4.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/plupload/plupload.html5.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/plupload/plupload.flash.js"></script>
<style type="text/css">
.uploadify:hover .uploadify-button {
background-color:blue;
}
</style>
<script type="text/javascript">

var request_params={};

var fromPageId='${param.pageid}';
var entityCode="tlingx_file";
var methodCode="grid";
var entityId='${entityId }';
var params={};
params=request_params;
removeDefaultAttribute(params);
var cmpId='${param.cmpId}';
var textField='id';
var valueField='id';
var extParams={};
var extparam='${extparam}';
var fieldNames=["<%=i18n.text("查询")%>","<%=i18n.text("高级")%>"];
var type="${param.type}";

/**
 * 数据提交
 */
function lingxSubmit(){
	if(!fromPageId){closeWindow();return;}
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj=array[0];
		var val=obj.data["path"];
		var txt=obj.data["name"];
		var win=getFromWindow(fromPageId);
		var objValue=[{text:txt,value:val}];
		if(type=="JSON"){
			win.lingxSet({cmpId:cmpId,text:txt,value:JSON.stringify(objValue)});//val
		}else{
			win.lingxSet({cmpId:cmpId,text:val,value:val});//val
		}
		
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
function search(){
	var arr=[];
	for(var i=0;i<searchFieldCache.length;i++){
		var id=searchFieldCache[i],val;
		var temp={};
		val=Ext.getCmp("id-search-"+id).getValue();
		
			temp[id]=val||"";
			arr.push(temp);
		
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
	
	var store=Ext.getCmp("tree").getStore();
	store.getRootNode().remove();
	store.setRootNode({
		 text: "ROOT",
         id:0,
         expanded: true
	});
}

function reloadGrid2(){
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
		json.toolbar.unshift(Ext.create('Ext.ux.upload.Button', {
			//renderTo: Ext.getBody(),
			text: '上传',
			iconCls:'Folderpage',
			uploader: 
			{
				url: '<%=basePath %>lingx/template/upload/handler.jsp',
				//uploadpath: '/Root/files',
				autoStart: true,
				max_file_size: '20mb',			
				drop_element: 'dragload',
				statusQueuedText: '准备上传',
				statusUploadingText: '上传中 ({0}%)',
				statusFailedText: '<span style="color: red">上传失败</span>',
				statusDoneText: '<span style="color: green">上传成功</span>',

				statusInvalidSizeText: '上传文件超出限制',
				statusInvalidExtensionText: '该文件类型不可上传'
			},
			listeners: 
			{
				filesadded: function(uploader, files)								
				{
					//console.log('filesadded');
					return true;
				},
				
				beforeupload: function(uploader, file)								
				{
					//console.log('beforeupload');			
				},

				fileuploaded: function(uploader, file)								
				{
					
					//console.log(file);	
				},
				
				uploadcomplete: function(uploader, success, failed)								
				{			//reloadGrid();
					Ext.getCmp("tree").getSelectionModel().clearSelections();
	        		Ext.getCmp("tree").getView().refresh();
	        		extParams.category_id="";
	        		//resetExtParams();
	        		reloadGrid2();
				},
				scope: this
			}
					
			
		}));
		json.toolbar.push("->");
/*		json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){//2017-01-10 改变查询方式
			openSearchWindow(json.GridConfig.queryField,items);
		}});*/
	//,xtype:"cycle"
		if(json.GridConfig.queryField){
			var tool=[];
			for(var i=0;i<json.fields.list.length;i++){
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
			
			toolbars.push({
				 xtype: 'toolbar',
			     items:tool,
			     dock: 'top',
			     displayInfo: true
			});
		}
		toolbars.push({
	        xtype: 'toolbar',
	        items:json.toolbar,
	        dock: 'top',
	        displayInfo: true
	        });
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
			layout:'border',
			border:json.GridConfig.broder,
			items:[{
				id:"tree",
				region:"west",
				//border:false,
				width:150,
				animate:false,
	            //border:false,
	            autoScroll: true,
	            rootVisible: false,
	            store: Ext.create('Ext.data.TreeStore', {
	        		proxy: {
	        	         type: 'ajax',
	        	         url: "e?e=tlingx_file_category&m=tree&lgxsn=1",
	        	         reader: {
	        	             type: 'json'
	        	         },
	        	     },
	        	     root: {
	        	         text: "ROOT",
	        	         id:"0",
	        	         expanded: true
	        	    },
	        	     autoLoad: true,
	        	     listeners:{
	        	    	 datachanged:function(){
	        	    		/* if(Ext.getCmp("datas").getSelectionModel().getCount()==0)
	     		    		Ext.getCmp("datas").getSelectionModel().select(0); */
	     		    	}/* ,
	     		    	boforeload:function(store, operation, eOpts){
	     		    		console.log(store.parentNode);
	     		    		store.parentNode=store.getRootNode();
	     		    		console.log(store.parentNode);
	     		    		///console.log();
	     		    	} */
	        	     }
	        	}),
				xtype:"tree",
				dockedItems: [{
		    	        xtype: 'toolbar',
		    	        items:[{iconCls:"Add",handler:function(){
		    	        	openWindow("文件类型-添加","e?e=tlingx_file_category&m=add");
		    	        }},{iconCls:"Pencil",handler:function(){
		    	        	var array=Ext.getCmp("tree").getSelectionModel().getSelection();
		    	        	if(array&&array.length>0){
		    	        		var obj=array[0];
		    	        		var id=obj.data["id"];
		    	        		openWindow("文件类型-修改","e?e=tlingx_file_category&m=edit&id="+id);
		    	        	}else{
		    	        		lgxInfo("请选择文件类型项");
		    	        	}
		    	        }},{iconCls:"Delete",handler:function(){
		    	        	var array=Ext.getCmp("tree").getSelectionModel().getSelection();
		    	        	if(array&&array.length>0){
		    	        		var obj=array[0];
		    	        		var id=obj.data["id"];
		    	        		openWindow("文件类型-删除","e?e=tlingx_file_category&m=del&id="+id);
		    	        	}else{
		    	        		lgxInfo("请选择文件类型项");
		    	        	}
		    	        }},{
		    	        	text:"全部",iconCls:"Script",handler:function(){
		    	        		Ext.getCmp("tree").getSelectionModel().clearSelections();
		    	        		Ext.getCmp("tree").getView().refresh();
		    	        		extParams.category_id="";
		    	        		//resetExtParams();
				        		reloadGrid2();
		    	        	}
		    	        }],
		    	        dock: 'top',
		    	        displayInfo: true
		    	}],
		    	listeners:{
		    		
		    		select:function(view,record,item,index,event,obj){
		        		var id=record.data.id;
		        		extParams.category_id=id;
		        		reloadGrid2();
		        	}}
			},{
				region:"center",
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
	    	        	itemcontextmenu: function(view, record, item, index, e) { 
				        	
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
			        ,
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		lingxSubmit();
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

</script>
</head>
<body >

</body>
</html>