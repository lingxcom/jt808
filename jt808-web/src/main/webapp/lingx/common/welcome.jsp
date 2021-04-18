<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实时监控</title>
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/jquery.js"></script>
<style type="text/css">
.div1{
width: 100%;
    margin: 0 45px 10px 10px;
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);

    -webkit-user-select:none;
-moz-user-select:none;
-ms-user-select:none;
user-select:none;
}
.div1 div{
text-align:left;
}
.div1 h1{
    font-size: 14px;
    color: #ADB6CA;
    margin:0px;
     margin-top:15px;
}
.div1 span{
font-size:30px;
font-weight: bold;
    margin-top: 5px;
    color: #ffb227;
}

.div1:hover{
    box-shadow: 0px 2px 4px 5px  rgba(100, 100, 100, 0.3);
}
.icon1{
margin: 0;
    padding: 0;
    border: 0;
    font: inherit;
    display: inline-block; 
     margin-left: 25px;  
     width: 80px;   
      height: 80px;
      background: url(lingx/images/safe.png) center no-repeat;
}
</style>
<script type="text/javascript">

$(function(){
	reload();
	$(window).bind("click",function(){
		if(window.parent&&window.parent.removeMenu){
		window.parent.removeMenu();
		}else if(window.parent.window.parent&&window.parent.window.parent.removeMenu){
			window.parent.window.parent.removeMenu();
		}
	});
});

function reload(){

	$.post("lingx/common/handler.jsp",{c:"server"},function(json){
		$("#cpu1").text(json.cpu.cpuNum);
		$("#cpu2").text(json.cpu.used +"%");
		$("#cpu3").text(json.cpu.sys+"%");
		$("#cpu4").text(json.cpu.free+"%");
		
		$("#mem1").text(json.mem.total +"GB");
		$("#mem2").text(json.mem.used+"GB");
		$("#mem3").text(json.mem.free+"GB");
		$("#mem4").text(json.mem.usage+"%");
		
		$("#jvm1").text(json.jvm.total +"MB");
		$("#jvm2").text(json.jvm.used+"MB");
		$("#jvm3").text(json.jvm.free+"MB");
		$("#jvm4").text(json.jvm.usage+"%");

		$("#sys1").text(json.sys.computerName);
		$("#sys2").text(json.sys.osName);
		$("#sys3").text(json.sys.computerIp);
		$("#sys4").text(json.sys.osArch);

		$("#jdk1").text(json.jvm.name);
		$("#jdk2").text(json.jvm.version);
		$("#jdk3").text(json.jvm.startTime);
		$("#jdk4").text(json.jvm.runTime);
		$("#jdk5").text(json.jvm.home);
		$("#jdk6").text(json.sys.userDir);
		
		$(".delTr").remove();
		for(var i=0;i<json.sysFiles.length;i++){
			var disk=json.sysFiles[i];
			$("#disk").append("<tr class='delTr'><td>"+disk.dirName+"</td><td>"+disk.sysTypeName+"</td><td>"+disk.typeName+"</td><td>"+disk.total+"</td><td>"+disk.free+"</td><td>"+disk.used+"</td><td>"+disk.usage+"%</td></tr>");
		}
	},"json");
}

var intervalProcess2=setInterval("reload()",60*1000);


function blockContextmen(){
console.log(window.event);
if(window.event&&window.event.target.tagName!="INPUT"&&window.event.target.tagName!="TEXTAREA"){
window.event.returnValue=false;
}
}
document.oncontextmenu=blockContextmen;
</script>
</head>
<body style="background-color:#d6e3f2;">
<div class="container-fluid" style="margin-top:0px;">

<!--  -->
<div class="row" style="margin-top:20px;">
<div  class="col-xs-6" >

<div class="panel panel-default div1">
  <div class="panel-heading">服务器信息</div>
  <table class="panel-body table ">
  <tr><td width="25%">服务器名称</td><td width="25%" id="sys1">-</td><td width="25%">操作系统</td><td id="sys2">-</td></tr>
  <tr><td>服务器IP</td><td id="sys3">-</td><td>系统架构</td><td id="sys4">-</td></tr>
</table>
</div>
</div>
<div  class="col-xs-6" >

<div class="panel panel-default div1">
  <div class="panel-heading">技术资料</div>
  <table class="panel-body table ">
  <tr><td>官方网站：<a href="https://www.lingx.com" target="_blank">https://www.lingx.com</a></td><td>技术文档：<a href="http://docs.lingx.com" target="_blank">http://docs.lingx.com</a></td></tr>
  <tr><td>视频教程：<a href="https://space.bilibili.com/482658194" target="_blank">https://space.bilibili.com/482658194</a></td><td>源码地址：<a href="https://github.com/lingxcom/lingx" target="_blank">https://github.com/lingxcom/lingx</a></td></tr>
</table>
</div>
</div>
</div>


<div class="row" style="margin-top:20px;">
<div  class="col-xs-6" >
<div class="panel panel-default div1">
  <div class="panel-heading">CPU</div>
  <table class="panel-body table ">
  <tr><th width="50%">属性</th><th>值</th></tr>
  <tr><td>核心数</td><td id="cpu1">-</td></tr>
  <tr><td>用户使用率</td><td id="cpu2">-</td></tr>
  <tr><td>系统使用率</td><td id="cpu3">-</td></tr>
  <tr><td>当前空闲率</td><td id="cpu4">-</td></tr>
</table>
</div>
</div>
<div  class="col-xs-6" >
<div class="panel panel-default div1">
  <div class="panel-heading">内存</div>
  <table class="panel-body table ">
  <tr><th width="33%">属性</th><th width="33%">内存</th><th>JVM</th></tr>
  <tr><td>总内存</td><td id="mem1">-</td><td id="jvm1">-</td></tr>
  <tr><td>已用内存</td><td id="mem2">-</td><td id="jvm2">-</td></tr>
  <tr><td>剩余内存</td><td id="mem3">-</td><td id="jvm3">-</td></tr>
  <tr><td>使用率</td><td id="mem4">-</td><td id="jvm4">-</td></tr>
</table>
</div>
</div>
</div>


<!--  -->
<div class="row" style="margin-top:20px;">
<div  class="col-xs-12" >
<div class="panel panel-default div1">
  <div class="panel-heading">Java虚拟机信息</div>
  <table class="panel-body table ">
  <tr><td width="25%">JDK名称</td><td id="jdk1">-</td width="25%"><td width="25%">JDK版本</td><td id="jdk2">-</td></tr>
  <tr><td>启动时间</td><td id="jdk3">-</td><td>运行时长</td><td id="jdk4">-</td></tr>

  <tr><td>安装路径</td><td colspan="3" id="jdk5">-</td></tr>
  
  <tr><td>项目路径</td><td colspan="3" id="jdk6">-</td></tr>
</table>
</div>
</div>
</div>

<!--  -->
<div class="row" style="margin-top:20px;">
<div  class="col-xs-12" >
<div class="panel panel-default div1">
  <div class="panel-heading">磁盘状态</div>
  <table id="disk" class="panel-body table ">
  <tr><th>盘符路径</th><th>文件系统</th><th>盘符类型</th><th>总大小</th><th>可用大小</th><th>已用大小</th><th>已用百分比</th></tr>
<!--   <tr><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td></tr> -->
</table>
</div>
</div>
</div>

<!-- 
<div class="row" style="margin-top:20px;">
<div  class="col-xs-4" >
<div class="div1" style="height:200px;"  > </div>
</div>
<div  class="col-xs-4" >
<div class="div1" style="height:200px;" > </div>
</div>
<div  class="col-xs-4" >
<div class="div1" style="height:200px;" > </div>
</div>
</div>
 -->
 
</div>

</body>
</html>