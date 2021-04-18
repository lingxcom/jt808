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
<style type="text/css">  

#container{height:100%}  
</style>  
<script type="text/javascript" src="//webapi.amap.com/maps?v=1.4.4&key=e607415290b3792980c29664c6bd5b5b&plugin=AMap.MapType"></script> 

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
</head>
<body>

<div id="container"></div> 
<script type="text/javascript">


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
var map = new AMap.Map('container',{
    resizeEnable: true,
    zoom: 14,
    center: [lngDef, latDef]
});
map.addControl(new AMap.MapType({
    defaultType:0 //0代表默认，1代表卫星
}));
/* var trafficLayer = new AMap.TileLayer.Traffic({
    zIndex: 10,map:map
}); */
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
	         url: handlerJsp+'?c=getTreeList&checkbox=true',
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
				
				itemdblclick:function(view,record,item,index,event,obj){
					var node=record;
					var temp=node.data.id;
					addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
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