<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
	org.springframework.jdbc.core.JdbcTemplate jdbc=spring.getBean(org.springframework.jdbc.core.JdbcTemplate.class);

	com.lingx.core.model.bean.UserBean userBean=(com.lingx.core.model.bean.UserBean)session.getAttribute(com.lingx.core.Constants.SESSION_USER);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>系统首页</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss4.jsp"%> 
<script src="//cdn.bootcss.com/flv.js/1.5.0/flv.min.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="gps/car_icons.css?1">
<style type="text/css">
.carStatus_1{
	background:url('gps/icons/circle_red_16.png') no-repeat !important;
}
.carStatus_2{
	background:url('gps/icons/circle_blue_16.png') no-repeat !important;
}
.carStatus_3{
	background:url('gps/icons/circle_green_16.png') no-repeat !important;
}
.carStatus_4{
	background:url('gps/icons/circle_black_16.png') no-repeat !important;
}
.carStatus_5{
	background:url('gps/icons/circle_zi_16.png') no-repeat !important;
}
</style>

<script type="text/javascript">

function FLVPlayer(opts)
{
    var videoElement = document.createElement('VIDEO');
    videoElement.autoplay = true;
    videoElement.controls = false;
    videoElement.muted = false;
    videoElement.style.width = '100%';
    videoElement.style.height = '100%';
    opts.container.append(videoElement);

    this.container = opts.container;
    this.videoElement = videoElement;
    this.httpFlvURL = opts.url;

    this.mediaInfo = null;
    this.play = null;
    this.onPlayEvtListener = null;
    this.onPauseEvtListener = null;
    this.onStopEvtListener = null;

    this.autoFastForward = opts.autoFastForward;
    this.autoFastForwardInterval = null;

    this.play = function()
    {
        if (this.player) return;

        var self = this;
        self.player = new flvjs.createPlayer({
            type                            : 'flv',
            url                             : self.httpFlvURL,
            isLive                          : true,
            enableWorker                    : true,
            enableStashBuffer               : true,
            autoCleanupSourceBuffer         : true,
            autoCleanupMaxBackwardDuration  : 5,
            autoCleanupMinBackwardDuration  : 1
        });

        self.player.on('media_info', function()
        {
            self.mediaInfo = self.player.mediaInfo;
        });

        self.player.on('statistics_info', function()
        {
            console.log(arguments);
        });

        var autoPlayTimer = null;
        self.videoElement.addEventListener('player', function(e)
        {
            if (autoPlayTimer) clearInterval(autoPlayTimer);
            if (self.onPlayEvtListener) self.onPlayEvtListener(self, e);
        });
        self.videoElement.addEventListener('dblclick', function()
        {
            if (self.videoElement.requestFullscreen) self.videoElement.requestFullscreen();
        });
        autoPlayTimer = setInterval(function()
        {
            try { self.player.play(); } catch(e) { clearInterval(autoPlayTimer); };
        });

        self.player.attachMediaElement(self.videoElement);
        self.player.load();
        self.player.play();

        if (this.autoFastForward) this.autoFastForwardInterval = setInterval(function()
        {
            if (self.videoElement.buffered.length > 0 && self.videoElement.buffered.end(0) - self.videoElement.currentTime > 2)
            {
                console.log(self.videoElement.buffered.end(0) + "-" + self.videoElement.currentTime);
                self.videoElement.currentTime = self.videoElement.buffered.end(0) - 1;
            }
        }, 1000);
    };

    this.fullscreen = function()
    {
        if (this.videoElement && this.videoElement.requestFullscreen)
            this.videoElement.requestFullscreen();
    };

    this.onPlay = function(fn)
    {
        this.onPlayEvtListener = fn;
    };

    this.destroy = function()
    {
        this.player.destroy();
        clearInterval(this.autoFastForwardInterval);
    }
}
 /*
 if (location.hash)
 {
     var hash = location.hash.substring(1);
     $('#tag').val(hash);
 }*/
 var tdh;
 function playVideo()
 {	//var tdh= $('#tdh').val()
	 lgxInfo("正在下发播放指令，请稍等...");
     window.videoPlayer = new FLVPlayer({
         container : $('#video'),
         url : 'http://127.0.0.1:3333/video/'+tid+"-" + tdh,
         autoFastForward : false
     });
     window.videoPlayer.play();
 }
 function stopVideo(){
	 var player=window.videoPlayer;
	 
     player.destroy();
     $('#video').empty();
     player = null;
     
     $.post("gps/handler.jsp",{c:"send9202",tid:tid,tdh:tdh},function(json){},"json");
 }
</script>
<script type="text/javascript">

var mediaAddress = "";
var fromPageId='${param.pageid}';
var date24=new Date();
if(date24.getDate()>1){
	date24.setDate(date24.getDate()-1);
}
var kjbs=1,ktbs=1,tdh=1;
var STIME,TIME;
var handlerJsp="gps/handler.jsp";
var tid,store,extParams={};

Ext.onReady(function(){
	 Ext.define('Option',{
	        extend: 'Ext.data.Model',
	        fields: [
	            {name: 'text', type: 'string'},
	            {name: 'value', type: 'string'}
	        ]
	    });
	 

		store=Ext.create('Ext.data.TreeStore', {
	    	id:"treeStore",
			proxy: {
				actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	            },
		         type: 'ajax',
		         url: handlerJsp+'?c=getTreeList&checkbox=false',
		         reader: {
		             type: 'json'
		         },
		     },
		     root: {
		         text:"所有 车队",
		         id: '0',
		         expanded: true
		    },
		     autoLoad: true,
		     listeners:{
	         		beforeload:function(store){
	         			
	         			if(store)
			    		Ext.apply(store.proxy.extraParams,extParams);  
			    	},
		    	 	load:function(){
			    		Ext.getCmp("tree").getSelectionModel().select(0);
			    	}
		     }
		});

	Ext.define('Pangram', {
	    extend: 'Ext.data.Model',
	    fields:[ { name: 'id', type: 'string' },
	             { name: 'timeView', type: 'string' },
	             { name: 'stime', type: 'string' },
	             { name: 'etime', type: 'string' },
	             { name: 'length', type: 'int' },
	             { name: 'type', type: 'int' },
	             { name: 'mltype', type: 'int' },
	             { name: 'zcqtype', type: 'int' },
	             { name: 'tdh', type: 'int' },
	             { name: 'time', type: 'int' },
	             {name:"mileage",type:'int'}
	             ],
	    idProperty: "id"
	});
	
	var mediaStore=Ext.create('Ext.data.Store', {
	     model: 'Pangram',
	     
	     proxy: {
	    	 actionMethods: {
             create : 'POST',
             read   : 'POST', // by default GET
             update : 'POST',
             destroy: 'POST'
         },
	         type: 'ajax',
	         url: 'gps/handler.jsp?c=getMediaList',
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
	    		// Ext.getCmp("entityGrid_1").getSelectionModel().select(0);
	    	 },beforeload:function(){
	    		Ext.apply(mediaStore.proxy.extraParams,extParams);  
	    	}
	     }
	 });
    Ext.create('Ext.Viewport', {
    	id:'viewport',
    	layout: 'border',
		border:false,
		style:"padding:2px",
    	items:[
    	       {
    		region:"west",
    		 split: true,
           // collapsible: true,
    		width:240,
    		//closable: true,
            animate:false,
           
    		border:true,
           // autoScroll: true,
            autoScroll: true,
            rootVisible: false,
            store: store,
            id:"tree",
			xtype:"tree",
			dockedItems: [{
    	        xtype: 'toolbar',
    	        items:[{
    	        	id:"SearchText",
    	            xtype    : 'textfield',
    	            name     : 'field1',
    	            width:140,
    	            emptyText: '设备ID/名称/IMEI',
    	            listeners:{
    	            	specialkey: function(field, e){
	                		if(e.getKey()== e.ENTER){
	                			var text=Ext.getCmp("SearchText").getValue();
	    	    	        	extParams["text"]=(text);
	    	    	        	if(text){
	        	    	        	store.load(); 
	    	    	        	}else{
	    	    	        		store.getRootNode().remove();
	    	    	    			store.setRootNode({
	    	    	    				 text: "ROOT",
	    	    	    		         id:"0",
	    	    	    		         expanded: true
	    	    	    			});
	    	    	        	}
	                		}
	                	},
    	            	change:function(){ 
    	            		/*
    	            		var text=Ext.getCmp("SearchText").getValue();
    	    	        	extParams["text"]=(text);
    	    	        	if(text){
        	    	        	store.load(); 
    	    	        	}else{
    	    	        		store.getRootNode().remove();
    	    	    			store.setRootNode({
    	    	    				 text: "ROOT",
    	    	    		         id:"0",
    	    	    		         expanded: true
    	    	    			});
    	    	        	}
    	    	        	*/
    	            	}
    	            }
    	        },{text:"查询",handler:function(){
    	        	var text=Ext.getCmp("SearchText").getValue();
    	        	extParams["text"]=(text);
    	        	if(text){
	    	        	store.load(); 
    	        	}else{
    	        		store.getRootNode().remove();
    	    			store.setRootNode({
    	    				 text: "ROOT",
    	    		         id:"0",
    	    		         expanded: true
    	    			});
    	        	}
    	        	
    	        }},{
    	        	text:"刷新",
    	        	handler:function(){
    	        		window.location.reload();
    	        	}
    	        }],
    	        dock: 'top',
    	        displayInfo: true
    	        }],
			listeners:{
				
				checkchange:function(record,checked){
					 if (checked) {

		                 record.bubble(function(parentNode) {
		                     parentNode.set('checked', true);
		                 });
		                 record.cascadeBy(function(node) {
		                     node.set('checked', true);
		                     var temp=node.data.id;
		                     temp=temp+"";
		                     if(temp.indexOf("_")!=-1){
		                    	 tid=temp.substring(temp.indexOf("_")+1);
		                    	 /* addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
		                    	 Lingx.post(handlerJsp,{c:"getCarInfo2",carid:tid},function(json){
		             				var text=getText(json);
		             			}); */
		                     }
								
		                     //tree.fireEvent("checkchange",node,true);
		                 });
		                 record.expand();
		                 record.expandChildren();
		                 //reloadSsjk();
		             } else {
		                 //record.collapse();
		                // record.collapseChildren();
		                 record.cascadeBy(function(node) {
		                     node.set('checked', false);
		                     var temp=node.data.id;
		                     temp=temp+"";
		                     if(temp.indexOf("_")!=-1)
								remove(temp.substring(temp.indexOf("_")+1));
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
		                     }
		                 });
		                 reloadSsjk();
		             }
				},
				
				itemclick:function(view,record,item,index,event,obj){//dbl
					var node=record;
					var temp=node.data.id;
					tid=temp.substring(temp.indexOf("_")+1);
					/* addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
					Lingx.post(handlerJsp,{c:"getCarInfo2",carid:tid},function(json){
        				var text=getText(json);
        			}); */
				}
    	
			}
    	},{
    		region:"east",
	    	   width:340,
    		   layout:"border",
	     		 split: true,
	    	   items:[{
   	    		region:"north",
        		//height:"380",
		    	   items:[{
		    		   xtype: 'form',
		    		border:false,
		               defaultType: 'textfield',
						 bodyPadding: 5,
		               defaults: {
		            	   labelAlign: 'right',
				            labelWidth:60
		                   //width: 150
		               },items:[{
		            	   fieldLabel: '<%=i18n.text("起始时间",session)%>',
	                    	id: 'stime',
		                    name: 'stime',
		        	        xtype:'datetimefield',
	        	        	format:'Y-m-d H:i:s',
		        	        value:date24,
		        	        width: 240
		               
		               },{
		            	   fieldLabel: '<%=i18n.text("截止时间",session)%>',
	                   	id: 'etime',
	                    	name: 'etime',
		        	        xtype:'datetimefield',
	        	        	format:'Y-m-d H:i:s',
	        	        	value:new Date(),
		        	        width: 240
	               
	               },{
	            	   id:"tdh",
	            	   name:"tdh",
	            	   xtype:"combobox",
	            	   fieldLabel: '<%=i18n.text("通道号",session)%>',
	            	   displayField: 'text',
	               		valueField: 'value',
	               		store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=TDH',reader:{type:'json'}},autoLoad:true}),
	            	   value:"1",
	            	   width: 240
	               },{
	            	   id:"type",
	            	   name:"type",
	            	   xtype:"combobox",
	            	   fieldLabel: '<%=i18n.text("媒体类型",session)%>',
	            	   displayField: 'text',
	               		valueField: 'value',
	               		store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=MTLX',reader:{type:'json'}},autoLoad:true}),
		            	   value:"2",
	            	   width: 240
	               },{
	            	   id:"ml",
	            	   name:"ml",
	            	   xtype:"combobox",
	            	   fieldLabel: '<%=i18n.text("码流类型",session)%>',
	            	   displayField: 'text',
	               		valueField: 'value',
	               		store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=MLLX',reader:{type:'json'}},autoLoad:true}),
		            	    value:"0",
	            	   width: 240
	               },{
	            	   id:"zcq",
	            	   name:"zcq",
	            	   xtype:"combobox",
	            	   fieldLabel: '<%=i18n.text("存储器",session)%>',
	            	   displayField: 'text',
	               		valueField: 'value',
	               		store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=ZCQLX',reader:{type:'json'}},autoLoad:true}),
		            	   value:"1",
	            	   width: 240
	               },{
	     	   text: '<%=i18n.text("查询设备记录",session)%>',
	     	   width:160,
	     	   id:"btn1",
	     	   style:"margin:2px;",
	 	        xtype:'button',
	 	        handler:function(){
	 	        	if(!tid){
	 	        		lgxInfo("操作失败，需要先选择左边车辆");return;
	 	        	}
	 	        	extParams.tid=tid;
	 	        	var stime=Lingx.toTime14(Ext.getCmp("stime").getValue());
	 	        	var etime=Lingx.toTime14(Ext.getCmp("etime").getValue());
	 	        	var tdh=Ext.getCmp("tdh").getValue();
	 	        	var type=Ext.getCmp("type").getValue();
	 	        	var ml=Ext.getCmp("ml").getValue();
	 	        	var zcq=Ext.getCmp("zcq").getValue();
	 	        	lgxInfo("正在下发指令,请稍等...");
	 	        	$.post("gps/handler.jsp",{c:"send9205",tid:tid,stime:stime,etime:etime,tdh:tdh,type:type,ml:ml,zcq:zcq},function(json){
	 	        		//alert(json.message);
	 	        		setTimeout(function() {
	 	        			mediaStore.loadPage(1);
	 	        			lgxInfo("已刷新");
    					}, 1000);
	 	        		
	 	        		//lgxInfo("处理完成");
	 	        	},"json");
	 	        }
	        },{id:"tzBtn",
	        	text:"停止播放",
		     	   style:"margin:2px;",
		 	        xtype:'button',
		 	        disabled:true,
	        	handler:function(){
	        		stopVideo()
	        	}
	        }
	        
	               ]
		    	   }]
		       },{
	        		region:"center",
	          		 split: true,
					id:'grid',
					 store:mediaStore,
					xtype:'grid',

		    	        listeners:{
		    	        	itemcontextmenu: function(view, record, item, index, e) { 
		    	        		e.preventDefault();  
		  		                e.stopEvent();  
		  		                var menu=Ext.create("Ext.menu.Menu",{
		     						 floating : true,  
		     						 items:[{
		     							 text:"播放",
		     							 handler:function(){
		     			   	        		 tid=tid.substring(tid.length-12);
		     			   	        		 tdh=record.data.tdh;
		     			   	        		playVideo();
		    		    	            	 $.post("gps/handler.jsp",{c:"send9201",tid:tid,tdh:record.data.tdh,type:record.data.type,mltype:record.data.mltype,zcqtype:record.data.zcqtype,stime:record.data.stime,etime:record.data.etime},function(json){
		    		    	            		 enableBtn();
		    		    	            	 },"json");
		    		    	        	
		     							 }
		     						 }]});
		  		              menu.showAt(e.getXY());
		    	        	},
		    	        	itemdblclick:function(view,record,item,index,event,obj){

		    	        		if(!confirm("确定播放该记录吗?"))return;
		    	        		tid=tid.substring(tid.length-12);
			   	        		 tdh=record.data.tdh;
			   	        		playVideo();
		    	            	 $.post("gps/handler.jsp",{c:"send9201",tid:tid,tdh:record.data.tdh,type:record.data.type,mltype:record.data.mltype,zcqtype:record.data.zcqtype,stime:record.data.stime,etime:record.data.etime},function(json){
		    	            		 enableBtn();
		    	            	 },"json");
		    	        	}
		    	        },
					  columns:[
							{ xtype: 'rownumberer',width:24},
				            { text: '<%=i18n.text("时间",session)%>', dataIndex: 'timeView',width:200 ,renderer:function(value,p,record){
				            	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8)+" "+value.substring(8,10)+":"+value.substring(10,12)+":"+value.substring(12,14)+" - "+value.substring(8+6,10+6)+":"+value.substring(10+6,12+6)+":"+value.substring(12+6,14+6));
				            }},
				            { text: '<%=i18n.text("大小MB",session)%>', dataIndex: 'length' ,width:60,renderer:function(value,p,record){
				            	return parseInt(value/1024/1024);
				            }},
				            
				            { text: '<%=i18n.text("通道号",session)%>', dataIndex: 'tdh' ,width:60},
				            
				            { text: '<%=i18n.text("媒体类型",session)%>', dataIndex: 'type' ,width:60,renderer:function(value,p,record){
				            	var temp="";
				            	switch(value-0){
				            	case 0:temp="音视频";
				            		break;
				            	case 1:temp="音频";
				            		break;
				            	case 2:temp="视频";
				            		break;
				            	}
				            	return temp;
				            }},
				            { text: '<%=i18n.text("码流类型",session)%>', dataIndex: 'mltype' ,width:60,renderer:function(value,p,record){
				            	var temp="";
				            	switch(value-0){
				            	case 1:temp="主码流";
				            		break;
				            	case 2:temp="子码流";
				            		break;
				            	}
				            	return temp;
				            }},
				            { text: '<%=i18n.text("存储器",session)%>', dataIndex: 'zcqtype' ,width:100,renderer:function(value,p,record){
				            	var temp="";
				            	switch(value-0){
				            	case 1:temp="主存储器";
				            		break;
				            	case 2:temp="灾备存储器";
				            		break;
				            	}
				            	return temp;
				            }}
				            
					  ]
	        	}]
	       },
    	{
    		region:"center",
        	contentEl:"main"
    	}/* ,{
    		region:"south",
    		height:100,
      		 split: true,
      		 style:{overflow:"auto"},
    		contentEl:"grid_table"
    	} */]
    	
       
    }).show();

});

function enableBtn(){
	Ext.getCmp("tzBtn").setDisabled(false);
	//Ext.getCmp("zcBtn").setDisabled(false);
	//Ext.getCmp("kjBtn").setDisabled(false);
	//Ext.getCmp("ktBtn").setDisabled(false);
}
</script>

</head>
<body>
<div style="display:none;">
<div id="main" style="height:100%;text-align:center;background-color:#e8e8e8;">
<center>
<div id="video" style="background-color: #333333;border-radius: 10px; overflow: hidden; width: 400px; height: 300px;"></div>
<br>
</center>
</div>
</div>
</body>
</html> 