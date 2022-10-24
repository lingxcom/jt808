<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
	org.springframework.jdbc.core.JdbcTemplate jdbc=spring.getBean(org.springframework.jdbc.core.JdbcTemplate.class);
	com.lingx.core.model.bean.UserBean userBean=(com.lingx.core.model.bean.UserBean)session.getAttribute(com.lingx.core.Constants.SESSION_USER);
	

	com.lingx.core.service.ILingxService lingx=spring.getBean(com.lingx.core.service.ILingxService.class);
	String temp8=lingx.getConfigValue("lingx.gps.map.windowinfo", "1,400,240");
	String array8[]=temp8.split(",");
	request.setAttribute("WindowInfoType",array8[0] );
	request.setAttribute("WindowInfoWidth",array8[1] );
	request.setAttribute("WindowInfoHeight",array8[2] );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>系统首页</title>
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<%@ include file="/lingx/include/include_JavaScriptAndCss4.jsp"%> 
<script src="//cdn.bootcss.com/flv.js/1.5.0/flv.min.js" type="text/javascript"></script>
<style type="text/css">  

#container{height:100%}  
</style>  

<script src="js/jsmap.js"></script>
<script src="gps/car_menu.js?123"></script>
   
<link rel="stylesheet" type="text/css" href="gps/car_icons.css?12">
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
    	this.player.pause();
    	this.player.unload();
    	this.player.detachMediaElement();
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
 function playVideo()
 {	var tdh= $('#tdh').val()
	 lgxInfo("正在下发播放指令，请稍等...");
	 $.post("gps/handler.jsp",{c:"send9101",tid:tid,tdh:tdh},function(json){
		 window.videoPlayer = new FLVPlayer({
	         container : $('#video'),
	         url : json.pull_address+"?jt1078=1&deviceId="+tid+"&channel="+tdh,
	         autoFastForward : false
	     });
	     window.videoPlayer.play();
		 
	 },"json");
     
 }
 function stopVideo(){
	 var player=window.videoPlayer;
	 
     player.destroy();
     $('#video').empty();
     player = null;
     var tdh= $('#tdh').val()
     $.post("gps/handler.jsp",{c:"send9102",tid:tid,tdh:tdh},function(json){},"json");
 }
</script>
</head>
<body>

<div id="container" style="padding:5px 10px;">
<div id="video" style="background-color: #333333;border-radius: 10px; overflow: hidden; width: 400px; height: 300px;"></div>
<br>
通道号：<input id="tdh" value="1">
<button onclick="playVideo()">播放视频</button>
<button onclick="stopVideo()">停止播放</button>
</div> 
<script type="text/javascript">

var tid="";
var formPageId='${param.pageid}';
var ocBoolean=false;//报警声音开关
var getGpsUrl="<%=basePath %>gps/action/getGPS.jsp";
var handlerJsp="<%=basePath%>gps/handler.jsp";
var store,extParams={};
var markerArray=new Array();
var infoWinCache=new HashMap();

var latDef= 39.916385;
var lngDef=116.396621;
var videoEnabled="false";
Ext.onReady(function(){
			
    var intervalProcess=setInterval("reload()",10*1000);	
		
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
    Ext.create('Ext.Viewport', {
    	id:'viewport',
    	layout: 'border',
		border:false,
		style:"padding:2px",
    	items:[{
    		id:'tree',
    		region:"west",
    		 split: true,
    		width:210,
    		//closable: true,
            animate:false,
            autoScroll: true,
            rootVisible: false,
            store: store,
			xtype:"tree",
			dockedItems: [{
    	        xtype: 'toolbar',
    	        items:[{
    	        	id:"SearchText",
    	            xtype    : 'textfield',
    	            name     : 'field1',
    	            width:110,
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
		                     if(temp.indexOf("_")!=-1)
								addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
		                     //tree.fireEvent("checkchange",node,true);
		                 });
		                 record.expand();
		                 record.expandChildren();
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
		             }
				},

				itemclick:function(view,record,item,index,event,obj){
					var node=record;
					var temp=node.data.id;
					tid=temp.substring(temp.indexOf("_")+1);
					//addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
				},
				itemdblclick:function(view,record,item,index,event,obj){
					var node=record;
					var temp=node.data.id;
					tid=temp.substring(temp.indexOf("_")+1);
					//addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
				},
				
	        	itemcontextmenu: function(view, record, item, index, e) { 
	        		if(record.data.id.indexOf("_")>0){
	        			e.preventDefault();  
  		                e.stopEvent();  
  		              var car_menu= createCarMenu(record);
  		              car_menu.showAt(e.getXY());
	        		}
		                
		            }
    	
			}
    	},
    	
    	{
    		region:"center",

    		id:'gmap',
    		//border:false,
			contentEl:"container"
        	
    	}]
    	
       
    }).show();
});

function addCar(carid,carno){
	
	var exists=false;
	var index=-1;
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		if(carid==obj.getExtData()){
			exists=true;
			index=i;
			break;
		}
	}
	if(exists){
		//var map=Ext.getCmp("gmap").getMap();
		map.setCenter(markerArray[index].getPosition());
	}else{
		//lgxInfo("正在地图上添加车辆["+carno+"]...");
		$.post(getGpsUrl,{carid:carid,carno:carno},function(json){
			if(!json.lat){
				lgxInfo("该车辆终端未上传数据");
				return;
			}
			
			var m=getMarker(json,map);
			markerArray.push(m);
		},"json");
	}
}
function remove(id){
	var array=markerArray;
	var max=array.length;
	while(array.length>0&&max>0){
		var obj=array.shift();
		if(id==obj.getExtData()){
			obj.setMap(null);
			break;
		}else{
			array.push(obj);
		}
		max--;
	}

	var iw=infoWinCache.get(id);
	if(iw){
	iw.setMap(null);
	infoWinCache.remove(id);}
}

function replayMarker(options){
	var index=-1;
	var newArr=[];
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		if(options.tid==obj.getExtData()){
			exists=true;
			index=i;
			//break;
		}else{
			newArr.push(obj);
		}
	}
	
	var marker=markerArray[index];
	if(marker){
	marker.setMap(null);
	delete marker;}
	
	marker=getMarker2(options,map);
	markerArray[index]=marker;
	
	
	
}

function getMarker2(options,map){
	var point = new AMap.LngLat(options.lng_gaode,options.lat_gaode );
	var marker=null;
	
		marker=new AMap.Marker({
			extData:options.id||options.tid,
			title:options.title,
	        position:point,
	        icon:new AMap.Icon({image:'images/car/car3.png',size:new AMap.Size(40, 40)}), //shadow
	        map:map
	    });
	
	
	return marker;
}

function getMarker(options,map){
	var point = new AMap.LngLat(options.lng_gaode,options.lat_gaode );
	map.setCenter(point); 
	var marker=null;
	
		marker=new AMap.Marker({
			extData:options.id||options.tid,
			title:options.title,
	        position:point,
	        icon:new AMap.Icon({image:'images/car/car3.png',size:new AMap.Size(40, 40)}), //shadow
	        map:map
	    });
	
	return marker;
}

function reload(){
	//console.log("进行定时刷新...");
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		if(obj&&obj.getExtData())
		$.post(getGpsUrl,{carid:obj.getExtData(),carno:obj.getTitle()},function(json){
			var m=replayMarker(json);
			
		},"json");
		
	}
	
	
}
function updateNode(node,options){
node.eachChild(function(child) { 
if(child&&child.data&&child.data.id&&(child.data.id+"").indexOf((options.id+""))>=0){
if((child.data.id+"").indexOf("_")>0&&(options.id+"").length<6)return;

for(var key in options){
	if(key!='id'){
		child.set(key,options[key]);
	}
}
}
updateNode(child,options);
})

}

var nodeids="";
function getNodeIds(){
nodeids="";
var rootNode=Ext.getCmp("tree").getRootNode();
eachNodeIds(rootNode);
return nodeids;
}
function eachNodeIds(node){
node.eachChild(function(child) { 
nodeids=nodeids+","+child.data.id;
eachNodeIds(child);
})
}
function reloadTreeNode(){
var ids=getNodeIds();
$.post(handlerJsp,{c:"reloadTreeNode",ids:ids},function(json){
var list=json.list;
var rootNode=Ext.getCmp("tree").getRootNode();
for(var i=0;i<list.length;i++){
updateNode(rootNode,list[i]);
}
},"json");
}

</script>

</body>
</html> 