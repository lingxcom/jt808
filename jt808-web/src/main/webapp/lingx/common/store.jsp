<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lingx.core.model.bean.UserBean,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate,com.lingx.core.service.*" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体列表</title>
<base href="<%=basePath%>">
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript">
var extParams={node:6};
Ext.onReady(function(){
	 Ext.define('Entity',{
	        extend: 'Ext.data.Model',
	        fields: [
	            {name: 'name', type: 'string'},
	            {name: 'image', type: 'string'},
	            {name: 'price', type: 'string'},
	            {name: 'file_url', type: 'string'},
	            {name: 'content', type: 'string'},
	            {name: 'create_time', type: 'string'}
	        ]
	    });
	 var entityStore=Ext.create('Ext.data.Store', {
	     model: 'Entity',
	     
	     proxy: {
	    	 actionMethods: {
              create : 'POST',
              read   : 'POST', // by default GET
              update : 'POST',
              destroy: 'POST'
          },
	         type: 'ajax',
	         url: "lingx/common/store_handler.jsp?c=grid",
	         reader: {
	             type: 'json',
		            root: 'rows',
		            totalProperty: 'total'
	         }
	     },
	     autoLoad: true,
	     listeners:{
	    	 load:function(){
	    		 //lgxInfo("load commpited");
	    		 //Ext.getCmp("entityGrid_1").getSelectionModel().select(0);
	    	 },beforeload:function(){
	    		Ext.apply(entityStore.proxy.extraParams,extParams);  
	    	}
	     }
	 });
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			id:"tree",
			region:"west",
            split: true,
            width: 240,
            minWidth: 175,
            maxWidth: 400,
            animCollapse: true,
            margins: '2 0 2 2',
            animate:false,
            autoScroll: true,
            rootVisible: false,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: "lingx/common/store_handler.jsp?c=tree",
        	         reader: {
        	             type: 'json'
        	         }
        	     },
        	     root: {
        	         text: "ROOT",
        	         id:6,
        	         expanded: true
        	    },
        	     autoLoad: true,
        	     listeners:{
        	    	datachanged:function(){
        	    		//if(Ext.getCmp("tree").getSelectionModel().getCount()==0)
      		    		//Ext.getCmp("tree").getSelectionModel().select(0);
      		    	}
         	     }
        	}),
			xtype:"treepanel",
			listeners:{
   	    	 
  		    	select:function(view,record,item,index,event,obj){
  		    		extParams.node=record.data.id;
  		    		entityStore.loadPage(1);
	        	}
     	     }
		},{
			region:"center",
            margins: '2 2 2 0',
            xtype:'grid',
            columns: [{ xtype: 'rownumberer',width:26}, 
                { header: '名称',width:200, dataIndex: 'name' },
                { header: '图片',width:200, dataIndex: 'image' ,renderer:function(value,p,record){
                	var url="http://mdd.lingx.com/"+eval(value)[0].value;
	            	return "<a target='_blank' href='"+url+"'><image width='80' src='"+url+"' /></a>";
	            }},
                { header: '价格(元)', dataIndex: 'price' ,renderer:function(value,p,record){
                	return "¥ "+parseFloat(value).toFixed(2);
                }},//
                { header: '描述',width:200, dataIndex: 'content' },
                { header: '发布时间',width:200, dataIndex: 'create_time',renderer:function(value,p,record){
                	return ft(value);
                } },
                { header: '操作', dataIndex: 'name',renderer:function(value,p,record){
	            	return "<a href='javascript:;' onclick='buy(\""+record.data.id+"\")'>购买并安装</a>";
	            }  }
            ],
      store: entityStore,
      bbar: Ext.create('Ext.PagingToolbar', {
          store: entityStore,
      }),
      dockedItems_bak: [{
	        xtype: 'toolbar',
          //border:false,
	        items: [
	                {id:"text", xtype: 'textfield',width:140, emptyText: '查询条件',listeners:{
	                	specialkey: function(field, e){
	                		if(e.getKey()== e.ENTER){
	                			extParams={param:Ext.getCmp("text").getValue()};
      	                	entityStore.load();
	                		}
	                	}
	                }},
	                { xtype: 'button', text: '查询' ,handler:function(){
	                	extParams={param:Ext.getCmp("text").getValue()};
	                	entityStore.load();
	                },listeners:{
							specialkey: function(field, e){
			                    if (e.getKey() == e.ENTER) {
			                    	extParams={param:Ext.getCmp("text").getValue()};
          	                	entityStore.load();
			                    }
			                }
					}}
	            ],
	        dock: 'top',
	        displayInfo: true
	    }
      //,{
      //    	        xtype: 'pagingtoolbar',
      //    	        store: entityStore,   // GridPanel使用相同的数据源
      //    	        dock: 'bottom',
      //    	        displayInfo: true
      //    	    }
      ]
			}]
		});
});
function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14);
}

function buy(id){
	openWindow2("购买并安装","lingx/common/store_buy.jsp?id="+id);
}
</script>
</head>
<body>
</body>
</html>