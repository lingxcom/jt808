<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑实体</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="lingx/js/lingx-bootstarp.js?12" charset="UTF-8"></script>

<link href="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<style type="text/css">
.span-control{
display:block;width:100%;padding:7px 0px;font-size:14px;line-height:1.42857143;color:#555;
}
</style>
<script type="text/javascript">
var request_params=${REQUEST_PARAMS};
var fromPageId='${param.pageid}';
var entityCode=request_params.e;
var methodCode=request_params.m;
var entityId=request_params.id;
var params=${_params};
var vaildMessage={};
var servletRet={};


if(request_params["_refEntity_"]){
	params["_refEntity_"]=request_params["_refEntity_"];
}
if(request_params["_refId_"]){
	params["_refId_"]=request_params["_refId_"];
}

isExe=false;
var confirmMsg='';
var fieldsCache='';
var requestUrl='';
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
	
	var objCache={};
	var fields=$(".form-control");
	for(var i=0;i<fields.length;i++){
		var el=$(fields[i]);
		var name=el.attr("id");
		objCache[name]=el.val();
	}
	fields=$("input:checked");
	for(var i=0;i<fields.length;i++){
		var el=$(fields[i]);
		var name=el.attr("name");
		objCache[name]=el.val();
	}
	objCache.t=3;
	//console.log(objCache);
	// 非空检查 
	for(var i=0;i<fieldsCache.length;i++){
		var f=fieldsCache[i];
		if($.type(objCache[f.code])== "undefined")objCache[f.code]="";
		if(f.isNotNull&&!objCache[f.code]){
			lgxInfo(f.name+"不可为空");
			return ;
		}
	}
	// 非空检查 end
	isExe=true;
	
	$.post(requestUrl,objCache,function(json){
		isExe=false;
		if(json.message)
		lgxInfo(json.message);
		if(json.code==1){
        	if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){
	        	getFromWindow(fromPageId).reloadGrid();
        	}
        	closeWindow();
        }else if(json.code==2){
        	//不刷新，例如大型树形结构数据，刷新很麻烦2021-01-25
        	closeWindow();
        }else{
        	
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
	},"json")
	/*
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
	*/
}

$(function(){
	var items=new Array();
	if(params["_refId_"]){//支持多表关联时的编辑
		items.push({name:"_refId_",value:params["_refId_"],xtype:'hidden'});
		items.push({name:"_refEntity_",value:params["_refEntity_"],xtype:'hidden'});
	}
	var requestParameters={e:entityCode,m:methodCode,eid:entityId,pageid:fromPageId||""};
	for(var p in params){
		requestParameters[p]=params[p];
	}
	$.post("g",requestParameters,function(json){
		
		confirmMsg=json.confirm;
		fieldsCache=json.fields.list;
		var extParamJson=json.extParamJson||"{}";
		extParamJson=$.parseJSON(extParamJson);
		if(json.prompt){
			items.push({fieldLabel:'提示',name:'',value:json.prompt,xtype:'displayfield'});
		}
		var lastFormClass="";
		for(var i=0;i<json.fields.list.length;i++){
			var field=json.fields.list[i];
			
			if((!json.toField||json.toField=='id')&&params[field.code]){//通过URL传入的字段进行隐藏，在添加角色的上级节点，该地方不合适隐藏，所以注释
				field.inputType="hidden";
				field.value=params[field.code];
			}
				//items.push(obj);
				if($.type(field.value)== "undefined")field.value="";
				pushForm(field);
				lastFormClass=field.formClass;
		}
		if("col-xs-6"==lastFormClass){
			$(".form-group:last").removeClass("col-xs-6");
			$(".form-group:last").addClass("col-xs-12");
		}
		$("input").bind("keypress",function(e){
			if(e.keyCode==13){
				lingxSubmit();
			}
		});
		
		if($("body").height()+Lingx.PANEL_HEIGHT>Lingx.MAX_HEIGHT){
    		Lingx.getRootWindow().resizeWindow({height:Lingx.MAX_HEIGHT+30});
		}else{
			Lingx.getRootWindow().resizeWindow({height:$("body").height()+Lingx.PANEL_HEIGHT+60});
		}

		 requestUrl="e?e="+entityCode+"&m="+methodCode;
		if(json.requestUrl){requestUrl=json.requestUrl;}
		
	},"json");
});
function pushForm(obj){
	if(obj.refEntity)obj.inputType="object";
	switch(obj.inputType){
	case "hidden":
		$("#form").append('<input type="hidden" class="form-control" id="'+obj.code+'" value="'+obj.value+'">');
		break;
	
	case "file":
		var field=obj;
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
        var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+':</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
	    '  <span  id="'+obj.code+'"  class="span-control" ></span>'+
	    '</div>'+
	  '</div>';
	  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
	  $("#form").find("#"+obj.code).html(field.value);
		break;
	case "image":
		var field=obj;
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
        var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+':</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
	    '  <span  id="'+obj.code+'"  class="span-control" ></span>'+
	    '</div>'+
	  '</div>';
	  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
	  $("#form").find("#"+obj.code).html(field.value);
		break;
	case "object":
	case "combobox":
	case "radio":
	case "checkbox":
	case "dialogoption":
	case "dialogoption2":
	case "dialogtree":
	case "dialogtree2":
	case "dialoguser":
	case "dialoguser2":
	case "dialogorg":
	case "dialogorg2":
		var field=obj;
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
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+':</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
	    '  <span  id="'+obj.code+'"  class="span-control" ></span>'+
	    '</div>'+
	  '</div>';
	  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
	  $("#form").find("#"+obj.code).html(field.value);
	break;
	default:
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+':</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <span  id="'+obj.code+'"  class="span-control" ></span>'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).text(obj.value);
	}
	
}
var preClazz="";
function appendForm(template,clazz){
	var el=$(template);
	if(clazz=="col-xs-6"&&preClazz==clazz){
		preClazz="";
		$(".form-group:last").removeClass("col-xs-6");
		$(".form-group:last").addClass("col-xs-12");
		$(".form-group:last").append(el.html())
		return;
	}
 	$("#form").append(el);
	preClazz=clazz;
}

function getTexts(temp){
	try{
	if($.type(temp)=="object"){
				return temp.text;
	}else if(temp&&temp.length>1&&temp.charAt(0)=='['){
    	var tempText="";
    	var tempArr=$.parseJSON(temp);
    	for(var i=0;i<tempArr.length;i++){
    		tempText+=tempArr[i].text+",";
    	}
    	if(tempText.length>0){
    		tempText=tempText.substring(0,tempText.length-1);
    	}
    	return tempText;
    }else if(temp&&temp.length>1&&temp.charAt(0)=='{'){
    	var tempMap=$.parseJSON(temp);
    	return tempMap["text"];
    }else{
    	return temp;
    }
	}catch(e){
		return temp;
	}
}


function getValues(temp){
	try{
	 if($.type(temp)=="object"){
			return temp.value;
	 }else if(temp&&temp.length>1&&temp.charAt(0)=='['){
    	var tempText="";
    	var tempArr=$.parseJSON(temp);
    	for(var i=0;i<tempArr.length;i++){
    		tempText+=tempArr[i].value+",";
    	}
    	if(tempText.length>0){
    		tempText=tempText.substring(0,tempText.length-1);
    	}
    	return tempText;
    }else if(temp&&temp.length>1&&temp.charAt(0)=='{'){
    	var tempMap=$.parseJSON(temp);
    	return tempMap["value"];
    }else {
    	return temp;
    }
	}catch(e){
		return temp;
	}
}
	
	function lingxClear(){
		$("input").val("");
		$("textarea").val("");
		
	}
</script>
</head>
<body style="background:#dfe9f6;overflow-x:hidden">
<div  class="container" style="margin-top:20px;width:700px;">
<div class="form-horizontal" id="form">
<div id="hidden" style="display:none;"></div>
<!--
  <div class="form-group col-xs-6">
    <label for="inputEmail3" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">用户账号:</label>
    <div class="'+(obj.formClass2||"col-xs-8")+'">
      <input type="text" class="form-control" id="inputEmail3" placeholder="Email">
    </div>
  </div>
  <div class="form-group col-xs-6">
    <label for="inputPassword3" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">交易密码:</label>
    <div class="'+(obj.formClass2||"col-xs-8")+'">
      <input type="password" class="form-control" id="inputPassword3" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-xs-offset-2 col-xs-10">
      <div class="checkbox">
        <label>
          <input type="checkbox"> Remember me
        </label>
      </div>
    </div>
  </div>
   -->
</div>
</div>
</body>

</html>