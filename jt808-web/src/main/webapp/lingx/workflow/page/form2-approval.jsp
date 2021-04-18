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

public String fdate(Object obj){
	String str=obj.toString();
	return str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6,8);
}

public String last2(String str){
	return str.substring(str.length()-2,str.length());
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
	List<Map<String,Object>> opinionList=(List<Map<String,Object>>)request.getAttribute("opinionList");
	int taskType=Integer.parseInt(request.getAttribute("task_type").toString());
	int authType=Integer.parseInt(request.getAttribute("auth_type").toString());
	int defineType=Integer.parseInt(request.getAttribute("define_type").toString());
	String model=request.getAttribute("model").toString();//有form 与 view 两种模式
	String taskId="",instanceId="";
	Map<String,Object> task=(Map<String,Object>)request.getAttribute("task");
	Map<String,Object> user=(Map<String,Object>)request.getAttribute("user");

UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
	String orgUserJSON=getOrgUserJSON(userBean,jdbc,orgService);
	request.setAttribute("orgUserJSON", orgUserJSON);
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
<title>任务审批</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<link href="js/mui/css/mui.min.css" rel="stylesheet">
<link href="js/mui/plugin/css/mui.picker.min.css" rel="stylesheet">
<link href="js/mui/css/timeline.css" rel="stylesheet">
<link href="lingx/js/resources/css/workflow.css" rel="stylesheet">
<script type="text/javascript" src="js/mui/js/mui.min.js"></script>
<script type="text/javascript" src="js/mui/plugin/js/mui.picker.all.js"></script>


<script type="text/javascript" src="lingx/js/rootApi_mobile.js"></script>

<script type="text/javascript">
var values=${params_json};
var opinion=${opinion};
var defineTaskId="${define_task_id}";
var instanceId="${instance_id}";
var taskId="${task_id}";
var authTag=",${auth_tag},";
var currentUserId='${SESSION_USER.id}';
var orgUserJSON=${orgUserJSON};
var handlerJsp="lingx/workflow/page/handler.jsp";

function setFormValue(){
	$.each(values,function(index,obj){
		$("input[name='"+obj.name+"']").val(obj.value);
		$("select[name='"+obj.name+"']").val(obj.value);
		$("textarea[name='"+obj.name+"']").val(obj.value);
		$("#"+obj.name).attr("src",obj.value);//签章功能有用到
	});
}
$(function(){  
	mui('.mui-scroll-wrapper').scroll();
	var ali = $('.m-tab .m-tab-nav li');  
	var aDiv = $('.m-tab-bd .m-tab-panel');  
	ali.click(function(){    
		var _this = $(this);    //setTimeout();的作用是延迟某一段代码的执行   
		//$(this)方法属于哪个元素，$(this)就是指哪个元素 
		_this.addClass('m-active').siblings().removeClass('m-active');      
		//如果想用一组元素控制另一组元素的显示或者隐藏，需要用到索引      
		var index = _this.index();      
		aDiv.eq(index).addClass('m-active').siblings().removeClass('m-active');    

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
		el.empty();
		el.css("border","0px none");
		el.css("height","auto");
		el.css("width","auto");
		json=JSON.parse(json);
		el.append("<input id='uploadEl_"+json.name+"' name='uploadEl_"+json.name+"' type='hidden'>");
		el.append("<input id='uploadEl_"+json.name+"_val' class='lingx-wf-upload-filepath' name='uploadEl_"+json.name+"_val' type='hidden'>");
	});
	
	
	setFormValue();
	handlerDownloadTag();
});


function jsonRet(json){
	$(".m-footer-menu").hide();
	if(json.code==1){
		lgxInfo(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value[1].value},function(json){},"json");
			});
		}
	}else if(json.code==2){
		window.location.href="w?m=form&is_mobile=true&_TASK_ID="+json._TASK_ID;
	}else if(json.code==3){
		alert(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value[1].value},function(json){
					alert("操作成功");
					//reloadParentWindow();
					//window.close();
					closeWin();
				},"json");
			});
		}else{
			closeWin();
			
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

function ok(){
	var msg=prompt("审批意见：","同意，按规定办理 ");
	if(!msg){
		alert("审批意见不可为空");return;
	}
	$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:1,content_approve:msg},function(json){
		jsonRet(json);
	},"json");
}
function ok2(){
	var msg="已查阅";
	$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:3,content_approve:msg},function(json){
		jsonRet(json);
	},"json");
}
function no(){
	var msg=prompt("退回原因：","数据有误");
	if(!msg){
		alert("审批意见不可为空");return;
	}
	$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:2,content_approve:msg},function(json){
		jsonRet(json);
	},"json");
}
function zf(){
	if(!confirm("同意并转发该任务？"))return;
	openUserDialog(false,function(arr){
		if(!confirm("确定转发给"+arr[1].text+"？"))return;
		$.post("w",{m:"forward",_TASK_ID:taskId,nextUserId:arr[1].value,ret_approve:1,content_approve:"同意并转发给"+arr[1].text},function(json){
			jsonRet(json);
		},"json");
		//console.log(value);
	});
}
function deleteWF(){
	if(!confirm("确定删除该任务？"))return;
	$.post("w",{m:"delete",_TASK_ID:taskId},function(json){
		jsonRet(json);
	},"json");
}
function edit(){
	window.location.href="w?m=edit&is_mobile=true&_TASK_ID="+taskId;
}

function openUserDialog(isMuti,callback){
	var userPickerTemp = new mui.PopPicker({
		layer: 2
	});
	userPickerTemp.setData(orgUserJSON);
	userPickerTemp.show(callback);
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
<style type="text/css">
ul{
list-style:none;
margin:0px;
padding:0px;
}
</style>
</head>
<body>

<header id="default_header" class="mui-bar mui-bar-nav" style="background-color:#5b8dca;">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"  style="color:#fff;"></a><!--  mui-btn-link href="lingx/workflow/wap/wf-main.jsp" -->
			<a class=" mui-pull-right mui-icon mui-icon-chatbubble" style="color:#fff;" href="lingx/workflow/page/form2-comment.jsp?m=form&_TASK_ID=${task_id}&is_mobile=true"></a>
			<h1 class="mui-title" style="color:#fff;">${define.name }</h1>
</header>


<div class="m-page yzui-page" id="content" style="overflow:hidden;">
<div class="mui-scroll-wrapper">
<div class="mui-scroll">
	<div class="m-bd">
    	<div class="header">
        	<div class="mui-table">
                <div class="mui-table-cell mui-col-xs-12">
                    <div class="yzui-icon icon-lg icon-word bg-01 mui-pull-left"><span><%=last2(user.get("name").toString()) %></span></div>
                    <div class="userinfo">
                        <h4 class="fs18 mui-ellipsis mb12">${instance.name }</h4>
                        <p class="mui-h5 mui-ellipsis " >${user.orgname}</p>
                    </div>
                </div>
                
            </div>
        </div>
    	<div class="cnt">
        	<!--请假
        	<ul class="process-info">
            	<li>
                	<label>请假总天数：</label>
                    <p>2天</p>
                </li>
                <li>
                	<label>请假时间：</label>
                    <p>9月30日 06:00 - 10月1日 06:00</p>
                </li>
                <li>
                	<label>请假时间：</label>
                    <p>10月7日 06:00 - 10月8日 06:00</p>
                </li>
                <li>
                	<label>理由：</label>
                    <p>家中有事</p>
                </li>
            </ul>-->
            ${content3 }
            
            
            <p class="mui-text-right fs12">流程编号：${instance.serial_number }</p>
        </div>
    </div>
    
    <div class="m-tab mui-clearfix">
        <div class="m-tab-hd">
            <div class="m-tab-nav f-clearfix">
                <ul>
                    <li tapmode="" class="m-active">审批信息</li>
                    <li tapmode="" class="">讨论(${opinion_length })</li>
                </ul>
            </div>
        </div>
        <div class="m-tab-bd f-clearfix">
         <!--审批信息-->
            <div class="m-tab-panel m-active approval-process bg-lightgray" style="margin-top:-9px;padding-bottom: 20px;">
            
                <section id="cd-timeline" class="cd-container">
                <%
                for(Map<String,Object> map:history){
                	%>
                	<div class="cd-timeline-block">
                            <%if(!"开始".equals(map.get("name").toString())&&!"结束".equals(map.get("name").toString())){ %>
                        <div class="cd-timeline-img cd-picture"><%=last2(map.get("user_name").toString()) %></div>
                        <div class="cd-timeline-wrap mui-clearfix">
                            <div class="mui-h5 fs12"><%=map.get("name") %></div>
                             <div class="cd-timeline-content mui-clearfix">
                             
                             <%
                             if("2".equals(map.get("status").toString())){
                            	 %>
                                <span class="processing"><span class="mui-icon iconfont icon-daichuli mr5 fs18"></span>当前处理人：<%=map.get("user_name") %></span>
                            	  <%
                             } else{%>
                             <div class="mui-clearfix">
                                    <span class="processed mui-pull-left"><span class="iconfont icon-103 fc-lightgreen mr5 fs18"></span><%=map.get("user_name") %></span>
                                    <span class="cd-date mui-pull-right"><%=fdate(map.get("create_time")) %></span>
                                </div>
                                <p>处理意见：<%=map.get("ret_message") %></p>
                             <%}%>
                                
                            </div>
                          
                        </div>
                          <%}else{
                        	  %>
                        	  <div class="cd-timeline-img cd-begin">
                        </div>
                
                        <div class="cd-timeline-wrap cd-timeline-begin  mui-clearfix">
                            <div class="mui-h5">流程<%=map.get("name") %></div>
                        </div>
                        	   <%
                          }%>
                    </div>
                	 <%
                }
                %>
                	
                    
                </section>
                
                <!--提交完后，如果被驳回，那你可以选择撤消申请-->
                <!--<div class="u-row-btn mui-text-center">
                	<a class="fc-red">撤消</a>
                </div>-->
               <!--  <ul class="mui-table-view">
                    <li class="mui-table-view-cell mui-text-center">
                        <a class="fc-red" style="line-height:1.5;">撤消</a>
                    </li>
                </ul>
                
                <div class="mui-content-padded ml15">
                    <p class="fs12">当流程还未审批时，才可以撤消</p>
                </div> -->

            </div>
        	<!--讨论-->
        	<div class="m-tab-panel ">
        	<%
        	if(opinionList.size()==0){
        		%>
        		<p class="bg-white mui-text-center" style="margin-bottom:0; padding-top:20px; height:100px;">还没有人讨论</p>
        		<%
        	}else{
        		%>
        		<ul class="u-chat-item ">
        		<%
        		for(Map<String,Object> map:opinionList){
        			%>
        			<li class="u-chat-item-cell">
                        <a href="javascript:;">
                            <div class="yzui-icon icon-sm icon-word bg-01 mui-pull-left mr10">
                           <span><%=last2(map.get("user_name").toString()) %></span> 
                            </div>
                            <div class="u-chat-item-cnt">
                                <span class="name"><%=last2(map.get("user_name").toString()) %></span><span class="time" style="float:right;"><%=fdate(map.get("create_time")) %></span>
                                <p class="content"><%=map.get("content") %></p>
                            </div>
                        </a>
                    </li>
        			<%
        		}
        		%>
                    
                </ul>
        		<%
        	}
        	%>
            	
            </div>
           
        </div>
    </div>
    
</div>
</div> 
</div>
<%
int auth=Integer.parseInt(request.getAttribute("auth_type").toString());//串行 1，并行4，审核2，查看3
int taskStatus=Integer.parseInt(task.get("status").toString());//任务状态，待领1，待办2，完成3，回退4，抄送5
if("form".equals(model)){//等待处理
	if(taskStatus==4){  //被驳回的，需要编辑表单
		%>
		<div class="m-footer-menu">
				<ul class="f-flex-wrap">
		<li class="f-flex-con"><a type="button" class="mui-btn mui-btn-block mui-btn-warning"  href="w?m=form&_TASK_ID=${task_id}&is_mobile=true&is_edit=true">已被驳回 请修改申请</a></li>
			</ul>
		</div>
		<%
	}else{
		if(auth==1){//编辑
			%>
			<div class="m-footer-menu">
			<ul class="f-flex-wrap">
			<li class="f-flex-con">
			
			<a class="mui-btn mui-btn-block mui-btn-success"  href="w?m=form&_TASK_ID=${task_id}&is_mobile=true&is_edit=true">修改表单</a>
			
			</li>
			
			<li class="f-flex-con">
			<a class="mui-btn mui-btn-block mui-btn-danger"  href="javascript:;" onclick="deleteWF();">删除流程</a>
			
			</li>
			</ul>
			</div>
			<%
		}else if(auth==2||auth==4){//审核
			%>
			<div class="m-footer-menu">
				<ul class="f-flex-wrap">
			    	<li class="f-flex-con"><button id='agreeBtn' type="button" class="mui-btn mui-btn-block mui-btn-primary" onclick="ok()">同意</button></li>
			    	<li class="f-flex-con"><button id='rejectBtn'type="button" class="mui-btn mui-btn-block mui-btn-warning" onclick="no()">退回</button></li>
			    	<li class="f-flex-con"><button id='editBtn'type="button" class="mui-btn mui-btn-block mui-btn-success" onclick="edit()">编辑</button></li>
			    	<li class="f-flex-con"><button id='zfBtn'type="button" class="mui-btn mui-btn-block mui-btn-royal" onclick="zf()">转发</button></li>
			        <!--<li class="f-flex-con"><button type="button" class="mui-btn mui-btn-block mui-btn-warning" onclick="">已被驳回 请修改申请</button></li>-->
			    </ul>
			</div>
			<%
		}else if(auth==3){//查看
			%>
			<div class="m-footer-menu">
				<ul class="f-flex-wrap">
			    	<li class="f-flex-con"><button id='agreeBtn' type="button" class="mui-btn mui-btn-block mui-btn-success" onclick="ok2()">已阅</button></li>
			    	<li class="f-flex-con"><button id='zfBtn'type="button" class="mui-btn mui-btn-block mui-btn-royal" onclick="zf()">转发</button></li>
			        <!--<li class="f-flex-con"><button type="button" class="mui-btn mui-btn-block mui-btn-warning" onclick="">已被驳回 请修改申请</button></li>-->
			    </ul>
			</div>
			<%
		}
	}

} %>



${attach_html }
</body>

</html>