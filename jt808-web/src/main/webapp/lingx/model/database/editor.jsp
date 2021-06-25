<%@page import="com.lingx.core.Constants,com.lingx.core.service.*"%>
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
<title>数据库编辑工具</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
</head>
<script type="text/javascript">
var handlerJsp="<%=basePath%>lingx/model/database/handler.jsp";
var currentTable="tlingx_user";
var extParams={table:currentTable};
function reloadTree(){
	var storeTmp=Ext.getCmp("tree").getStore();
	storeTmp.getRootNode().remove();
	storeTmp.setRootNode({
	         text:"数据库",
	         id: "database",
	         iconCls:'Database',
         expanded: true
	});
}
function reloadGrid(){
	Ext.getCmp("datas").getStore().reload();
}
Ext.onReady(function(){
	var store = Ext.create('Ext.data.Store', {
		fields: ["id", "table_schema", "table_name", "column_name", "is_nullable", "column_type",
                 "column_key", "extra", "column_default", "column_comment"],
	    remoteSort: true,
	    autoLoad:true,
	    proxy: {
	    	actionMethods: {
                create : 'POST',
                read   : 'POST', // by default GET
                update : 'POST',
                destroy: 'POST'
            },

	    	type: 'ajax',
	        url: handlerJsp+'?c=getColumns',
	        reader: {
	        	type: 'json',
	            root: 'rows',
	            totalProperty: 'total'
	        }//,
	        //simpleSortMode: true
	    },
	    sorters: [{
	    }],
	    listeners:{
	    	beforeload:function(){
	    		Ext.apply(store.proxy.extraParams,extParams);  
	    	},
	    	load:function(){
	    	}
	    }
	});


    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToMoveEditor: 1,
        autoCancel: false
    });
	 var viewport = Ext.create('Ext.Viewport', {
		 padding:2,
         layout: 'border',
         items:[{
        	 id:"tree",
        	 region: 'west',
        	 width:240,
             split: true,
             minWidth: 175,
             maxWidth: 400,
             collapsible: true,
             animCollapse: true,
        	 title:"数据库资源",
        	   animate:false,
               autoScroll: true,
               store: Ext.create('Ext.data.TreeStore', {
            	   id:"treeStore",
           		proxy: {
           	         type: 'ajax',
           	         url: handlerJsp+'?c=getDbTree',
           	         reader: {
           	             type: 'json'
           	         },
           	     },
           	     root: {
           	         text:"数据库",
           	         id: "database",
           	         iconCls:'Database',
           	         expanded: true
           	    },
           	     autoLoad: true
           	}),
   			xtype:"tree",
   			dockedItems: [{
    	        xtype: 'toolbar',
    	        items: [
    	                {id:"text", xtype: 'textfield', emptyText: '查询条件',listeners:{
    	                	specialkey: function(field, e){
    	                		if(e.getKey()== e.ENTER){
    	                			
    	                		}
    	                	}
    	                }},
    	                { xtype: 'button',iconCls:"Zoom", text: '查询' ,handler:function(){
    	                	
    	                },listeners:{
    							specialkey: function(field, e){
    			                    if (e.getKey() == e.ENTER) {
    			                    	
    			                    }
    			                }
    					}}
    	            ],
    	        dock: 'top',
    	        displayInfo: true
    	    }
            ],
   			listeners:{
   				select:function(model,record,index,opts){
   					currentTable=record.data.id;
   					extParams["table"]=currentTable;
   					Ext.getCmp("datas").getStore().loadPage(1);
				}
   			}
         },{
        	 region:"center",
        	 layout:'border',
        	 border:false,
        	 items:[{
        		 id:"datas",
        		 region:"center",
                 title:"数据对象",
                 xtype:'grid',
                 store:store,
                 //"id", "table_schema", "table_name", "column_name", "is_nullable", "column_type",
                  //"column_key", "extra", "column_default", "column_comment"
                 columns:[{ xtype: 'rownumberer'},
                          { text: '字段名', dataIndex: 'column_name', width: 120 ,editor:{xtype:"textfield"}},
                          { text: '数据类型', dataIndex: 'column_type',  maxWidth: 120 ,editor:{xtype:"textfield"}},
                          { text: '空值', dataIndex: 'is_nullable',  minWidth: 80 ,editor:{xtype:"textfield"}},
                          { text: '主键/索引', dataIndex: 'column_key', maxWidth: 80, },                        
                          { text: '扩展', dataIndex: 'extra', maxWidth: 80 },
                          { text: '默认值', dataIndex: 'column_default' ,editor:{xtype:"textfield"}},
                          { text: '注释', dataIndex: 'column_comment',editor:{xtype:"textfield"}}],

                 plugins:[rowEditing],
                 listeners:{
                	 edit:function(editor, e){
                		 console.log(e.record.data);
                		var row=(e.record.data);
                		
                		if(e.record.data.id)
                			row.c="alert_edit";
                		else
                			row.c="alert_add";
                		
                		row.table=currentTable;
                		Lingx.post(handlerJsp,row,function(json){
                			lgxInfo(json.msg);
                		 	reloadGrid();
                   		 	e.record.commit();
                		});
                	 }
                 },
                 dockedItems: [{
 	    	        xtype: 'toolbar',
 	    	        items:[
 	    	             {iconCls:"Add",text:"添加",handler:function(){
 	    	            	  rowEditing.cancelEdit();
 	    	                var r = {column_name: ''};
							var len=store.getTotalCount();
							store.insert(len, r);
	    	                rowEditing.startEdit(len, 0);
 	    	                 //store.insert(0, r);
 	    	                 //rowEditing.startEdit(0, 0);
 	    	             }},
 	    	             {iconCls:"Delete",text:"删除",handler:function(){
 	    	            	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
 	    	            	if(array&&array.length>0){
 	    	   				var id=array[0].data.id;
 	    	   				if(!confirm("确认要删除吗？"))return;
 	    	   				 Lingx.post(handlerJsp,{c:'alert_remove',table:currentTable,id:id},function(json){
	    	        			lgxInfo(json.msg);
	    	        			reloadGrid();
	    	       		     });
 	    	   				}else{
 	    	   					lgxInfo("请选择要删除的行");
 	    	   				}
 	    	             }},
 	    	             //{iconCls:"Disk",text:"保存"},
 	    	             '->',
 	    	             {iconCls:"Accept",text:"执行SQL脚本",handler:function(){
 	    	            	 var sql=Ext.getCmp("sql").getValue();
 	    	            	 if(!sql){
 	    	            		 lgxInfo("SQL脚本不可为空");
 	    	            		 return;
 	    	            	 }
 	    	            	 Lingx.post(handlerJsp,{c:'finder',sql:sql},function(json){
 	    	        			lgxInfo(json.msg);
 	    	        			reloadTree();
 	    	       		     });
 	    	             }},
 	    	             {iconCls:"Script",text:"载入创建数据表脚本",handler:function(){
 	    	            	 Ext.getCmp("sql").setValue("CREATE TABLE `ttest` ( \n`id` int(11) NOT NULL AUTO_INCREMENT,\nPRIMARY KEY (`id`) 		) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
 	    	             }}],
 	    	        dock: 'top',
 	    	        displayInfo: true
 	    	        }],
        	 },{
            	 region: 'south',
            	 height:150,
                 split: true,
                 collapsible: true,
                 animCollapse: true,
                 layout:"fit",
            	 title:"SQL脚本",
            	 items:[{
            		 id:"sql",
            		 xtype:"textarea",
            		 border:false,
            		 enableKeyEvents:true,
            	 }]
             },]
         }]
	 });
	 viewport.show();
});
</script>
<body>
</body>
</html> 