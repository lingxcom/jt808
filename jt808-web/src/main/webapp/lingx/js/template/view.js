
Ext.onReady(function(){
	var items=new Array();
	Lingx.post("g",{e:entityCode,m:methodCode,eid:entityId},function(json){
		if(json.prompt){
			items.push({fieldLabel:'提示',name:'',value:json.prompt,xtype:'displayfield'});
		}
		for(var i=0;i<json.fields.list.length;i++){
			var field=json.fields.list[i];
			if(field.inputType=='hidden'){//隐藏的不留标签
				items.unshift({name:field.code,value:field.value,xtype:'hidden'});
				continue;
			}
			if(Ext.isObject(field.value)){
				if(field.value.exists){
					if(field.escape){//是否转义->是转义
					
					if(field.isEntityLink){//是否连接->要连接
						
					
					if(field.refEntity=="tlingx_optionitem"){
						field.value=field.value.text;
					}else{
						if(field.value.value)field.value.value=field.value.value+"";
						if(field.value.value.indexOf(",")==-1){
							field.value="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+field.value.id+"\")'>"+field.value.text+"</a>";
						}else{
							var arrayValue=field.value.value.split(",");
							var arrayText=field.value.text.split(",");
							var arrayId=field.value.id.split(",");
							var temp1="";
							for(var i=0;i<arrayValue.length;i++){
								temp1+="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+arrayId[i]+"\")'>"+arrayText[i]+"</a>,";
							}
							temp1=temp1.substring(0, temp1.length-1);
							field.value=temp1;
						}
					}
					
					}else{//不要连接
						field.value=field.value.text;
					}
					
					}else{//不转义
						field.value=field.value.value;
					}//end 是否转义
				}else{
					field.value=field.value.text;
				}
			}
			if(field.inputType=='file'&&field.value){
				var temp=field.value;
				
		        if(temp&&temp.length>1&&temp.charAt(0)=='['){
		        	var tempText="";
		        	var tempArr=Ext.JSON.decode(temp);
		        	for(var ii=0;ii<tempArr.length;ii++){
		        		tempText+="<a target='_blank' href='"+tempArr[ii].value+"' >"+tempArr[ii].text+"</a>,";
		        	}
		        	if(tempText.length>0){
		        		tempText=tempText.substring(0,tempText.length-1);
		        	}
		        	field.value=tempText;
		        }else{
		        	field.value="<a target='_blank' href='"+field.value+"' >文件下载</a>";
		        }
				
				
			}
			if(field.inputType=='image'&&field.value){
				var temp=field.value;
		        if(temp&&temp.length>1&&temp.charAt(0)=='['){
		        	var tempText="";
		        	var tempArr=Ext.JSON.decode(temp);
		        	for(var ii=0;ii<tempArr.length;ii++){
		        		tempText+="<a target='_blank' href='"+tempArr[ii].value+"' >"+tempArr[ii].text+"</a>,";
		        	}
		        	if(tempText.length>0){
		        		tempText=tempText.substring(0,tempText.length-1);
		        	}
		        	field.value=tempText;
		        }else{
		        	field.value="<a target='_blank' href='"+field.value+"' ><img width='400' src='"+field.value+"'/></a>";
		        }
				//field.value="<a target='_blank' href='"+field.value+"' ><img width='400' src='"+field.value+"'/></a>";
				
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
		Ext.create("Ext.Viewport",{
			layout:'fit',
	        border: false,
	        autoScroll:true,
			items:[{
				id:"form",
				xtype:'form',
		        autoScroll:true,
				bodyStyle:"background:#dfe9f6;",
				 //frame: true,
				 bodyPadding: 5,
				 fieldDefaults: {
			            labelAlign: 'right',
			            labelWidth: 100
			           , anchor: '100%'
			        },
				//layout: 'absolute',
		        url: "e?e="+entityCode+"&m="+methodCode+"&lgxsn=1",
		        defaultType: 'displayfield',
		        border: false,
		        items:items,
		        listeners:{
		        	afterrender:function(panel){
		        		if(panel.getHeight()+Lingx.PANEL_HEIGHT>Lingx.MAX_HEIGHT){
			        		Lingx.getRootWindow().resizeWindow({height:Lingx.MAX_HEIGHT+30});
		        		}else{
		        			Lingx.getRootWindow().resizeWindow({height:panel.getHeight()+Lingx.PANEL_HEIGHT+30});
		        		}
		        	}
		        }
			}
			       ]
		});
		
	});
});