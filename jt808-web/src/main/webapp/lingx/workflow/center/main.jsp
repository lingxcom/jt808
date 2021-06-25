<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程主页</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="js/jquery.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
var processInstanceId="${param.processInstanceId }";

Ext.onReady(function(){
	 var store = Ext.create('Ext.data.Store', {
         fields: ["name", "user_id", "status", "stime", "etime"],
         pageSize: 20,  //页容量5条数据
         //是否在服务端排序 （true的话，在客户端就不能排序）
         remoteSort: false,
         remoteFilter: true,
         proxy: {
             type: 'ajax',
             url: 'e?e=tlingx_wf_instance_task&m=grid&t=3&rule=_none_&instance_id=${param.processInstanceId }&_refEntity_=tlingx_wf_instance',
             reader: {   //这里的reader为数据存储组织的地方，下面的配置是为json格式的数据，例如：[{"total":50,"rows":[{"a":"3","b":"4"}]}]
                 type: 'json', //返回数据类型为json格式
                 root: 'rows',  //数据
                 totalProperty: 'total' //数据总条数
             }
         },
         sorters: [{
             //排序字段。
             property: 'stime',
             //排序类型，默认为 ASC 
             direction: 'asc'
         }],
         autoLoad: true  //即时加载数据
     });

	 
    var viewport = Ext.create('Ext.Viewport', {
    	layout:'border',
        border: false,
        items:[{
        	region:"center",
        	autoScroll: true,
            border: false,
        	html: '<iframe id="INDEX_FRAME" scrolling="auto" frameborder="0" width="100%" height="100%" src="lingx/workflow/center/info.jsp?processInstanceId=${param.processInstanceId }"> </iframe>',
        	
        },{
        	region:"south",
        	 split: true,
           border: false,
			title:"处理记录",
           collapsible: true,
           animCollapse: true,
        	height:"50%",
        	xtype:"grid",
        	store:store,
        	bbar: Ext.create('Ext.PagingToolbar', {
	            store: store
	        }),
        	columns: [       { xtype: 'rownumberer',width:26},             
                      { text: '任务名称', dataIndex: 'name', align: 'left', maxWidth: 200 },
                      { text: '处理人', dataIndex: 'user_id', width:200, renderer:function(value,record){
                    	  var sb="";
                    	  for(var i=0;i<value.length;i++){
                    		  sb=sb+value[i].text+",";
                    	  }
                    	  if(sb.length>0)sb=sb.substring(0, sb.length-1);
                    	  return sb;
                      }},
                      { text: '当前状态', dataIndex: 'status', align: 'left', minWidth: 80,renderer:function(value,record){
                    	  return value[0].text;
                      } },
                      { text: '接受时间', dataIndex: 'stime', maxWidth: 180, align: 'left',renderer:function(value,record){
                    	  var temp=value;if(!temp)return"";
                    	  return temp.substring(0,10);
                      } },                        
                      { text: '完成时间', dataIndex: 'etime', maxWidth: 180 ,renderer:function(value,record){
                    	  var temp=value;if(!temp)return"";
                    	  return temp.substring(0,4)+"-"+temp.substring(4,6)+"-"+temp.substring(6,8);
                      }}
                   ]
        }]
		
    });
    viewport.show();
});
</script>
</head>
<body>
<div id="IMG">

</div>
</body>

</html>