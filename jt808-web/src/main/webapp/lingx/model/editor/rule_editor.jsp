<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String code=request.getParameter("code");
	String id=request.getParameter("id");
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
 <title>代码编辑</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript">
var currentEntityCode="<%=code %>";
var currentEntityId="<%=id %>";
var handlerUrl="<%=basePath%>lingx/model/editor/handler.jsp";

var fromPageId='${param.pageid}';
var cindiSelectHtml="";
var leftSelectHtml="";
var rightSelectHtml="";
initSelectHtml();
var divTag="<div style='border:1px solid blue;padding:1px;margin-top:10px;background:#fff;' type='GROUP'><div>";
var condiDivTag="<div style='' type='CONDI'>"+getLeft()+getOp()+getRight()+"<input onclick='delCondi(this)' type=\"button\" value=\"删\"/></div>";
var btns="<div style='' type='TOOLS'><input onclick='addGroup(this)' type=\"button\" value=\"添加分组\"/><input onclick='addCondi(this)' type=\"button\" value=\"添加条件\"/><input onclick='delGroup(this)' type=\"button\" value=\"删除分组\"/>"+getJh()+"</div>";
var rule={};
var win={};
var isInit=false;
function addGroup(btn){
	var div=$(btn).parent().parent();
	var newDiv=$(divTag);
	newDiv.append($(btns));
	newDiv.css("border-color","#"+getRandomColor());
	div.append(newDiv);
}
function addCondi(btn){
	var div=$(btn).parent().parent();
	var newDiv=$(condiDivTag);
	div.append(newDiv);
}
function delCondi(btn){
	if(!confirm("确定删除该条件吗？"))return;
	var div=$(btn).parent();
	div.remove();
}
function delGroup(btn){
	if(!confirm("确定删除该组吗？"))return;
	var div=$(btn).parent().parent();
	div.remove();
}
$(function(){

	win=getFromWindow(fromPageId);
	var rule=win.getRule();
	if(rule){
		//alert(eval(rule).type);
		getHtml($.parseJSON(rule),$("#group"));
	}else{
		$("#group").append($(btns));
	}
});
function initSelectHtml (){
	$.ajax({
		   type: "POST",
		   url: "e",
		   async:false,
		   data: "e=tlingx_option&m=items&code=LJYSF&lgxsn=1",
		   dataType:'json',
		   success: function(json){
			   cindiSelectHtml+="<select class='op'>";
				for(var i=0;i<json.length;i++){
					cindiSelectHtml+="<option value='"+json[i].value+"'>"+json[i].text+"</option>";
				}
				cindiSelectHtml+="</select>";
		   }
		});
	//alert(cindiSelectHtml);leftSelectItems
	var tempLeftSelectHtml="";
	$.ajax({
		   type: "POST",
		   url: "e",
		   async:false,
		   data: "e=tlingx_role&m=combo&lgxsn=1",
		   dataType:'json',
		   success: function(json){
				for(var i=0;i<json.length;i++){
					tempLeftSelectHtml+="<option value='"+json[i].value+"'>角色:"+json[i].text+"</option>";
				}
		   }
		});
	$.ajax({
		   type: "POST",
		   url: handlerUrl,
		   async:false,
		   data: "c=leftSelectItems&lgxsn=1&entityCode="+currentEntityCode,
		   dataType:'json',
		   success: function(json){
			   leftSelectHtml+="<select class='left'>";
				for(var i=0;i<json.length;i++){
					leftSelectHtml+="<option value='"+json[i].value+"'>"+json[i].text+"</option>";
				}
				
				leftSelectHtml+=tempLeftSelectHtml+"</select>";
		   }
		});
	$.ajax({
		   type: "POST",
		   url: handlerUrl,
		   async:false,
		   data: "c=rightSelectItems&lgxsn=1&entityCode="+currentEntityCode,
		   dataType:'json',
		   success: function(json){
			   rightSelectHtml+="<select class='right'>";
				for(var i=0;i<json.length;i++){
					rightSelectHtml+="<option value='"+json[i].value+"'>"+json[i].text+"</option>";
				}
				rightSelectHtml+="</select>";
		   }
		});
}
function getHtml(obj,div){
	if(obj.type=="group"){
		
		var newDiv=$(divTag);
		if(isInit){
			newDiv.css("border-color","#"+getRandomColor());
			div.append(newDiv);
		}else{
			newDiv=div;
			isInit=true;
		}
		var arr=obj.group;
		for(var i=0;i<arr.length;i++){
			getHtml(arr[i],newDiv);
		}
	}else if(obj.type=="condi"){
		var temp=$(condiDivTag);
		temp.find(".left").val(obj.left);
		temp.find(".op").val(obj.op);
		
		var t1=temp.find(".right").find("option[value='"+obj.right+"']");
		if(!t1.html()){//当中介对象转为实体对象时，补上该项的值
			temp.find(".right").append("<option value='"+obj.right+"'>"+obj.right+"</option>");
		}
		temp.find(".right").val(obj.right);
		div.append(temp);
	}else if(obj.type=="op"){
		var temp=$(btns);
		temp.find("select").val(obj.op);
		div.append(temp);
	}else{
		
	}
}

function getJSON(obj){
	var json={};
	if(obj.attr("type")=="GROUP"){
		var array=new Array();
		var list=obj.children();
		for(var i=0;i<list.length;i++){
			var ret=getJSON($(list[i]));
			if(ret)
			array.push(ret);
		}
		json.group=array;
		json.type='group';
	}else
	if(obj.attr("type")=="CONDI"){
		json.left=obj.find(".left").val();
		json.right=obj.find(".right").val();
		json.op=obj.find(".op").val();
		json.type='condi';
	}else
	if(obj.attr("type")=="TOOLS"){
		var op=obj.find("select").val();
		json.op=op;
		json.type='op';
	}else{
		json=undefined;
	}
	return json;
}

function ok(){
	var temp=getJSON($("#group"));
	var str=JSON.stringify(temp);
	$.post(handlerUrl,{c:'saveProperty',code:currentEntityCode,id:currentEntityId,prop:'z_dataRule',value:str,oldvalue:""},function(json){
		lgxInfo(json.message);
		win.setRule(str);
		closeWindow();
	},"json");
	
}
function resizeIframeWin(w,h){
	//editor.setSize(w-16,h-86);
}
function getLeft(){
	//return "<select class='left'><option>当前用户ID</option></select>";
	return leftSelectHtml;
}
function getOp(){
	//return "<select class='op'><option>等于</option></select>";
	return cindiSelectHtml;
}

function getRight(){
	//return "<select class='right'><option>当前用户ID</option></select>";
	return rightSelectHtml;
}
function getJh(){
	return "<select class='jh'><option value='and'>且</option><option value='or'>或</option></select>";
}

function getRandomColor(){
	var r=Math.random()*255;
	var g=Math.random()*255;
	var b=Math.random()*255;
	var color=parseInt(r).toString(16)+parseInt(g).toString(16)+parseInt(b).toString(16);
	return color;
}
function lingxSubmit(){
	ok();
}
</script>

  </head>
  <body  style="margin:0px;padding:0px;background-color:#F7F7F7;">
<div id="group" type="GROUP" style="background-color:#F7F7F7;height:338px;overflow:auto;">

</div>
</body>
</html>
