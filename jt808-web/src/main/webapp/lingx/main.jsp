<%@ page import="java.net.*,java.io.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	II18NService i18n=spring.getBean(II18NService.class);
	UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
%>
<!DOCTYPE>
<html style="height:100%">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>${SESSION_USER.app.name }</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<link rel="stylesheet" type="text/css" href="lingx/js/resources/css/style.css">
<script type="text/javascript" src="lingx/js/rootApi.js?123"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<style type="text/css">
.div_1{
	padding: 4px 0;
    border-top: 1px solid hsla(0,0%,100%,.1);
    border-bottom: 1px solid hsla(0,0%,100%,.1);
    width: 100%;
    position: relative;
    white-space: nowrap;
    overflow: hidden;
    display:block;
}
.ul_1{
    overflow-y: auto;
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
}
.li_1{
cursor:pointer;
    color: #fff;
    font-size: 16px;
    white-space: nowrap;
    overflow: hidden;
    -webkit-transition: all .2s ease-out 0s;
    transition: all .2s ease-out 0s;
    width: 100%;
    height: 40px;
    line-height: 40px;
    box-sizing: border-box;
    display: list-item;
    text-align:center;
    -webkit-user-select:none;
-moz-user-select:none;
-ms-user-select:none;
user-select:none;
}

.li_2{
    color: #fff;
    white-space: nowrap;
    overflow: hidden;
    -webkit-transition: all .2s ease-out 0s;
    transition: all .2s ease-out 0s;
    width: 100%;
    height: 10px;
    line-height: 10px;
    box-sizing: border-box;
    display: list-item;
    text-align:center;
}

.li_1:hover{
background-color:#dbe6f4;
color:#333333;
}

.li_1_hover{
background-color:#fff;
color:#333333;
}

.product-item-name {
    display: inline-block;
    vertical-align: middle;
    width: 178px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    color: #fff;
    font-size: 12px;
    padding-right: 8px;
}
.menu_2{
position:fixed;
z-index:30001;
top:50px;
left:60px;
width:170px;
height:100%;
color:#333333;
background-color:#fff;
border-right:1px solid #4088c8;
}

.closeBtn{
position:fixed;
top:50px;
left:203px;
cursor:pointer;
width:16px;
height:16px;
color:#aaa;
background:url('lingx/images/close.png') no-repeat;
z-index:999;
}

.closeBtn:hover{
cursor:pointer;
width:16px;
height:16px;
color:#aaa;
background:url('lingx/images/close.png') no-repeat #e6725d;
}

</style>
<script type="text/javascript">
var menu;
Ext.onReady(function(){
Lingx.post("d",{c:"menu"},function(json){
menu=json;
var ul=$(".ul_1");
for(var i=0;i<menu.length;i++){
	var li=$("<li class='li_1' >"+menu[i].short_name+"</li>");
	ul.append(li);
// 	li.bind("mouseover",function(e){
		
// 	});
	li.bind("click",function(e){
		
		removeMenu();
		var name=($(this).html());
		menu2(name);
		$(this).addClass("li_1_hover");
		return false;
	});
}
Ext.create('Ext.Viewport', {
	id:'viewport',
	layout: 'fit',
	contentEl:"vp"
	}).show();
	
	
var tabs = Ext.widget('tabpanel', {
    renderTo: 'workArea',
    activeTab: 0,
    width:getApi().getWidth()-65,
    height:getApi().getHeight()-55,
    id: 'tabpanel',
    bodyStyle:"background:#fff;",
    border:false,
    items: [{
    	id:"<%=i18n.text("首页",session)%>",
    	html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="${SESSION_USER.app.indexPage}"> </iframe>',
        title: '<%=i18n.text("首页",session)%>',
        autoScroll: true
       // ,closable: true

    }]
});

$(window).bind("resize",function(){
	Ext.getCmp("tabpanel").setWidth(getApi().getWidth()-65);
	Ext.getCmp("tabpanel").setHeight(getApi().getHeight()-55);
});
});
});

function menu2(name){
	var div=$("<div class='menu_2'><div class='closeBtn'></div><ul class='ul_1' style='margin-top:15px;'></ul></div>");
	$("body").append(div);
	div.bind("mouseleave",function(){
		//removeMenu();
	});
	div.find(".closeBtn").bind("click",removeMenu);
	for(var i=0;i<menu.length;i++){
		if(menu[i].short_name==name){
			var list2=menu[i].menu;
			for(var j=0;j<list2.length;j++){
				if(list2[j].text){
				var li=$("<li class='li_1' style='color:#333;' id='"+list2[j].itemId+"' uri='"+list2[j].uri+"'>"+list2[j].text+"</li>");
				div.find(".ul_1").append(li);
				li.bind("click",function(){
					var temp=$(this);
					var id=temp.attr("id");
					var uri=temp.attr("uri");
					addTab(id,temp.html(),uri);
					removeMenu();
				});
				}else{
					var li=$("<li class='li_2' style='color:#333;' id='"+list2[j].itemId+"' ><hr style='height:1px;border:none;border-top:1px dotted #aaa;'></li>");
					div.find(".ul_1").append(li);
				}
			}
			break;
		}
	}
}

function removeMenu(){
	$(".menu_2").remove();
	$(".li_1_hover").removeClass("li_1_hover");
}
function stopEvent(e){
	e.stopPropagation();
}

function addTab(id,title,url){
	var tab=Ext.getCmp("tabpanel").queryById(id);
	if(tab){
		tab.show();
	}else{
		Ext.getCmp("tabpanel").add({
			id:id,
			html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
            title: title,

            closable: true,
            autoScroll: true
		}).show();
	}
}

function keepSession(){
	Lingx.post("d",{},function(json){
	});
}
setInterval("keepSession()", 5*60*1000); //隔5分钟访问一次

</script>
</head>
<body style="height:100%">

<div id="vp">
<!-- 头部工具栏 -->
<div style="background-color:#4088c8;height:60px;line-height:50px;position:fixed;top:0px;width:100%;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;" onclick="removeMenu()">
<div style="width:60px;height:50px;float:left;padding-top:5px;padding-left:10px;">
<%if(Utils.isNotNull(userBean.getApp().getLogo()) ){ %>
<img width="40" height="40" src="${SESSION_USER.app.logo }">
<%} %>
</div>

<div style="color:#fff;font-size:16px;font-weight:bold ;height:50px;line-height:50px;float:left;">${SESSION_USER.app.name }</div>

<div style="color:#fff;font-size:14px;height:50px;line-height:50px;margin-left:50px;position:fixed;top:0px;right:10px;">

<a style="text-decoration:none;color:#fff;font-size:14px;" href="javascript:;" onclick="javascript:openWindow('修改用户信息','e?e=tlingx_user&m=editSelf&id=${SESSION_USER.id}');">${SESSION_USER.name } </a>
&nbsp;| &nbsp;
<a style="text-decoration:none;color:#fff;font-size:14px;" href="javascript:;" onclick="javascript:openWindow('修改用户密码','e?e=tlingx_user&m=editPassword&id=${SESSION_USER.id}');">修改密码 </a>
&nbsp;|&nbsp;
<a style="text-decoration:none;color:#fff;font-size:14px;" href="d?c=logout">退出系统 </a>
</div>
</div>
<!-- 左侧菜单栏 -->
<div id="menu" style="background-color:#4088c8;height:100%;position:fixed;top:50px;left:0px;width:60px;;z-index:9999;" onclick="removeMenu()">
<div class="div_1">
<ul class="ul_1" onclick="return false;" style="margin-top:10px;">

</ul>
</div>
</div>
<!-- 工作区 -->
<div id="workArea" style="background-color:#dbe6f4;height:100%;position:fixed;top:50px;left:60px;width:100%;padding-top:5px;padding-left:5px;" onclick="removeMenu()">

</div>
</div>
</body>
</html> 