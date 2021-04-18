var Lingx=Lingx||{};
Lingx.html=Lingx.html||{};
Lingx.html.template=Lingx.html.template||{};
Lingx.html.template.message="";
Lingx.PANEL_HEIGHT=60;

Lingx.MAX_HEIGHT=500;
Lingx.getRootWindow=function(param){
	param=param||window;
	if(param.isRoot&&param.isRoot())
		return param; 
	else return Lingx.getRootWindow(param.parent.window);
};
Lingx.getRandomString=function (num){
	var temp="1234567890qwertyuiopasdfghjklzxcvbnmASDFGHJKLQWERTYUIOPZXCVBNM";
	var ret="";
	for(var i=0;i<num;i++){
		ret+=temp.charAt(temp.length*Math.random());
	}
	return ret;
};

Lingx.post=function(url,params,callback){
	Ext.Ajax.request({url:url,params:params,success:function(res){
		var json=Ext.JSON.decode(res.responseText);
		callback.call(this,json);
	}}); 
};
Lingx.getTimeByDateObject=function(date){
	return date.getFullYear()+""+Lingx.add0(date.getMonth()+1,2)+""+Lingx.add0(date.getDate(),2)+Lingx.add0(date.getHours(),2)+Lingx.add0(date.getMinutes(),2)+Lingx.add0(date.getSeconds(),2);
};
Lingx.getSTimeByDateObject=function(date){
	return date.getFullYear()+""+Lingx.add0(date.getMonth()+1,2)+""+Lingx.add0(date.getDate(),2)+"000000";
};
Lingx.getETimeByDateObject=function(date){
	return date.getFullYear()+""+Lingx.add0(date.getMonth()+1,2)+""+Lingx.add0(date.getDate(),2)+"235959";
};
Lingx.formatTime14=function(value){
	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8)+" "+value.substring(8,10)+":"+value.substring(10,12)+":"+value.substring(12,14));
    
};
Lingx.formatTime8=function(value){
	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8));
    
};
Lingx.formatTimeTS=function(value){
	var date=new Date();
	date.setTime(value);
	return date.format("yyyy-MM-dd hh:mm:ss");
};
Lingx.toTime14=function(value){
	return value.format("yyyy-MM-dd hh:mm:ss").replace(/[^0-9]/ig,"");
};
Lingx.add0=function(temp,number){
	temp=""+temp;
	var len=temp.length;
	for(var i=len;i<number;i++){
		temp="0"+temp;
	}
	return temp;
};

Lingx.playAlarm=function(){
	var html="";
	//alert(Ext.isIE);
	if(Ext.isIE){
		html='<embed id="myaudio" src="images/1311.mp3" width="0" height="0" hidden="true" autostart="1" loop="1" align="middle" showdisplay="0" showcontrols="1" showpositioncontrols="1" showselectioncontrols="0" showtracker="1" moviewindowsize="0" fullscreenmode="0" moviewindowwidth="0" moviewindowheight="0" autorewind="1" playcount="1" selectionstart="0" appearance="1" borderstyle="0" filename="1311.mp3" displaymode="0"></embed>';
	}else{
		html='<audio id="myaudio" src="images/1311.mp3" controls="controls" loop="true" hidden="true" autoplay="true" ></audio>';
	}
	document.getElementById("alarmAudio").innerHTML=(html);
};

Lingx.urlAddParams=function(url,options){
	if(url.indexOf('?')==-1){
		url+="?_1=1";
	}
	if(options)
	for(var t in options){
		url=url+'&'+t+'='+options[t];
	}
	return url;
};
/**
 * 字段转编辑控件
 * field 属性
 * items from.items
 * params 参数
 */
Lingx.fieldPushItems=function(field,entity,items,params){
	var obj={
			//id:field.code,
			etype:field.refEntity,
			fieldLabel:field.isNotNull?"<span style='color:red;'>*</span>"+field.name:field.name,
			name:field.code,
			xtype:field.inputType,
			value:field.value ,
			params:{extparam:extparam},
			//xtype:'dialogoption',
			width:field.length*14,
			minWidth:100,
			maxWidth:400
			};
	if((!entity.toField||entity.toField=='id')&&params[field.code]){//通过URL传入的字段进行隐藏，在添加角色的上级节点，该地方不合适隐藏，所以注释
		obj.xtype="hidden";
		obj.value=params[field.code];
	}
	if(entity.prompt){
		items.push({fieldLabel:'提示',name:'',value:json.prompt,xtype:'displayfield'});
	}
	switch(field.inputType){
	case 'hidden':
		if(Ext.isObject(field.value))field.value=field.value.value;//当对象字段值，需要转换
		items.push({name:field.code,value:field.value,xtype:'hidden'});
		break;
	case 'displayfield':
		if(Ext.isObject(field.value)){
			if(field.value.exists){
				alert(field.value.value);
				if(field.value.value.indexOf(",")==-1){
					field.value="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+field.value.id+"\")'>"+field.value.text+"</a>";
				}else{
					var arrayValue=field.value.value.split(",");
					var arrayText=field.value.text.split(",");
					var arrayId=field.value.id.split(",");
					var temp1="";
					for(var i=0;i<arrayValue.length;i++){
						temp1+="<a href='javascript:;' onclick='openViewWindow(\""+field.value.etype+"\",\""+field.value.ename+"\",\""+arrayId[i]+"\")'>"+arrayText[i]+"</a>";
					}
					field.value=temp1;
				}
			}else{
				field.value=field.value.text;
			}
		}
		items.push({name:field.code,value:field.value,xtype:'hidden'});
		items.push(obj);
		break;
	case 'datefield':
		obj.format="Y-m-d";
		items.push(obj);
		break;
	case 'datetimefield':
		obj.format="Y-m-d H:i:s";
		items.push(obj);
		break;
	case 'textfield':
	case 'password':
		obj.listeners={
			specialkey: function(field, e){
                if (e.getKey() == e.ENTER) {
                	lingxSubmit();
                }
            }
		};
		items.push(obj);
		break;
	case 'radio':
	case 'checkbox':
		if(field.refEntity=='tlingx_optionitem'){
			obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code='+field.inputOptions,reader:{type:'json'}}),
				autoLoad:false});
		}else{
			obj.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+field.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
				autoLoad:false});
		}
		items.push(obj);
		break;
	case 'combobox':
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
			//console.log(obj.value);
			obj.value=obj.value.value;
		}
		items.push(obj);
		break;
		default:
			//alert("错误的控件类型:"+field.inputType);

			items.push(obj);
	}
	
};
Lingx.javascriptPasswordEncode=function(password,userid){
	var temp="1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";
	var useridNew="";
	for(var i=0;i<userid.length;i++){
		if(temp.indexOf(userid.charAt(i))>=0){
			useridNew+=userid.charAt(i);
		}
	}
	userid=useridNew;
	var pwdMd5=hex_md5(password);
	var useridMd5=hex_md5(userid);
	return hex_md5(pwdMd5+useridMd5);
};
//------------------公共函数-----------------------
/**
 * 对象操作方法
 */
function methodWindow(button,entityOptions,methodOptions){
	var condi="";//级联参数
	for(var i in params){
		condi=condi+"&"+i+"="+params[i];
	}
	if(methodOptions.type=='JSON'){
		if(methodOptions.currentRow){
			var array=Ext.getCmp("datas").getSelectionModel().getSelection();
			if(array&&array.length>0){
				var id=array[0].data.id;
				Lingx.post("d",{c:"method_script",e:entityOptions.code,m:methodOptions.code,id:id},function(json){
					if(json.ret){
						if(methodOptions.fields.list.length==0){
							if(methodOptions.confirm){
								if(confirm(methodOptions.confirm)){
									Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+'&'+(methodOptions.toField?methodOptions.toField:'id')+'='+id+condi,{lgxsn:1},function(json){
										lgxInfo(json.message);
										reloadGrid();
									});
								}
							}else{
								Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+'&'+(methodOptions.toField?methodOptions.toField:'id')+'='+id+condi,{lgxsn:1},function(json){
									lgxInfo(json.message);
									reloadGrid();
								});
							}
							
						}else{
							openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'&'+(methodOptions.toField?methodOptions.toField:'id')+'='+id+condi,methodOptions.winStyle);
						}
					}else{
						lgxInfo(json.msg||"不可操作");
					}
				});
				
					
			}else{
				lgxInfo("请选择要操作的记录");
			}
		}else{
			if(methodOptions.fields.list.length==0){
				if(methodOptions.confirm){
					if(confirm(methodOptions.confirm)){
						Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+condi,{lgxsn:1},function(json){
							lgxInfo(json.message);
						});
					}
				}else{
					Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+condi,{lgxsn:1},function(json){
						lgxInfo(json.message);
					});
				}
				
			}else{
				openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+condi,methodOptions.winStyle);
			}
		}
		//end json
	}else if(methodOptions.type=='JSP'){
		if(methodOptions.currentRow){
			var array=Ext.getCmp("datas").getSelectionModel().getSelection();
			if(array&&array.length>0){
				var id=array[0].data.id;
				openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi,methodOptions.winStyle);
			}else{
				lgxInfo("请选择要操作的记录");
			}
		}else{
			openWindow(entityOptions.name+"-"+button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+condi,methodOptions.winStyle);
		}
		//end jsp
	}else if(methodOptions.type=='JAVASCRIPT'){
		if(methodOptions.currentRow){
			var array=Ext.getCmp("datas").getSelectionModel().getSelection();
			if(array&&array.length>0){
				var id=array[0].data.id;
				Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi,{},function(json){
					eval(json.javascript);
				});
			}else{
				lgxInfo("请选择要操作的记录");
			}
		}else{
			Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+condi,{},function(json){
				eval(json.javascript);
			});
		}
		//end javascript
	}else if(methodOptions.type=='URL'){
		if(methodOptions.currentRow){
			var array=Ext.getCmp("datas").getSelectionModel().getSelection();
			if(array&&array.length>0){
				var id=array[0].data.id;
				openWindow(button.text,'e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi,methodOptions.winStyle);
				/*Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi,{},function(json){
					openWindow(button.text,json.url);
				});*/
			}else{
				lgxInfo("请选择要操作的记录");
			}
		}else{
			Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+condi,{},function(json){
				openWindow(button.text,json.url,methodOptions.winStyle);
			});
		}
		//end url
	}else if(methodOptions.type=='WINDOW'){//没用
		if(methodOptions.currentRow){
			var array=Ext.getCmp("datas").getSelectionModel().getSelection();
			if(array&&array.length>0){
				var id=array[0].data.id;
				Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+'&id='+id+condi,{},function(json){
					window.open(json.url);//,"_blank"
				});
			}else{
				lgxInfo("请选择要操作的记录");
			}
		}else{
			Lingx.post('e?e='+entityOptions.code+'&m='+methodOptions.code+condi,{},function(json){
				window.open(json.url);//,"_blank"
			});
		}
		//end url
	}

}
///EDIT.JS
function callbackFormSubmit(form, action) {
	isExe=false;
	if(action.result.message)lgxInfo(action.result.message);
        if(action.result.code==1){
        	if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){
	        	getFromWindow(fromPageId).reloadGrid();
        	}
        	closeWindow();
        }else if(action.result.code==2){
        	//不刷新，例如大型树形结构数据，刷新很麻烦2021-01-25
        	closeWindow();
        }else{
        	var json=action.result;
        		if(json.messages){
        			if(json.messages.length){
        				for(var i=0;i<json.messages.length;i++){
            				lgxInfo(json.messages[i]);
            			}
        			}else{
        				for(var key in json.messages ){
        					lgxInfo(json.messages[key]);
        				}
        			}
        			
        		}
        }
    }

String.prototype.format=function(param){
    var reg = /{([^{}]+)}/gm;
    return this.replace(reg,function(match,name){
        return param[name];
    });
};
String.prototype.getBytes = function() {
	var cArr = this.match(/[^x00-xff]/ig);
	return this.length + (cArr == null ? 0 : cArr.length);
};
Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

$(function(){
	$(window).bind("click",function(){
		if(window.parent&&window.parent.removeMenu){
		window.parent.removeMenu();
		}else if(window.parent.window.parent&&window.parent.window.parent.removeMenu){
			window.parent.window.parent.removeMenu();
		}
	});
});
/*
function blockContextmen(){
console.log(window.event);
if(window.event&&window.event.target.tagName!="INPUT"&&window.event.target.tagName!="TEXTAREA"){
window.event.returnValue=false;
}
}
document.oncontextmenu=blockContextmen;*/
