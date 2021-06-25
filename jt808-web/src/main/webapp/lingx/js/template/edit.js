isExe=false;
var confirmMsg='';
var fieldsCache='';
function lingxSubmit(){
	if(isExe){
		alert("正在后台处理，请稍后！可以关闭当前对话框。");
		return ;
	}
	if(confirmMsg){
		if(confirm(confirmMsg)){
			lingxSubmitFn();
		}
		/*Ext.MessageBox.confirm("系统消息",confirmMsg,function(val){
			if(val=='yes')lingxSubmitFn();
		});*/
	}else{
		lingxSubmitFn();
	}
}
function lingxSubmitFn(){
	// 非空检查 
	var fields=Ext.getCmp("form").getForm().getFields();
	var objCache={};
	for(var i=0;i<fields.items.length;i++){
		var obj=fields.items[i].getSubmitData();
		for(var temp in obj){
			objCache[temp]=obj[temp];
		}
	}
	for(var i=0;i<fieldsCache.length;i++){
		var f=fieldsCache[i];
		if(f.isNotNull&&!objCache[f.code]){
			lgxInfo(f.name+"不可为空");
			return ;
		}
	}
	// 非空检查 end
	isExe=true;
	Ext.getCmp("form").getForm().submit({
		params:{t:3},
		success: callbackFormSubmit,
		failure:callbackFormSubmit
	});
}

Ext.onReady(function(){
	var items=new Array();
	if(params["_refId_"]){//支持多表关联时的编辑
		items.push({name:"_refId_",value:params["_refId_"],xtype:'hidden'});
		items.push({name:"_refEntity_",value:params["_refEntity_"],xtype:'hidden'});
	}
	var requestParameters={e:entityCode,m:methodCode,eid:entityId,pageid:fromPageId||""};
	for(var p in params){
		requestParameters[p]=params[p];
	}
	Lingx.post("g",requestParameters,function(json){
		
		confirmMsg=json.confirm;
		fieldsCache=json.fields.list;
		var extParamJson=json.extParamJson||"{}";
		extParamJson=Ext.JSON.decode(extParamJson);
		if(json.prompt){
			items.push({fieldLabel:'提示',name:'',value:json.prompt,xtype:'displayfield'});
		}
		for(var i=0;i<json.fields.list.length;i++){
			var field=json.fields.list[i];
			if(field.inputType=='hidden'){//隐藏的不留标签
				if(Ext.isObject(field.value))field.value=field.value.value;//当对象字段值，需要转换
				items.push({name:field.code,value:field.value,xtype:'hidden'});
				continue;
			}
			
			if(field.inputType=='displayfield'){//
				var realValue=field.value;
				if(Ext.isObject(field.value)){
					realValue=field.value.value;
					if(field.value.exists){
						field.value="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+field.value.id+"\")'>"+field.value.text+"</a>";
					}else{
						field.value=field.value.text;
					}
				}
				if(!params[field.code]){//当该字段需要隐藏时，原来的控件会变成hidden可以传值，就不附件hidden框
					items.push({name:field.code,value:realValue,xtype:'hidden'});
				}
				
			}
			
			var extparam="",eparams="";
			if(field.inputOptions){
				if(field.inputOptions.indexOf("{")==0){
					eparams=Ext.decode(field.inputOptions);
				}else{
					var type=field.inputOptions;
					var ec=entityCode;
					var fc=getFieldCodeFormMethodFieldByStr(field.refEntity,json.fields.list);//field.refEntity
					var hf=getFieldCodeFormMethodFieldByObj(params,json.fields.list);
					var hfval=params[hf];
					extparam=(type+","+fc+","+ec+","+hf+","+hfval);
					eparams={extparam:extparam};
				}
			}
			
				var obj={
						etype:field.refEntity,
						fieldLabel:field.isNotNull?"<span style='color:red;'>*</span>"+field.name:field.name,
						name:field.code,
						xtype:field.inputType,
						value:field.value,
						params:eparams,
						width:field.length*14,
						minWidth:100,
						maxWidth:400,
						inputOptions:field.inputOptions
						};
				if(field.inputType=='datefield'){//日期标签加属性
					obj.format="Y-m-d";
					obj.altFormats='Y-m-d|m.d.Y';
				}
				if(field.inputType=='datetimefield'){
					obj.format="Y-m-d H:i:s";
					obj.altFormats='Y-m-d H:i:s|m.d.Y';
					
				}
				if((!json.toField||json.toField=='id')&&params[field.code]){//通过URL传入的字段进行隐藏，在添加角色的上级节点，该地方不合适隐藏，所以注释
					obj.xtype="hidden";
					obj.value=params[field.code];
				}
				if(obj.xtype=="textfield"||obj.xtype=="password"){
					obj.listeners={
							specialkey: function(field, e){
			                    if (e.getKey() == e.ENTER) {
			                    	lingxSubmit();
			                    }
			                },
							focus:function(_this,the,ops){
							}
					};
				}
				if(obj.xtype=='radio'||obj.xtype=='checkbox'){
					if(field.refEntity=='tlingx_optionitem'){
						obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code='+field.inputOptions,reader:{type:'json'}}),
							autoLoad:false});
					}else{
						obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+field.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
							autoLoad:false});
					}
				}
				
				if(obj.xtype=='combobox'){
					obj.displayField= 'text';
					obj.valueField= 'value';

					if(field.refEntity=='tlingx_optionitem'){
						obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code='+field.inputOptions,reader:{type:'json'}}),
							autoLoad:true});
					}else{
						obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+field.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
							autoLoad:true});
					}
					if(Ext.isObject(obj.value)){
						obj.value=obj.value.value+'';
					}
				}
				
				if(obj.xtype=='numberfield'){
					obj.hideTrigger=true;
				}
				items.push(obj);
			
		}
		

		var requestUrl="e?e="+entityCode+"&m="+methodCode;
		if(json.requestUrl){requestUrl=json.requestUrl;}
		Ext.create("Ext.Viewport",{
			layout:'fit',
	        border: false,
	        autoScroll:true,
			items:[{
				id:"form",
				xtype:'form',
				width:300,
		        autoScroll:true,
				bodyStyle:"background:#dfe9f6;",
				 bodyPadding: 5,
				 fieldDefaults: {
			            labelAlign: 'right',
			            labelWidth: 100,
			            anchor: '100%'
			        },
		        url: requestUrl,
		        defaultType: 'textfield',
		        border: false,
		        items:items,
		        listeners:{
		        	afterrender:function(panel){
		        		if(extParamJson.width){
		        			Lingx.getRootWindow().resizeWindow({width:extParamJson.width});
		        		}
		        		if(extParamJson.height){
		        			Lingx.getRootWindow().resizeWindow({height:extParamJson.height});
		        		}else{
		        			if(panel.getHeight()+Lingx.PANEL_HEIGHT>Lingx.MAX_HEIGHT){
				        		Lingx.getRootWindow().resizeWindow({height:Lingx.MAX_HEIGHT+30});
			        		}else{
			        			Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT+30});
			        		}
		        		}
		        		
		        	}
		        }
			}
			       ]
		});
		
	});
});