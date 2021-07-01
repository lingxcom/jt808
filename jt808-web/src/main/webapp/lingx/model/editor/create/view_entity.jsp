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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>创建实体对象</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">

var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/model/editor/handler.jsp";
function lingxSubmit(){
	var tree=Ext.getCmp("tree");
	if(tree.getSelectionModel().getSelection().length==1){
		window.location.href="<%=basePath%>lingx/model/editor/create/entity_fields.jsp?pageid=${param.pageid}&tableName="+tree.getSelectionModel().getSelection()[0].getId();
	}else{
		lgxInfo("请选择数据表");
	}
	//console.log(tree.getSelectionModel().getSelection().length);
	//alert(tree.getSelectionModel().getSelection()[0].getId());
}
Ext.onReady(function(){
	 Ext.define('DbTable',{
	        extend: 'Ext.data.Model',
	        fields: [
	            {name: 'id', type: 'string'},
	            {name: 'text', type: 'string'}
	        ]
	    });
	var store = Ext.create('Ext.data.TreeStore', {
		model:'DbTable',
		proxy: {
	         type: 'ajax',
	         url: handlerJsp+'?c=getViewTree',
	         reader: {
	             type: 'json'
	         },
	     },
	     root: {
	         text: '数据表',
	         id: '00',
	         expanded: true
	    },

	     autoLoad: true
	});
	Ext.create("Ext.Viewport",{
		layout:'fit',
		border:false,
		items:[{
			id:'tree',
			border:false,
			store: store,
			xtype:"tree"
		}
		       
		       ]
	}).show();
});
</script>
</head>
<body>
</body>
</html> 