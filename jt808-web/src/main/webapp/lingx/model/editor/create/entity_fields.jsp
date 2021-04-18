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
var tableName="<%=request.getParameter("tableName") %>";
var isComplete=false;
function lingxSubmit(){
	if(isComplete){
		lgxInfo("该对象模型已经是完成状态，不可以替换。");
	}else{
		window.location.href="<%=basePath%>lingx/model/editor/create/entity_config.jsp?pageid=${param.pageid}&tableName="+tableName;
	}
	
	
/* 	Ext.Msg.prompt('系统消息', '请输入数据表['+tableName+']的对象名称：', function(btn, text){
	    if (btn == 'ok'&&text){
	    	//if(!confirm("是否为数据表["+tableName+"]创建对象？"))return;
	    	Ext.Ajax.request({url:handlerJsp,params:{c:"createEntity",code:tableName,name:text},success:function(res){
	    		var json=Ext.JSON.decode(res.responseText);
	    		lgxInfo(json.message);
	    		if(json.code==1){
	    			getFromWindow(fromPageId).reloadGrid();
	    			closeWindow();
	    			
	    		}
	    	}});
	    	
	    }
	}); */
	
}
Ext.onReady(function(){
	Lingx.post(handlerJsp,{c:"isComplete",code:tableName},function(json){
		isComplete=json.isComplete;
	});
	
	var store = Ext.create('Ext.data.Store', {
		//model:'DbTableField',
		proxy: {
	         type: 'ajax',
	         url: handlerJsp+'?c=getColumns&table='+tableName,
	         reader: {
	             type: 'json',
	             root: 'rows',
		         totalProperty: 'total'
	         }
	     },
	     autoLoad: true
	});
	Ext.create("Ext.Viewport",{
		layout:'fit',
		border:false,
		items:[{
			id:'grid',
			border:false,
			store: store,
			xtype:"grid",
			columns: [
                       { header: '代码',  dataIndex: 'column_name1' },
                       { header: '类型', dataIndex: 'column_type1' },
                       { header: '空值', dataIndex: 'is_nullable1' },
                       { header: '注释', dataIndex: 'column_comment1' }
                   ]
		}
		       
		       ]
	}).show();
});
</script>
</head>
<body>
</body>
</html> 