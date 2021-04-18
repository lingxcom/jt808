
function lingxSubmit(){
	Ext.getCmp("form").getForm().submit({
		params:{t:3},
		success:callbackFormSubmit,
		failure:callbackFormSubmit
	});
}

Ext.onReady(function(){
	var items=new Array();
	Lingx.post("g",{e:entityCode,m:methodCode,eid:entityId},function(json){
		if(json.prompt){
			items.push({fieldLabel:'提示',name:'',value:json.prompt,xtype:'displayfield'});
		}
		for(var i=0;i<json.fields.list.length;i++){
			var field=json.fields.list[i];
			if(field.inputType=='hidden'){//隐藏的不留标签
				items.push({name:field.code,value:field.value,xtype:'hidden'});
				continue;
			}
			if(Ext.isObject(field.value)){
				if(field.value.exists){
					items.push({name:field.code,value:field.value.value,xtype:'hidden'});
					field.value="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+field.value.id+"\")'>"+field.value.text+"</a>";
				}else{
					field.value=field.value.text;
				}
			}else{
				items.push({name:field.code,value:field.value,xtype:'hidden'});
			}
				if(field.inputType=='file'&&field.value){
					field.value="<a target='_blank' href='"+field.value+"' >文件下载</a>";
					
				}
				if(field.inputType=='image'&&field.value){
					field.value="<a target='_blank' href='"+field.value+"' ><img width='400' src='"+field.value+"'/></a>";
					
				}
			items.push({
				id:field.code,
				etype:field.refEntity,
				fieldLabel:field.isNotNull?"<span style='color:red;'>*</span>"+field.name:field.name,
				name:field.code,
				value:field.value,
				width:field.length*14,
				minWidth:100,
				maxWidth:400});
			
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
				bodyStyle:"background:#dfe9f6;",
				 //frame: true,
				 bodyPadding: 5,
			     autoScroll:true,
				 fieldDefaults: {
			            labelAlign: 'right',
			            labelWidth: 100
			           , anchor: '100%'
			        },
				//layout: 'absolute',
		        url: requestUrl,
		        defaultType: 'displayfield',
		        border: false,
		        items:items,
		        listeners:{
		        	afterrender:function(panel){
		        		if(panel.getHeight()+Lingx.PANEL_HEIGHT>Lingx.MAX_HEIGHT){
			        		Lingx.getRootWindow().resizeWindow({height:Lingx.MAX_HEIGHT});
		        		}else{
		        			Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT});
		        		}
		        	}
		        }
			}
			       ]
		});
		
	});
});