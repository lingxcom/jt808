<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.model.bean.UserBean,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.core.service.*" %>
<%
/* ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return; */

com.lingx.support.web.listener.OnlineUserManager onlineUserManager=com.lingx.support.web.listener.OnlineUserManager.getOnlineUserManager(session);

String id=request.getParameter("id");
if(id!=null&&!"".equals(id)){
	onlineUserManager.invalidateSession(id);
	onlineUserManager.removeSession(id);
}List<UserBean> list=onlineUserManager.getListUser();
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>js/store.js"></script>
<script type="text/javascript" src="<%=basePath %>js/avalon.js"></script>
<style type="text/css">
.active{
background:#fff;
}
.noactive{
background:#dfe8f6;cursor:pointer
}
li{
line-height:24px;
}
.t0{background:#f4f6fa;}
.t1{}
</style>
<script type="text/javascript">
var fromPageId="${param.pageid}";
var handlerJsp="lingx/template/handler.jsp";
var code="",model,userId="${SESSION_USER.id}";
var fields=[];
$(function(){
	var win=getFromWindow(fromPageId);
	var json=win.getJsonGrid();
	//console.log(json);
	code=json.code;
	 model=avalon.define({
		$id:"body",
		list:[{field:"",lo:"等于",value:"",url:"",array:[]}],
		fields:[],
		history:[],
		add:function(){
			model.list.push({field:"",lo:"等于",value:"",url:"",array:[]});
		},
		clear:function(){
			if(!confirm("是否清空?"))return;
			model.list.splice(0,model.list.length);
			model.add();
			lingxSubmit();
		},
		del:function(index){
			model.list.splice(index,1);
		},
		save:function(){
			var name=prompt("请输入要保存的名称：","");
			if(!name)return;
			var listNew=[];
			for(var i=0;i<model.list.length;i++){
				var obj=model.list[i];
				var arrayNew=[];
				for(var k=0;k<obj.array.length;k++){
					arrayNew.push({text:obj.array[k].text,value:obj.array[k].value});
				}
				listNew.push({field:obj.field,lo:obj.lo,value:obj.value,url:obj.url,array:arrayNew});
			}
			$.post(handlerJsp,{c:"save",name:name,code:json.code,json:JSON.stringify(listNew)},function(json){
				lgxInfo(json.message);
				reload("getByUser");
			},"json");
		},
		get:function(index){
			var obj=model.history[index];
			model.list=JSON.parse(obj.json);
			lingxSubmit();			
		},
		delRecord:function(id){
			$.post(handlerJsp,{c:"del",id:id},function(json){
				lgxInfo(json.message);
				reload("getByUser");
			},"json");
		}		,
		change:function(index){
			var obj={};
			for(var i=0;i<model.fields.length;i++){
				//alert(model.list[index].field+" VS "+model.fields[i].value);
				if(model.list[index].field==model.fields[i].value){
					obj=model.fields[i];
					break;
				}
			}
			model.list[index]["url"]=obj.url;
			$.post(obj.url,{},function(json){
				var arr=model.list[index]["array"];//=json;
				arr.splice(0,arr.length);
				for(var i=0;i<json.length;i++){
					arr.push({text:removeTag(json[i].text),value:json[i].value});
				}
			},"json");
			//alert(model.list[index].url);
		},
		enter:function(e){
			if(e.keyCode==13){
				lingxSubmit();
			}
		}
	});
	 
	 fields=json.fields.list;
	for(var i=0;i<json.fields.list.length;i++){
		var obj=json.fields.list[i];
		var url="";
		if(obj.refEntity){
			if(obj.refEntity=="tlingx_optionitem"){
				url="e?e=tlingx_option&m=items&t=3&code="+obj.inputOptions;
			}else{
				url="e?e="+obj.refEntity+"&m=combo&t=3";
			}
		}
		
		model.fields.push({text:obj.name,value:obj.code,url:url});
	}
	if(store.get(code)){
		//console.log(store.get(code));
		model.list=JSON.parse(store.get(code));
	}
	reload("getByUser");
	
});

function reload(cmd){
	if("getByUser"==cmd){
		$("#tab1").removeClass("noactive");
		$("#tab2").removeClass("active");
		$("#tab1").addClass("active");
		$("#tab2").addClass("noactive");
	}else{
		$("#tab1").removeClass("active");
		$("#tab2").removeClass("noactive");
		$("#tab1").addClass("noactive");
		$("#tab2").addClass("active");
	}
	$.post(handlerJsp,{c:cmd,code:code},function(json){
		model.history=json.list;
	},"json");
}
function lingxSubmit(){
	var listNew=[];
	var array=[];
	for(var i=0;i<model.list.length;i++){
		var obj=model.list[i];
		if(!obj.field){continue;}//||!obj.value
		var arrayNew=[];
		if(obj.array)
		for(var k=0;k<obj.array.length;k++){
			arrayNew.push({text:obj.array[k].text,value:obj.array[k].value});
		}
		listNew.push({field:obj.field,lo:obj.lo,value:obj.value,url:obj.url,array:arrayNew});
		var oo=getObj(array,obj.field);
		//alert(obj.field+":"+oo);
		if(oo){
			oo[obj.field]=oo[obj.field]+"|"+"_"+obj.lo+"_"+obj.value;
		}else{
			var o={};
			o[obj.field]="_"+obj.lo+"_"+obj.value;
			array.push(o);
		}
	}
	store.set(code,JSON.stringify(listNew));
	
	for(var i=0;i<fields.length;i++){
		var obj=fields[i];
		var oo=getObj(array,obj.code);
		if(!oo){
			var o={};
			o[obj.code]="";
			array.push(o);
		}
	}
	getFromWindow(fromPageId).lingxSearch(array);
}
function getObj(list,key){
	for(var i=0;i<list.length;i++){
		if(list[i][key]){
			return list[i];
		}
	}
	return null;
}

function load(el){
	var e=$(el);
	if(e.find("option").length==1){
		$.post(e.attr("url"),{},function(json){
			for(var i=0;i<json.length;i++){
				e.append("<option value='"+json[i].value+"'>"+json[i].text+"</option>");
			}
		},"json");
	}
}

function removeTag(str){
	var reTag = /<(?:.|\s)*?>/g;  
    return str.replace(reTag,"");  
}
</script>
</head>
<body ms-controller="body">
<table width="100%" >
<tr>
<td valign="top" style="height:320px;overflow:auto;">
<div  style="height:320px;overflow:auto;">
<table>
<tr ms-repeat="list">
<td>
<select ms-duplex="el.field" ms-change="change($index)" style="width:120px;">
<option value="">--请选择--</option>
<option ms-attr-value="el2.value" ms-repeat-el2="fields">{{el2.text}}</option>
</select>
</td>
<td>
<select ms-duplex="el.lo">
<option>等于</option>
<option>包含</option>
<option>大于</option>
<option>小于</option>
<option value="">----</option>
<option>不等于</option>
<option>不包含</option>
<option>大于等于</option>
<option>小于等于</option>
<option value="">----</option>
<option>匹配</option>
<option>为空</option>
<option>不为空</option>
</select>
</td>
<td>
<input ms-if="!el.url&&!(el.lo=='为空'||el.lo=='不为空')" style="width:100px;" ms-duplex="el.value" ms-keypress="enter()">
<select ms-if="el.url&&!(el.lo=='为空'||el.lo=='不为空')" style="width:100px;"  ms-duplex="el.value" ms-attr-url="el.url">
<option value="">--不选择--</option>
<option ms-attr-value="el3.value" ms-repeat-el3="el.array">{{el3.text}}</option>
</select>
</td>
<td>
<a href="javascript:;" style="color:#aaa;" ms-click="del($index)">删除</a>
</td>
</tr>
<tr>
<td colspan="4" align="center">
<a href="javascript:;" ms-click="add()">添加</a>
<a href="javascript:;" ms-click="clear()">清空</a>
<a href="javascript:;" ms-click="save()">保存</a>
</td>
</tr>
</table>
</div>
</td>
<td valign="top" align="center" width="200">
<div style="border:1px solid #999;margin:0px;padding:0px;">
<div style="height:20px;width:200px;background-color:#fff;margin:0px;padding:0px;">
<ul style="list-style:none;margin:0px;padding:0px;">
<li id="tab1" style="float:left;width:100px;text-align:center;" onclick='reload("getByUser")'>本人记录</li>
<li id="tab2" style="float:left;width:100px;text-align:center;" onclick='reload("getNoUser")'>其他记录</li>
</ul>
</div>
<div style="background-color:#fff;width:200px;height:300px;overflow:auto;margin:0px;padding:0px;">
<ul style="list-style:none;margin:0px;padding:0px;">
<li style="cursor:pointer" ms-repeat="history" ms-attr-class="'t'+($index%2)">
<a href="javascript:;" ms-click="get($index)">{{el.name}}</a>
<a href="javascript:;" ms-if="userId==el.user_id" style="float:right;" ms-click="delRecord(el.id)">
<img src='lingx/js/resources/css/icons/erase.png' width='14'/>
</a>
</li>

<li  ms-if="history.length==0">暂无数据</li>
</ul>
</div>
</div>
</td>
</tr>
</table>
</body>
</html>