<%@page import="com.lingx.core.Constants,com.lingx.core.model.*,com.lingx.core.service.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	ILingxService lingx=spring.getBean(ILingxService.class);
	if(!lingx.isSuperman(request))return;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑实体</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">
var fromPageId='${param.pageid}';
var entityCode='${param.code}';
var handlerJsp="<%=basePath%>lingx/model/editor/handler.jsp";
Ext.onReady(function(){
Ext.create("Ext.Viewport",{
	id:'viewport',
	layout:'fit',
	items:[{
		id:"tree",
        animate:false,
        border:false,
        autoScroll: true,
        rootVisible: false,
        store: Ext.create('Ext.data.TreeStore', {
    		proxy: {
    	         type: 'ajax',
    	         url: handlerJsp+"?c=getFuncTree&code="+entityCode,
    	         reader: {
    	             type: 'json'
    	         },
    	     },
    	     root: {
    	         text: "ROOT",
    	         id:0,
    	         expanded: true
    	    },
    	     autoLoad: true,
    	     listeners:{
 		    	load:function(){
 		    	}
    	     }
    	}),
		xtype:"tree",
        listeners:{
        	checkchange:function(record,checked){
				 if (checked) {
	                 record.bubble(function(parentNode) {
	                     parentNode.set('checked', true);
	                     var node=parentNode;
	                     if(entityCode!=node.data.id&&node.data.id!=0)
	                     Lingx.post(handlerJsp,{c:"addFunc",module:entityCode,func:node.data.id},function(json){});
		                    
	                 });
	                 record.cascadeBy(function(node) {
	                     node.set('checked', true);
	                     if(entityCode!=node.data.id)
	                     Lingx.post(handlerJsp,{c:"addFunc",module:entityCode,func:node.data.id},function(json){});
	                    
	                 });
	                 record.expand();
	                 record.expandChildren();
	             } else {
	                 record.collapse();
	                 record.collapseChildren();
	                 record.cascadeBy(function(node) {
	                     node.set('checked', false);
	                     if(entityCode!=node.data.id&&node.data.id!=0)
	                     	Lingx.post(handlerJsp,{c:"delFunc",module:entityCode,func:node.data.id},function(json){});
	                     else
	                    	 Lingx.post(handlerJsp,{c:"delFunc",module:entityCode+"_object",func:'-'},function(json){});
	                 });
	                 record.bubble(function(parentNode) {
	                    var childHasChecked=false;
	                    var childNodes = parentNode.childNodes;
	                    if(childNodes || childNodes.length>0){
	                        for(var i=0;i<childNodes.length;i++){
	                            if(childNodes[i].data.checked){
	                                childHasChecked= true;
	                                break;
	                            }
	                        }
	                    }
	                    if(!childHasChecked){
	                         parentNode.set('checked', false);
		                     var node=parentNode;
		                     if(entityCode!=node.data.id)
			                     	Lingx.post(handlerJsp,{c:"delFunc",module:entityCode,func:node.data.id},function(json){});
			                     else
			                    	 Lingx.post(handlerJsp,{c:"delFunc",module:entityCode+"_object",func:'-'},function(json){});
	                     }
	                 });
	                
	             }
			},
        	itemdblclick:function(view,record,item,index,event,obj){
        		
        	}
        },
	}]
});
});
</script>
</head>
<body>
</body>

</html>