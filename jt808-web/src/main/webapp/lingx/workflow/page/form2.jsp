<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
public String getOrgUserJSON(UserBean userBean,JdbcTemplate jdbc,IOrgService orgService){
	String sql="select name text,id value from tlingx_org where id in %s order by orderindex asc";
	String sql2="select name text,id value from tlingx_user where id in(select user_id from tlingx_userorg where org_id=?) order by orderindex asc";
	String temp[]=orgService.getAllOrgIdsByAppid(userBean.getApp().getId());
	List<Map<String,Object>> list=jdbc.queryForList(String.format(sql,JSON.toJSONString(temp).replace("\"", "'").replace("[", "(").replace("]", ")")));
	for(Map<String,Object> map:list){
		map.put("children",jdbc.queryForList(sql2,map.get("value")));
	}
	return JSON.toJSONString(list);
}


public List<Map<String,Object>> getOrgTree(Object fid,JdbcTemplate jdbc){
	String sql="select name text,id value from tlingx_org where fid =? order by orderindex asc";
	List<Map<String,Object>> list=jdbc.queryForList(sql,fid);
	for(Map<String,Object> map:list){
		map.put("children", getOrgTree(map.get("value"),jdbc));
	}
	return list;
}
%>    
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	if(session.getAttribute("SESSION_USER")==null){
		response.sendRedirect(basePath+"lingx/wap/login.jsp");
		return;
	}
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean(JdbcTemplate.class);
ILingxService lingx=applicationContext.getBean(ILingxService.class);
IOrgService orgService=applicationContext.getBean(IOrgService.class);
	List<Map<String,Object>> list=(List<Map<String,Object>>)request.getAttribute("params");
	List<Map<String,Object>> history=(List<Map<String,Object>>)request.getAttribute("history");
	List<Map<String,Object>> attachment=(List<Map<String,Object>>)request.getAttribute("attachment");
	int taskType=Integer.parseInt(request.getAttribute("task_type").toString());
	int authType=Integer.parseInt(request.getAttribute("auth_type").toString());
	int defineType=Integer.parseInt(request.getAttribute("define_type").toString());
	String model=request.getAttribute("model").toString();//有form 与 view 两种模式
	String taskId="",instanceId="";
	Map<String,Object> task=(Map<String,Object>)request.getAttribute("task");

UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
	String orgUserJSON=getOrgUserJSON(userBean,jdbc,orgService);
	request.setAttribute("orgUserJSON", orgUserJSON);
	
	String rootid=userBean.getApp().getOrgRootId();
	List<Map<String,Object>> list11=jdbc.queryForList("select name text,id value from tlingx_org where id =? order by orderindex asc",rootid);
	for(Map<String,Object> map:list11){
		map.put("children", getOrgTree(map.get("value"),jdbc));
	}
	
	request.setAttribute("orgJSON", JSON.toJSONString(list11));
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>"> 
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="viewport"
	content="initial-scale=1, width=device-width, maximum-scale=1, user-scalable=no">
<meta name="viewport"
	content="initial-scale=1.0,user-scalable=no,maximum-scale=1"
	media="(device-height: 568px)">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-touch-fullscreen" content="yes">
<meta name="full-screen" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="address=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务办理</title>
<style type="text/css">
.hidden_zs{
display:none;
}
</style>
</head>
<body>
<div id="loading" style="width:100%;position:fixed;top:50px;text-align:center;">
<!-- 正在加载，请稍侯... -->
</div>
<header  id="default_header" class="mui-bar mui-bar-nav hidden_zs" style="background-color:#5b8dca;">
			<a class="mui-action-back mui-btn mui-btn-link mui-pull-left" style="color:#fff;">后退</a>
			<a id="submitBtn" class="mui-btn mui-btn-link mui-pull-right" style="color:#fff;" onclick="lingxSubmit()">提交</a>
			<h1 class="mui-title" style="color:#fff;">${define.name }</h1>
</header>

<form id="form" action="w" method="post" class="hidden_zs">
<div style="dispaly:none;">
<input id="m" type="hidden" name="m"  />
<%
String temp;
for(Map<String,Object>map:list){
	temp=map.get("name").toString();
	if(temp.charAt(0)!='_')continue;
	if("_TASK_ID".equals(temp))taskId=map.get("value").toString();
	if("_INSTANCE_ID".equals(temp))instanceId=map.get("value").toString();
	if("_CURRENT_TASK_ID".equals(temp))continue;
%><input type="hidden" name="<%=map.get("name") %>" value="<%=map.get("value") %>"/>
<%
}
%>
<input type="hidden" name="_CURRENT_TASK_ID" value="${param._TASK_ID }"/><!-- 当前办理的任务ID -->
</div>
<div style="width:100%;height:45px; ">&nbsp;</div>

${content2 }


${attach_html }
</form>


<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<link href="js/mui/css/mui.min.css" rel="stylesheet">
<link href="js/mui/plugin/css/mui.picker.min.css" rel="stylesheet">
<script type="text/javascript" src="js/mui/js/mui.min.js"></script>
<script type="text/javascript" src="js/mui/plugin/js/mui.picker.all.js"></script>

<script type="text/javascript" src="lingx/js/rootApi_mobile.js"></script>

<link href="lingx/workflow/page/treeSelect.css" rel="stylesheet">
<script type="text/javascript" src="lingx/workflow/page/treeSelect.js"></script>
<script type="text/javascript">
var values=${params_json};
var opinion=${opinion};
var defineTaskId="${define_task_id}";
var instanceId="${instance_id}";
var taskId="${task_id}";
var authTag=",${auth_tag},";
var currentUserId='${SESSION_USER.id}';
var orgUserJSON=${orgUserJSON};
var orgJSON=${orgJSON};
var handlerJsp="lingx/workflow/page/handler.jsp";
var treeUsers=new selectedtree();
$(function(){
	//控件初始化
	init();
	//设置初始值
	 setFormValue();
	
	 treeUsers.init("", orgJSON,function(id){
			//var ret=[{value:"i1d",text:"这是结果"}];
			$.post(handlerJsp,{c:"getUsersByOrgId",orgid:id},function(json){
				treeUsers.pushRight(json.list);
			},"JSON");
			
		});
});



function lingxSubmit(){
	if(!confirm("确认要提交任务吗？")){return;}
	var isValid=true;
	var array=new Array();
	var currEl=null,nullEl=null;
	var ipts1=$("input[notNull='true']");
	var ipts2=$("select[notNull='true']");
	var ipts3=$("textarea[notNull='true']");
	
	for(var i=0;i<ipts1.length;i++){
		array.push(ipts1[i]);
	}
	
	for(var i=0;i<ipts2.length;i++){
		array.push(ipts2[i]);
	}
	
	for(var i=0;i<ipts3.length;i++){
		array.push(ipts3[i]);
	}
	
	for(var i=0;i<array.length;i++){
		currEl=$(array[i]);
		if(!currEl.prop("name"))continue;
		if(isValid){
			isValid=currEl.val();
			nullEl=currEl;
		}else{
			break;
		}
	}
	if(isValid){
		$("#m").val("submit");
		var array=$("form").serializeArray();
		var fparams={};
		for(var i=0;i<array.length;i++){
			fparams[array[i].name]=array[i].value;
		}
		fparams.is_mobile=true;
		$.post("w",fparams,function(json){
			jsonRet(json);
		},"json");
		//console.log(fparams);
		//$("#form").submit();
	}else{
		lgxInfo("有数据不可为空，请检查");
		nullEl.focus();
	}
	
	return true;
}

function jsonRet(json){
	$("#submitBtn").hide();
	if(json.code==1){
		lgxInfo(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value[1].value},function(json){},"json");
			});
		}
	}else if(json.code==2){
		window.location.href="w?m=form&_TASK_ID="+json._TASK_ID+"&is_mobile=true";
	}else if(json.code==3){
		alert(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value[1].value},function(json){
					alert("操作成功");
					 closeWin()
					//reloadParentWindow();
					//window.close();
				},"json");
			});
		}else{
			 closeWin();
			//window.location.href="lingx/workflow/wap/wf-main.jsp";
			//reloadParentWindow();
			//window.close();
		}
		
	}else{
		alert(json.message);
	}
}

function closeWin(){
	if(api){
		api.sendEvent({
            name:'reloadMsg'});
		api.closeWin();
	}else{
		window.location.href="lingx/workflow/wap/wf-main.jsp";
	}
	
}

function init(){

	(function($$) {
		$$.init();
	var btns = $$('.lingx-wf-date');
	btns.each(function(i, btn) {
		btn.addEventListener('tap', function() {
			var optionsJson = this.getAttribute('data-options') || '{}';
			var options = JSON.parse(optionsJson);
			var id = this.getAttribute('id');
			var val=$("#"+id).val();
			if(val){
				options.value=val;
			}
			var picker = new $$.DtPicker(options);
			picker.show(function(rs) {
				
				 $("#"+id).val(rs.text);
				
				picker.dispose();
			});
		}, false);
	});
	
	btns = $$('.lingx-wf-datetime');
	btns.each(function(i, btn) {
		btn.addEventListener('tap', function() {
			var optionsJson = this.getAttribute('data-options') || '{}';
			var options = JSON.parse(optionsJson);
			var id = this.getAttribute('id');
			var val=$("#"+id).val();
			if(val){
				options.value=val;
			}
			var picker = new $$.DtPicker(options);
			picker.show(function(rs) {
				
				 $("#"+id).val(rs.text);
				
				picker.dispose();
			});
		}, false);
	});
	
	btns = $$('.lingx-wf-time');
	btns.each(function(i, btn) {
		btn.addEventListener('tap', function() {
			var optionsJson = this.getAttribute('data-options') || '{}';
			var options = JSON.parse(optionsJson);
			var id = this.getAttribute('id');
			var val=$("#"+id).val();
			if(val){
				options.value=val;
			}
			var picker = new $$.DtPicker(options);
			picker.show(function(rs) {
				
				 $("#"+id).val(rs.text);
				
				picker.dispose();
			});
		}, false);
	});
	
	btns = $$('.lingx-wf-month');
	btns.each(function(i, btn) {
		btn.addEventListener('tap', function() {
			var optionsJson = this.getAttribute('data-options') || '{}';
			var options = JSON.parse(optionsJson);
			var id = this.getAttribute('id');
			var val=$("#"+id).val();
			if(val){
				options.value=val;
			}
			var picker = new $$.DtPicker(options);
			picker.show(function(rs) {
				
				 $("#"+id).val(rs.text);
				
				picker.dispose();
			});
		}, false);
	});
	//日期初始化
	//用户单选 初始化
	var userPicker = new $$.PopPicker({
		layer: 2
	});
	userPicker.setData(orgUserJSON);
	var btns1 = $$('.lingx-wf-user');
	btns1.each(function(i, btn) {
		var el=$(btn);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		
		btn.addEventListener('tap', function() {
			var id = this.getAttribute('id');
			userPicker.show(function(rs) {
				//console.log(rs);
				var textEl= $("#"+id);
				textEl.val(rs[1].text);
				 $("input[name='"+textEl.attr("ref-name")+"']").val(rs[1].value);
				// userPicker.dispose();
			});
		}, false);
	});
	
	//组织单选初始化
	var orgPicker = new $$.PopPicker({
		layer: 1
	});
	orgPicker.setData(orgUserJSON);
	var btns2 = $$('.lingx-wf-org');
	btns2.each(function(i, btn) {
		var el=$(btn);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		btn.addEventListener('tap', function() {
			var id = this.getAttribute('id');
			orgPicker.show(function(rs) {
				//console.log(rs);
				 var textEl= $("#"+id);
				textEl.val(rs[0].text);
				 $("input[name='"+textEl.attr("ref-name")+"']").val(rs[0].value);
				// userPicker.dispose();
			});
		}, false);
	});
	
	
	})(mui);
	//treeUsers 用户多选
	var users=$(".lingx-wf-users");
	$.each(users,function(ind,obj){
		var el=$(obj);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		el.bind("tap",function(){
			treeUsers.show(function(array){
				var text="",value="";
				for(var i=0;i<array.length;i++){
					text+=array[i].text+",";
					value+=array[i].value+",";
				}
				if(text.length>0){
					text=text.substring(0,text.length-1);
					value=value.substring(0,value.length-1);
				}
				el.val(text);
				val.val(value);
				//alert(JSON.stringify(ret));
			});
		});
	});
	//外部连接
	var links=$(".lingx-wf-link");
	$.each(links,function(ind,obj){
		var el=$(obj);
		var url=el.attr("url");
		url=Lingx.urlAddParams(url,{instance_id:instanceId});
		el.empty();
		el.append('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>');
	});
	//附件上传
	links=$(".lingx-wf-upload");
	$.each(links,function(ind,obj){
		var el=$(obj);
		//var url=el.attr("url");
		var json=el.attr("json");
		var disabled=!!el.attr("disabled");
		el.empty();
		el.css("border","0px none");
		el.css("height","auto");
		el.css("width","auto");
		json=JSON.parse(json);
		el.append("<input id='uploadEl_"+json.name+"' name='uploadEl_"+json.name+"' type='hidden'>");
		el.append("<input id='uploadEl_"+json.name+"_val' class='lingx-wf-upload-filepath' name='uploadEl_"+json.name+"_val' type='hidden'>");
		if(!disabled)
		el.append('<button type="button" '+(disabled?'disabled=""disabled"':'')+' onclick="openWindow(\'文件上传框\',\'lingx/template/upload/upload.jsp?cmpId=uploadEl_'+json.name+'\');">附件上传</button>');
	});
	
	//选择控件动态获取选项
	var selects=$("select");
	$.each(selects,function(index,obj){
		var el=$(obj);
		var etype=el.attr("refEntity");
		var url="";
		if("tlingx_optionitem"==etype){
			var code1=el.attr("json");
			url="e?e=tlingx_option&m=items&lgxsn=1&code="+code1;
		}else{
			url="e?e="+etype+"&m=combo&lgxsn=1";
		}
		var param=el.attr("json");
		try{
		$.post(url,param&&param.charAt(0)=='{'?$.parseJSON(param):{},function(json){
			el.empty();
			for(var i=0;i<json.length;i++){
				el.append("<option value='"+json[i].value+"'>"+json[i].text+"</option>");
			}
			setFormValue();
		},"json");
		}catch(e){}
	});
	//对话框列表单选
	var dialogoptions=$(".lingx-wf-dialogoption");
	$.each(dialogoptions,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		//var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		el.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=grid&cmpId="+id,{}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框列表多选
	var dialogoption2s=$(".lingx-wf-dialogoption2");
	$.each(dialogoption2s,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		//var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		el.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=combogrid2&cmpId="+id,{text:encodeURI(encodeURI(el.val())),value:valEl.val()}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框树型单选
	var dialogtrees=$(".lingx-wf-dialogtree");
	$.each(dialogtrees,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		//var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		el.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=tree&cmpId="+id,{}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框树型多选
	var dialogtree2s=$(".lingx-wf-dialogtree2");
	$.each(dialogtree2s,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		//var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		el.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=combotree2&cmpId="+id,{text:encodeURI(encodeURI(el.val())),value:valEl.val()}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	
	//输入框初始化
	var editors=$(".lingx-wf-ipt");
	if(",0,"!=authTag){
	for(var i=0;i<editors.length;i++){
		var obj=editors[i];
		var el=$(obj);
		var authCfg=","+el.attr("authCfg")+",";
		var authType=el.attr("authType")||true;
		if(authCfg){
			if(authType){
				//true
				if(authCfg.indexOf(authTag)>-1){
					el.removeProp("disabled");
					el.removeAttr("disabled");
				}else{
					el.prop("disabled","disabled");
					el.attr("disabled","disabled");
				}
			}else{
				//false
				if(authCfg.indexOf(authTag)>-1){
					el.prop("disabled","disabled");
					el.attr("disabled","disabled");
				}else{
					el.removeProp("disabled");
					el.removeAttr("disabled");
				}
			}
		}
	}
	
	$(".hidden_zs").show();
	$("#loading").hide();
	}
}
function setFormValue(){
	$.each(values,function(index,obj){
		$("input[name='"+obj.name+"']").val(obj.value);
		$("select[name='"+obj.name+"']").val(obj.value);
		$("textarea[name='"+obj.name+"']").val(obj.value);
		//$("#"+obj.name).attr("src",obj.value);//签章功能有用到

		handlerDownloadTag();
	});
}

function openUserDialog(isMuti,callback){
	var userPickerTemp = new mui.PopPicker({
		layer: 2
	});
	userPickerTemp.setData(orgUserJSON);
	userPickerTemp.show(callback);
}

function test(){
	openUserDialog(true,function(rs){
		alert(rs[1].text);
	});
}
function lingxSet(options){
	$("#"+options.cmpId).val(options.text);
	$("#"+options.cmpId+"_val").val(options.value);
	if(options.cmpId.indexOf("upload")==0){
		 //处理下载标签
		 handlerDownloadTag();
	}
}
//处理上传控件的下载标签
function handlerDownloadTag(){
	var els=$(".lingx-wf-upload-filepath");
	$.each(els,function(ind,obj){
		var el=$(obj);
		if(el.val()){
			var as=el.parent().find(".lingx-wf-upload-download");
			as.remove();
			//el.after("<a target='_blank' href='"+el.val()+"' class='lingx-wf-upload-download' style='color:red;'>附件下载</a><br/>");
			var array=$.parseJSON(el.val());
			for(var i=0;i<array.length;i++){
				el.after("<li  class='lingx-wf-upload-download'><a target='_blank' href='"+array[i].value+"'>点击下载《"+array[i].text+"》</a></li>");
			}
			
		}
	});
}
</script>

</body>
</html>