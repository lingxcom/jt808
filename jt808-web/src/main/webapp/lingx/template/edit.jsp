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
		if(json.code==1){
			if(json.message)
			lgxInfo(json.message);
        	if(getFromWindow(fromPageId)&&getFromWindow(fromPageId).reloadGrid){
	        	getFromWindow(fromPageId).reloadGrid();
        	}
        	closeWindow();
        }else if(json.code==2){
    		if(json.message)
    		lgxInfo(json.message);
        	//不刷新，例如大型树形结构数据，刷新很麻烦2021-01-25
        	closeWindow();
        }else{
    		if(json.message)
    			alert(json.message);
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
		initDateIpt();
		if(!fromPageId){
			appendSubmitButton();
		}
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
	if(obj.inputType=="hidden"||obj.enabled==false){
		if($.type(obj.value)=="object")obj.value=obj.value.value;
		$("#form").append('<input type="hidden" class="form-control" id="'+obj.code+'" value="'+obj.value+'">');
	}else if(obj.inputType=="combobox"){
		var url="",code=obj.code,value=obj.value;
		if($.type(value)=="object")value=value.value;
		if(obj.refEntity=='tlingx_optionitem'){
			url='e?e=tlingx_option&m=items&lgxsn=1&code='+obj.inputOptions;
		}else{
			url='e?e='+obj.refEntity+'&m=combo&lgxsn=1';
		}
		$.post(url,{},function(json){
			var temp='<select class="form-control"> <option value""></option>';
			for(var i=0;i<json.length;i++){
				var o=json[i];
				
				temp+='<option  value="'+o.value+'" '+(value==o.value?"selected":"")+'>'+o.text+'</option>';//'<label><input type="radio" name="'+code+'" value="'+o.value+'" '+(value==o.value?"checked":"")+'> '+o.text+'</label>  ';
			}
			temp+='</select>';
			$("#"+code+"_Ipt").append(temp);
		},"json");
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'" id="'+obj.code+'_Ipt">'+
	    //'  <textarea type="text" class="form-control" id="'+obj.code+'" value="'+(obj.value||"")+'"  />'+
	    '</div>'+
	  	'</div>';
	  	 appendForm(template,(obj.formClass||"col-xs-10"))//$("#form").append(template);
	}else if(obj.inputType=="radio"){
		var url="",code=obj.code,value=obj.value;
		if($.type(value)=="object")value=value.value;
		if(obj.refEntity=='tlingx_optionitem'){
			url='e?e=tlingx_option&m=items&lgxsn=1&code='+obj.inputOptions;
		}else{
			url='e?e='+obj.refEntity+'&m=combo&lgxsn=1';
		}
		$.post(url,{},function(json){
			var temp='<div class="radio">';
			for(var i=0;i<json.length;i++){
				var o=json[i];
				
				temp+='<label><input type="radio" name="'+code+'" value="'+o.value+'" '+(value==o.value?"checked":"")+'> '+o.text+'</label>  ';
			}
			temp+='</div>';
			$("#"+code+"_Ipt").append(temp);
		},"json");
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'" id="'+obj.code+'_Ipt">'+
	    //'  <textarea type="text" class="form-control" id="'+obj.code+'" value="'+(obj.value||"")+'"  />'+
	    '</div>'+
	  	'</div>';
	  	 appendForm(template,(obj.formClass||"col-xs-10"))//$("#form").append(template);
	}else if(obj.inputType=="checkbox"){
		var url="",code=obj.code,value=obj.value;
		if($.type(value)=="object")value=value.value;
		if(obj.refEntity=='tlingx_optionitem'){
			url='e?e=tlingx_option&m=items&lgxsn=1&code='+obj.inputOptions;
		}else{
			url='e?e='+obj.refEntity+'&m=combo&lgxsn=1';
		}
		$.post(url,{},function(json){
			var temp='<div class="checkbox">';
			for(var i=0;i<json.length;i++){
				var o=json[i];
				
				temp+='<label><input type="checkbox" name="'+code+'" value="'+o.value+'" '+(value==o.value?"checked":"")+'> '+o.text+'</label>  ';
			}
			temp+='</div>';
			$("#"+code+"_Ipt").append(temp);
		},"json");
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'" id="'+obj.code+'_Ipt">'+
	    //'  <textarea type="text" class="form-control" id="'+obj.code+'" value="'+(obj.value||"")+'"  />'+
	    '</div>'+
	  	'</div>';
	  	 appendForm(template,(obj.formClass||"col-xs-10"))//$("#form").append(template);
	}else if(obj.inputType=="textarea"){
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
	    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
	    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
	    '  <textarea type="text" class="form-control" id="'+obj.code+'"   />'+
	    '</div>'+
	  	'</div>';
		  //template=$(template);
		 // template.find("#"+obj.code).val(obj.value);
	  	 appendForm(template,(obj.formClass||"col-xs-10"))//$("#form").append(template);
	  	$("#form").find("#"+obj.code).val(obj.value);
	}else if(obj.inputType=="password"){
	
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <input type="password" class="form-control" id="'+obj.code+'" >'+
		    '</div>'+
		  '</div>';
		  //template=$(template);
		  //template.find("#"+obj.code).val(obj.value);
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(obj.value);
	}else if(obj.inputType=="displayfield"){
	
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <input type="text" readonly class="form-control" id="'+obj.code+'" >'+
		    '</div>'+
		  '</div>';
		  //template=$(template);
		 // template.find("#"+obj.code).val(obj.value);
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(obj.value);
	}else if(obj.inputType=="datefield"){
	
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <input type="text" class="form-control form_date" data-date-format="yyyy-mm-dd" id="'+obj.code+'"  >'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(obj.value);
	}else if(obj.inputType=="datetimefield"){
	
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <input type="text" class="form-control form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" id="'+obj.code+'" >'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(obj.value);
	}else if(obj.inputType=="file"){
		var value=obj.value;
		//console.log(value);
		var text=getTexts(value);
		//alert(value);
		//var value=getValues(value);
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogUploadFile(\'文件中心\',\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogUploadFile(\'文件中心\',\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  //template=$(template);
		  //template.find("#"+obj.code).val(value);
		  //template.find("#"+obj.code+"_text").val(text);
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);

		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="image"){
		var value=obj.value;
		var text=getTexts(value);
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogUploadFile(\'文件中心\',\''+obj.code+'\',\'URL\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogUploadFile(\'文件中心\',\''+obj.code+'\',\'URL\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogoption"){
		//dialogModel(title,ecode,mcode,cmpId)
		//console.log(obj.value)
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="grid";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogoption2"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="combogrid2";
		//var jsonValue=JSON.stringify(obj.value);
		//jsonValue=encodeURI(jsonValue);
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogtree"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="tree";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogtree2"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="combotree2";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogModel(\'选择对话框\',\''+ecode+'\',\''+mcode+'\',\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialoguser"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogUser1(\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogUser1(\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialoguser2"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogUserN(\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogUserN(\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogorg"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogOrg1(\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogOrg1(\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else if(obj.inputType=="dialogorg2"){
		var value=obj.value;
		var text=getTexts(value);
		var value=getValues(value);
		var ecode=obj.refEntity;
		var mcode="";
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '<div class="input-group">'+
		    '<input type="text" readonly style="background-color:#fff;" onclick="dialogOrgN(\''+obj.code+'\')" class="form-control" id="'+obj.code+'_text"  >'+
		    '<span class="input-group-btn"><button class="btn btn-default" onclick="dialogOrgN(\''+obj.code+'\')" type="button">选择...</button></span></div>'+
		    '<input type="hidden" class="form-control" id="'+obj.code+'" />'+
		    '</div>'+
		  '</div>';
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(value);
		  $("#form").find("#"+obj.code+"_text").val(text);
	}else{
	
		var template='<div class="form-group '+(obj.formClass||"col-xs-10")+'">'+
		    '<label for="'+obj.code+'" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">'+(obj.isNotNull?"<span style='color:red'>*</span>":"")+obj.name+'</label>'+
		    '<div class="'+(obj.formClass2||"col-xs-8")+'">'+
		    '  <input type="text" class="form-control" id="'+obj.code+'"  >'+
		    '</div>'+
		  '</div>';
		 /// console.log(obj.code+":"+obj.value)
		  //template=$(template);
		  appendForm(template,(obj.formClass||"col-xs-10"))// $("#form").append(template);
		  $("#form").find("#"+obj.code).val(obj.value);
		
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

function appendSubmitButton(){
	var template=' <div class="form-group">'+
	   ' <div class="col-sm-offset-8 col-sm-4">'+
   ' <button type="button" onclick="lingxSubmit()" class="btn btn-primary">  &nbsp;&nbsp;提交&nbsp;&nbsp;  </button>'+
   ' <button type="button" onclick="lingxClear()"  class="btn btn-default">  &nbsp;清空&nbsp;  </button>'+
  '</div>'+
'</div>';
	$("#form").append(template);
}

function initDateIpt(){
	$('.form_datetime').datetimepicker({
	 	language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
        showMeridian: 1
    });
	$('.form_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });
	$('.form_time').datetimepicker({
		language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 1,
		minView: 0,
		maxView: 1,
		forceParse: 0
    });
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
    <label for="inputEmail3" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">用户账号</label>
    <div class="'+(obj.formClass2||"col-xs-8")+'">
      <input type="text" class="form-control" id="inputEmail3" placeholder="Email">
    </div>
  </div>
  <div class="form-group col-xs-6">
    <label for="inputPassword3" class="'+(obj.formClass1||"col-xs-4")+' " style="line-height:32px;text-align:right;">交易密码</label>
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