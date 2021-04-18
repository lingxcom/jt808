<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return;
java.io.File f=new java.io.File(request.getRealPath("temp"));
if(f.exists()){
org.apache.commons.io.FileUtils.cleanDirectory(f);
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<SCRIPT type="text/javascript">
var handler="<%=basePath %>lingx/model/update/handler.jsp";
var idsArray=new Array();
var textsArray=new Array();

var appid="${SESSION_USER.app.id}";
$(function(){
	$('#filename').bind('keyup', function(event){
		   if (event.keyCode=="13"){
			   findAndAddFile();
		   }
		});

 	$.post("e?e=tlingx_app&m=combo&t=3",{},function(json){
		$.each(json,function(index,obj){
			$("#appid").append("<option value='"+obj.value+"' >"+obj.text+"</option>");// "+(obj.value==rule_id?" selected=selected ":"")+"
		});
	},"JSON");
	$("#appid").bind("change",function(){
		appid=$("#appid").val();
	}); 
});

function addObject(etype){
	//indexPushId("entity");
	//		if(request_params.valueField)valueField=request_params.valueField;
	//		if(request_params.valueField)valueField=request_params.valueField;
	var value=$("#entity_value").val();
	var text=$("#entity_text").val();
	//lgxInfo(value);
	if(!value){text="";value="";}
	var url="e?e="+etype+"&m=combogrid2&cmpId=entity&valueField=code&value="+value+"&text="+encodeURI(encodeURI(text))+"&app_id="+appid;
	openWindow("选择对象",url);
}
function addToList(){
	var ids=$("#entity_val").val();
	var texts=$("#entity_key").val();
	idsArray=StringToArray(ids,idsArray);
	textsArray=StringToArray(texts,textsArray);
	replayEntity();
	$("#entity_val").val("");
	$("#entity_key").val("");
}

function StringToArray( string, array){
	return array.concat(string.split(","));
}

function replayEntity(){
	$("#list1").empty();
	for(var i=0;i<textsArray.length;i++){
		$("#list1").append("<li>[<a onclick='delLi(this);' href='javascript:;'>删除</a>] <span style='display:none;'>"+idsArray[i]+"</span>"+textsArray[i]+"</li>");
	}
}
//end
function findAndAddFile(){
	var filename=$("#filename").val();
	if(!filename){lgxInfo("文件名不可为空"); return;}
	$.post(handler,{c:"findAndAddFile",filename:filename},function(json){
		for(var i=0;i<json.list.length;i++){
			$("#list2").append("<li>[<a onclick='delLi(this);' href='javascript:;'>删除</a>] <span>"+json.list[i]+"</span></li>");
		}
	},"json");
}
function AddFile(){
	$("#list2").append("<li>[<a onclick='delLi(this);' href='javascript:;'>删除</a>] <span>"+$("#filename2").val()+"</span></li>");
}

//
function delLi(a){
	$(a).parent().remove();
}
function next(){
	if(!confirm("确认是否打包并上传到服务器？"))return;
	var entityids="",files="",sqltext,remark,type,reboot,isdata;
	/* var temp1=$("#list1").find("span");
	for(var i=0;i<temp1.length;i++){
		entityids+=($(temp1[i]).html())+",";
	} */
	var temp2=$("#list2").find("span");
	for(var i=0;i<temp2.length;i++){
		files+=($(temp2[i]).html())+",";
	}
	sqltext=$("#sqltext").val();
	remark=$("#remark").val();
	if(!remark){
		lgxInfo("更新描述不可为空");
		return;
	}
	if($("#appid").val()==0){
		lgxInfo("更新应用不可为空");
		return;
	}
	type=2;
	reboot=$('input[name="sfcq"]:checked').val();
	option=$('input[name="option"]:checked').val();
	var gncd=$('input[name="gncd"]:checked').val();
	var sjmx=$('input[name="sjmx"]:checked').val();
	entityids=$("#entity_value").val();
	$.post(handler,{c:"upload",
		appid:appid,
		entityids:entityids,
		files:files,
		sqltext:sqltext,
		gncd:gncd,
		sjmx:sjmx,
		remark:remark,
		type:type,
		reboot:reboot,
		isOption:option,
		name:$("#name").val(),
		secret:$("#secret").val(),
		},function(json){
			if(json.message)
		alert(json.message);
			else{
				alert("上传失败，密钥无效");
			}
	},"json");
	
	

}

function download(){
	if(!confirm("确认是否打包并下载到本地吗？"))return;
	var entityids="",files="",sqltext,remark,type,reboot,isdata;
	/* var temp1=$("#list1").find("span");
	for(var i=0;i<temp1.length;i++){
		entityids+=($(temp1[i]).html())+",";
	} */
	var temp2=$("#list2").find("span");
	for(var i=0;i<temp2.length;i++){
		files+=($(temp2[i]).html())+",";
	}
	sqltext=$("#sqltext").val();
	remark=$("#remark").val();
	if(!remark){
		lgxInfo("更新描述不可为空");
		return;
	}
	if($("#appid").val()==0){
		lgxInfo("更新应用不可为空");
		return;
	}
	type=2;
	reboot=$('input[name="sfcq"]:checked').val();
	var option=$('input[name="option"]:checked').val();
	var gncd=$('input[name="gncd"]:checked').val();
	entityids=$("#entity_value").val();
	var url=Lingx.urlAddParams(handler,{c:"packAndDownload",
		appid:$("#appid").val(),
		entityids:entityids,
		files:files,
		sqltext:encodeURI(encodeURI(sqltext)),
		remark:encodeURI(encodeURI(remark)),
		type:type,
		reboot:reboot,
		isOption:option,
		gncd:gncd,
		name:encodeURI(encodeURI($("#name").val())),
		secret:encodeURI(encodeURI($("#secret").val()))
		});
	//alert(url);
	window.location.href=url;
	

}
function lingxSubmit(){
	download();
}
</SCRIPT>
<style type="text/css">
*{font-size:14px;
line-height:28px;
margin:0px;
padding:0px;
}

button{padding-left:10px;padding-right:10px;}
</style>
</head>
<body style="background-color:#dfe9f6;padding:10px;">
<TABLE id="table-form" width="100%" height="100%" border="0" cellSpacing="2" cellPadding="0" >
<tr><td  align="right"><span style="color:red">*</span>上传密钥：</td><td><input id="secret" style="width:600px;" value="none"> 密钥只为作为能不能上传到服务器的标志，不影响下载与更新</td></tr>
<tr style="display:none;"><td  align="right"><span style="color:red">*</span>更新名称：</td><td><input id="name" type="hidden" style="width:600px;" value="LINGX平台更新包"> </td></tr>

<tr><td align="right"><span style="color:red">*</span>更新描述：</td><td><input id="remark" style="width:600px;"></input></td></tr>

<tr  style="display:none;"><td  align="right"><span style="color:red">*</span>选择应用：</td><td><input id="appid" name="appid" value="${SESSION_USER.app.id }" /> </td></tr> 
<!-- <tr><td width="120" align="right"><span style="color:red">*</span>数据类型：</td><td>
<input id="id1" type="radio" name="xjlx" value="1" checked/><label for="id1">系统补丁</label>
<input id="id2" type="radio" name="xjlx" value="2"/><label for="id2">应用更新</label>
<input id="id3" type="radio" name="xjlx" value="3"/><label for="id3">功能插件</label>
 </td></tr> -->
<tr><td width="200" align="right"><span style="color:red">*</span>是否需要重启：</td><td>
<input id="ida3" type="radio" name="sfcq" value="1"/><label for="ida3">需要</label>
<input id="ida4" type="radio" name="sfcq" value="0" checked/><label for="ida4">不需要</label>
</td></tr>

<tr><td width="120" align="right"><span style="color:red">*</span>是否需要字典：</td><td>
<input id="idb3" type="radio" name="option" value="1"/><label for="idb3">需要</label>
<input id="idb4" type="radio" name="option" value="0" checked/><label for="idb4">不需要</label>
</td></tr>

<tr><td  align="right"><span style="color:red">*</span>是否需要功能菜单：</td><td>
<input id="idc3" type="radio" name="gncd" value="1"/><label for="idc3">需要</label>
<input id="idc4" type="radio" name="gncd" value="0" checked/><label for="idc4">不需要</label>
</td></tr>

<tr style="display:none;"><td  align="right"><span style="color:red">*</span>是否需要数据模型：</td><td>
<input id="idd3" type="radio" name="sjmx" value="1" checked/><label for="idd3">需要</label>
<input id="idd4" type="radio" name="sjmx" value="0" /><label for="idd4">不需要</label>
</td></tr>


<!--  -->
<tr><td width="120"  align="right">实体对象：</td><td>
<input type="hidden" id="entity_value" /><input id="entity_text" style="width:600px;" readonly="readonly"/>
<button onclick="addObject('tlingx_entity');">选择</button>
<div>
<hr/>
<ul id="list1">

</ul>
</div>
</td></tr>
<tr><td align="right">文件列表：</td><td><input id="filename" style="width:600px;" /> <button onclick="findAndAddFile();">查询并添加</button> 文件全名，大小写必须一致
<br>
<input id="filename2" style="width:600px;display:none;" /> <button onclick="AddFile();" style="display:none;">直接添加</button>
<div>
更新文件列表
<ul id="list2">

</ul>
</div>
</td></tr>
<tr><td align="right">SQL脚本：</td><td><textarea id="sqltext" style="width:600px;height:100px;"></textarea> </td></tr>
<tr><td colspan="2" align="center"><button onclick="download()">打包并下载到本地</button>  <!-- -->
<button onclick="next()" >打包并上传到服务器</button> 

</td></tr>
</TABLE>
</body>
</html>