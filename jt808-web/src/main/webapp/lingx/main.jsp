<%@page import="com.lingx.core.service.*,java.util.List,java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	String logoutUrl="d?c=logout";
	try{
		ISsoCasService ssocas=spring.getBean(ISsoCasService.class);
		logoutUrl=ssocas.getLogoutUrl();
	}catch(Exception e){}
	request.setAttribute("logoutUrl", logoutUrl);
	
	

	String lanuage="";
	if(session.getAttribute("SESSION_LANGUAGE")!=null){
		lanuage=session.getAttribute("SESSION_LANGUAGE").toString();
	}
	II18NService i18n=spring.getBean(II18NService.class);
	List<Map<String,Object>> list=i18n.getLanuages();
	String select="<select style=\"border:0px;none;\" onchange=\"setLanuage(this.value)\">";
	for(Map<String,Object> map:list){
		select+="<option "+(lanuage.equals(map.get("lanuage").toString())?"selected":"")+" value=\""+map.get("lanuage")+"\">"+map.get("name")+"</option>";
	}
	select+="</select>";
	if(list.size()<=1)select="";
	request.setAttribute("select", select);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>${SESSION_USER.app.name }</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="lingx/js/rootApi.js?1234"></script>
<style type="text/css">
.leftmenu {
        width:100%;
        overflow:auto;
        padding:0px;
        text-align:center;
}
.leftmenu li {
        display: inline;
        white-space: nowrap;
}

.leftmenu span,
.leftmenu a:active,
.leftmenu a:visited,
.leftmenu a:link {
	display: block;
	text-decoration: none;
	margin: 6px 10px 6px 0px;
	padding: 2px 6px 2px 6px;
	color: #00527f;
}

.leftmenu a:hover {
	color: red;
}

</style>
<script type="text/javascript">
setInterval("keepSession()", 5*60*1000); //隔5分钟访问一次
var logo='${SESSION_USER.app.logo}';
var logoIMG='';
if(logo){
	logoIMG='<img src="'+logo+'" height="40" style="margin-top:10px;margin-right:10px;"/>';
}
function keepSession(){
	Lingx.post("d",{},function(json){
	});
}
function setLanuage(lanuage){
	Lingx.post("lingx/common/handler.jsp",{c:"lanuage",lanuage:lanuage},function(json){
		window.location.href="d?c=i";
	});
}
function getItems(list){
	var str="";
	for(var i=0;i<list.length;i++){
		if(list[i].text&&list[i].uri!='e?e=public&m=public'){
			str=str+"<li><a style='font-size:16px;line-height:28px;' href='javascript:;' onclick='addTab(\""+list[i].itemId+"\",\""+list[i].text+"\",\""+list[i].uri+"\")'>"+list[i].text+"</a></li>";
		}else if(list[i]=="-"){
			str=str+"<li><hr style='height:1px;border:none;border-top:1px dotted #aaa;'/></li>";
			
		}
		if(list[i].menu){
			str=str+getItems(list[i].menu);
		}
	}
	return str;
}
Ext.onReady(function(){
	Lingx.post("d",{c:"menu"},function(json){
		var menus=[];
		for(var i=0;i<json.length;i++){
			menus.push({title:json[i].text,html:"<ul class='leftmenu'>"+getItems(json[i].menu)+"</ul>",iconCls:"Resultsetnext",bodyStyle:"padding:0px; overflow:auto;"});
		}
    Ext.create('Ext.Viewport', {
    	id:'viewport',
    	layout: 'border',
    	items:[{
    			region:'north',
               // margins: '5 5 5 5',
    			//height:100,
    			height:72,
                split: true,
    			border:false,
    			bodyStyle: 'background:#4088c8;',//89b9e1 images/obd/top.jpg 347cc6
				html:'<div style="-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;position:absolute;padding-left:10px; left:0; top:0;color:#fff;font-size:24px;line-height:60px;height:60px;vertical-align:top;font:"Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,"WenQuanYi Micro Hei",Verdana,sans-serif,"\5B8B\4F53"><div style="height:60px;float:left;">'+logoIMG+'</div><div style="line-height:60px;height:60px;float:left;font-size:20px">${SESSION_USER.app.name}</div></div>'// 
				
				+'<div style="position:absolute; right:0; top:0;color:#fff;font-size:14px;line-height:24px;height:24px;">'
				+'${select}'
				+'</div>'
				
				+'<div style="position:absolute; right:0; bottom:0;color:#fff;font-size:14px;line-height:28px;height:28px;">'
				+'<%=i18n.text("当前用户")%>：<a style="color:#fff;font-size:14px;" href="javascript:;" onclick="javascript:openWindow(\'<%=i18n.text("修改用户信息")%>\',\'e?e=tlingx_user&m=editSelf&id=${SESSION_USER.id}\');";>${SESSION_USER.name}</a>[${SESSION_USER.account}] '
				+'&nbsp;|&nbsp;'
				+'<a style="color:#fff;font-size:14px;" href="javascript:;" onclick="javascript:openWindow(\'<%=i18n.text("修改密码")%>\',\'e?e=tlingx_user&m=editPassword\');";><%=i18n.text("修改密码")%></a>'
				+'&nbsp;|&nbsp;'
				+'<a style="color:#fff;font-size:14px;" href="${logoutUrl}"><%=i18n.text("退出系统")%></a>&nbsp;</div>'
				+'</div>'
    		},{
    			region:"west",
    			//margins: '2 0 2 0',
    			width:200,
    			title:"系统菜单",
    			iconCls:"Anchor",
                split: true,
                collapsible: true,
    			defaults: {
    		        // applied to each contained panel
    		        bodyStyle: 'padding:15px'
    		    },
    		    layout: {
    		        // layout-specific configs go here
    		        type: 'accordion',
    		        titleCollapse: true,
    		        animate: true
    		    },
    		    items: menus
    		},
    	       {
    			region:'center',
    			margins: '2 0 2 0',
    			id: 'tabpanel',
    			xtype: 'tabpanel',
    			region: 'center', // a center region is ALWAYS required for border layout
    			//tabPosition:'bottom',
               // deferredRender: false,
                activeTab: 0,     // first tab initially active
                items: [{
                	id:"<%=i18n.text("首页")%>",
                	html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="${SESSION_USER.app.indexPage}"> </iframe>',
                    title: '<%=i18n.text("首页")%>',
                    autoScroll: true
                }]
    		}]
       
    }).show();

	});
});

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
</script>
</head>
<body>
	
</body>
</html> 