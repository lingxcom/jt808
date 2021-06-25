<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
    
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=applicationContext.getBean(ILingxService.class);
	List<Map<String,Object>> list=(List<Map<String,Object>>)request.getAttribute("params");
	List<Map<String,Object>> history=(List<Map<String,Object>>)request.getAttribute("history");
	List<Map<String,Object>> attachment=(List<Map<String,Object>>)request.getAttribute("attachment");
	int taskType=Integer.parseInt(request.getAttribute("task_type").toString());
	int authType=Integer.parseInt(request.getAttribute("auth_type").toString());
	int defineType=Integer.parseInt(request.getAttribute("define_type").toString());
	String model=request.getAttribute("model").toString();//有form 与 view 两种模式
	String taskId="",instanceId="";
	Map<String,Object> task=(Map<String,Object>)request.getAttribute("task");
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>"> 
<meta content="width=device-width, initial-scale=0.5, maximum-scale=1, user-scalable=no" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务办理</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<link href="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>

<script type="text/javascript" src="<%=basePath %>js/ajaxfileupload.js"></script>

<script type="text/javascript" src="lingx/js/rootApi.js"></script>
<style type="text/css">
a:link{
text-decoration:none;
}
a:visited{
text-decoration:none;
}
a:hover{
text-decoration:none;
}
a:active{
text-decoration:none;
}
</style>
<script type="text/javascript">

var values=${params_json};
var opinion=${opinion};
var defineTaskId="${define_task_id}";
var instanceId="${instance_id}";
var taskId="${task_id}";
var authTag=",${auth_tag},";
var currentUserId='${SESSION_USER.id}';
</script>
<script type="text/javascript" src="lingx/workflow/page/form.js"></script>
</head>
<body>
<div class="container" style="width:1020px;padding-top:5px;">
<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">任务表单</a></li>
    <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">流程图示</a></li>
    <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">处理记录</a></li>
    <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">流程附件</a></li>
    <%if(defineType==2){
    %>
    <li role="presentation"><a href="#content" aria-controls="content" role="tab" data-toggle="tab">正文编辑</a></li>
	<%
    } %>
  </ul>

<div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="home" style="padding-top:10px;">
<center>
<form id="form" action="w" method="post">
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
<table>
<% if("form".equals(model)){ %>
<tr >
<td align="left" width="50%"><!-- onclick="lingxFallback()" -->
<input onclick="lingxReload()" type="button" class="btn btn-primary" value="刷新" data-toggle="tooltip" data-placement="bottom" title="刷新当前界面的数据" />
<div class="btn-group" role="group">
    <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      操作
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
      <li><a href="javascript:;" onclick="lingxRollback()" data-toggle="tooltip" data-placement="bottom" title="流程回退，将当前流程往之前的任务回退" >1、任务退回</a></li>
      <li><a href="javascript:;" onclick="lingxComment()" data-toggle="tooltip" data-placement="bottom" title="任务评论，参与该流程的都可以看到" >2、发表评论</a></li>
      <li><a href="javascript:;" onclick="lingxUpload()" data-toggle="tooltip" data-placement="bottom" title="上传流程相关附件" >3、上传附件</a></li>
      <li><a href="javascript:;" onclick="lingxDelegate()" data-toggle="tooltip" data-placement="bottom" title="指定某一人来帮助您完成该任务">4、任务委托</a></li>
      <li><a href="javascript:;" onclick="lingxCC()" data-toggle="tooltip" data-placement="bottom" title="授权给某些人可以观察整个流程的执行情况">5、任务抄送</a></li>
    </ul>
  </div>
<!-- <input onclick="lingxRollback()" type="button" class="btn btn-warning" value="退回" data-toggle="tooltip" data-placement="bottom" title="流程回退，将当前流程往之前的任务回退" />
<input onclick="lingxComment()" type="button" class="btn btn-info" value="评论" data-toggle="tooltip" data-placement="bottom" title="任务评论，参与该流程的都可以看到" />
<input onclick="lingxUpload()" type="button" class="btn btn-warning" value="附件" data-toggle="tooltip" data-placement="bottom" title="上传流程相关附件" />
<input onclick="lingxDelegate()" type="button" class="btn btn-info" value="委托" data-toggle="tooltip" data-placement="bottom" title="指定某一人来帮助您完成该任务"/>
<input onclick="lingxCC()" type="button" class="btn btn-success" value="抄送" data-toggle="tooltip" data-placement="bottom" title="授权给某些人可以观察整个流程的执行情况"/>
 -->
<input onclick="lingxClose()" type="button" class="btn btn-danger" value="关闭" data-toggle="tooltip" data-placement="bottom" title="关闭当前窗口"/>


</td>
<td align="right" width="50%">
<%
if(authType==1){//编辑
%>
<input onclick="lingxSave()" type="button" class="btn btn-success" value="保存数据" data-toggle="tooltip" data-placement="bottom" title="只保存您在表单中填写的数据，工作流程不变" />
<input onclick="lingxSubmit()" type="button" class="btn btn-primary"  value="提交任务" data-toggle="tooltip" data-placement="bottom" title="提交任务是指：根据工作流程的定义，将该工作提交给下一人来处理"/>
<%
}else if(authType==2){//审批
%>
<input onclick="lingxSave()" type="button" class="btn btn-info" value="保存数据" data-toggle="tooltip" data-placement="bottom" title="只保存您在表单中填写的数据，工作流程不变" />
<input onclick="lingxApprove()" type="button" class="btn btn-success"  value="任务审批" data-toggle="tooltip" data-placement="bottom" title="任务审批：同意将进入下一任务"/>
<%
}else if(authType==3){//查看
%>

<input onclick="lingxView()" type="button" class="btn btn-success"  value="已阅" data-toggle="tooltip" data-placement="bottom" title="点击已阅进入下一任务"/>
<%
}else if(authType==4){
%>
<input onclick="lingxSave()" type="button" class="btn btn-success" value="保存数据" data-toggle="tooltip" data-placement="bottom" title="只保存您在表单中填写的数据，工作流程不变" />
<input onclick="lingxSubmit()" type="button" class="btn btn-primary"  value="提交任务" data-toggle="tooltip" data-placement="bottom" title="提交任务是指：根据工作流程的定义，将该工作提交给下一人来处理"/>

<%
}
%>
</td>
</tr>
<% } else if("view".equals(model)){
	%>
<tr >
<td align="left" width="50%"><%if("1".equals(task.get("status").toString())){%>
<input onclick="lingxClaim()" type="button" class="btn btn-success"  value="签收" data-toggle="tooltip" data-placement="bottom" title="接收属于自己的任务"/>
<%
} %>
<input onclick="lingxClose()" type="button" class="btn btn-danger" value="关闭" data-toggle="tooltip" data-placement="bottom" title="关闭当前窗口"/>

</td>
<td align="right" width="50%">

</td>
</tr>
<%
}%>
<tr>
<td colspan="2" align="center">
<div style="width:990px">
${content }
</div>
</td>
</tr>
<tr>
<td colspan="2" >
<hr/>
<div class="panel panel-default opinion">
  <!-- Default panel contents -->
  <div class="panel-heading">处理意见区 （共0条）</div>
  
  <!-- List group -->
  <ul class="list-group">
   
  </ul>
</div>

</td>
</tr>
</table>
</form>
</center>
</div>
    <div role="tabpanel" class="tab-pane" id="profile" style="padding-top:10px;">
	<div style="height:800px">
	<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="lingx/workflow/center/info.jsp?taskid=<%=taskId %>&processInstanceId=<%=instanceId %>"> </iframe>
	</div>
	</div>
    <div role="tabpanel" class="tab-pane" id="messages" style="padding-top:10px;">
<table class="table table-hover">
<tr>
<th>#</th><th>任务名称</th><th>处理人</th><th>状态</th><th>开始时间</th><th>完成时间</th>
</tr>
<%
int i=0;
for(Map<String,Object> map:history){
	out.println("<tr><td>"+(++i)+"</td><td>"+map.get("name")+"</td><td>"+map.get("user_name")+"</td><td>"+fStatus(map.get("status"))+"</td><td>"+fTime(map.get("stime"))+"</td><td>"+fTime(map.get("etime"))+"</td></tr>");
}
%>
<%!
public String fTime(Object o){
	if(o==null||"".equals(o.toString()))return "";
	String s=o.toString();
	return s.substring(0,4)+"-"+s.substring(4,6)+"-"+s.substring(6,8)+" "+s.substring(8,10)+":"+s.substring(10,12);
}
public String fStatus(Object o){
	int i=Integer.parseInt(o.toString());
	String ret="";
	switch(i){
	case 1:ret="<span style='color:red'>待领</span>";break;
	case 2:ret="<span style='color:red'>待办</span>";break;
	case 3:ret="<span style='color:green'>完成</span>";break;
	case 4:ret="<span style='color:#fc9816'>回退</span>";break;
	case 5:ret="<span style='color:blue'>抄送</span>";break;
	default:
	}
	return ret;
}
%>
</table>
	</div>
    <div role="tabpanel" class="tab-pane" id="settings" style="padding-top:10px;">

<table class="table table-hover">
<tr>
<th>#</th><th>附件名称</th><th>上传人</th><th>上传时间</th><th>操作</th>
</tr>
<%
 i=0;
for(Map<String,Object> map:attachment){
	out.println("<tr><td>"+(++i)+"</td><td>"+map.get("name")+"</td><td>"+map.get("user_name")+"</td><td>"+fTime(map.get("create_time"))+"</td><td><a target='_blank' href='"+map.get("path")+"'>下载附件</a></td></tr>");
}
if(attachment.size()==0){
	%>
	<tr><td colspan="5" align="center" >暂未上传附件</td></tr>
	<%
}
%>
</table>
	</div>
      <%if(defineType==2){
    %>
    <div role="tabpanel" class="tab-pane" id="content" style="padding-top:10px;">
	<iframe width="100%"  frameborder="0" height="800" src="<%=lingx.getConfigValue("lingx.workflow.form.content.url","http://www.lingx.com")+"?id="+request.getAttribute("instance_id")%>"></iframe>
	</div>
	<%
    } %>
  </div>
  </div>
  
  <div style="dispaly:none;">
  <div class="modal fade bs-sign-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
  <div class="modal-dialog modal-sm" style="width:600px;" >
   <ul class='list-group'>
      </ul>
  </div>
</div>
  </div>
</body>

</html>