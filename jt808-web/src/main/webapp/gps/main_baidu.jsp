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
	
	java.util.Map<String,Object> configMap=jdbc.queryForMap("select * from tgps_config");

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
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<style type="text/css">  

#container{height:100%}  
</style>  

<script type="text/javascript" src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=03O2DO2I3qFgYGUfFZnc6vuP6dIX2jXQ"></script>

<script type="text/javascript" src="//mapopen.cdn.bcebos.com/github/BMapGLLib/DistanceTool/src/DistanceTool.min.js"></script>
    <link href="//mapopen.cdn.bcebos.com/github/BMapGLLib/DrawingManager/src/DrawingManager.min.css" rel="stylesheet">
    <script type="text/javascript" src="//mapopen.cdn.bcebos.com/github/BMapGLLib/DrawingManager/src/DrawingManager.min.js"></script>
<script src="js/jsmap.js"></script>
<script src="gps/car_menu.js?123"></script>
   
<script src="gps/getCarWinInfo.jsp?ts=2019041216160"></script>
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
 .info {
        z-index: 999;
        width: auto;
        padding: .75rem 1.25rem;
        margin-left: 1.25rem;
        position: fixed;
        top: 4rem;
        right:8rem;
        background-color: #fff;
        border-radius: .25rem;
        font-size: 14px;
        color: #666;
        box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
    }
    .BMapLabel{
    width:auto !important;
    max-width:none !important;
    }
</style>
</head>
<body>
<div class = "info">
 <a id="mapBtn1" href="javascript:;" >测距</a> | 
 <a id="mapBtn3" href="javascript:;" >距形选车</a>
 </div>
<div id="container"></div> 
<script type="text/javascript">


var endTimeTids=",${EndTimeTids},";
var WindowInfoType="${WindowInfoType}";
var formPageId='${param.pageid}';
var ocBoolean=false;//报警声音开关
var getGpsUrl="<%=basePath %>gps/action/getGPS.jsp";
var getAddressUrl="<%=basePath %>gps/action/getAddress.jsp";
var handlerJsp="<%=basePath%>gps/handler.jsp";
var store,extParams={};
var markerArray=new Array();
var infoWinCache=new HashMap();

var latDef= <%=configMap.get("lat")%>;
var lngDef=<%=configMap.get("lng")%>;
var videoEnabled="true"=="<%=spring.getEnvironment().getProperty("jt1078.module.enabled")%>";

var map =  new BMapGL.Map('container');
map.centerAndZoom(new BMapGL.Point(lngDef,latDef ), 12);  // 初始化地图,设置中心点坐标和地图级别
map.enableScrollWheelZoom(true); 

var mapType1 = new BMapGL.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});

map.addControl(mapType1);          //2D图，卫星图
        //左上角，默认地图控件
/* var trafficLayer = new BMapGL.TileLayer.Traffic({
    zIndex: 10,map:map
}); */

var myDistanceToolObject = new BMapGLLib.DistanceTool(map);

var styleOptions = {
        strokeColor: '#5E87DB',   // 边线颜色
        fillColor: '#5E87DB',     // 填充颜色。当参数为空时，圆形没有填充颜色
        strokeWeight: 2,          // 边线宽度，以像素为单位
        strokeOpacity: 1,         // 边线透明度，取值范围0-1
        fillOpacity: 0.2          // 填充透明度，取值范围0-1
    };
    var labelOptions = {
        borderRadius: '2px',
        background: '#FFFBCC',
        border: '1px solid #E1E1E1',
        color: '#703A04',
        fontSize: '12px',
        letterSpacing: '0',
        padding: '5px'
    };

    // 实例化鼠标绘制工具
    var drawingManager = new BMapGLLib.DrawingManager(map, {
        // isOpen: true,        // 是否开启绘制模式
        enableCalculate: false, // 绘制是否进行测距测面
        enableSorption: true,   // 是否开启边界吸附功能
        sorptiondistance: 20,   // 边界吸附距离
        circleOptions: styleOptions,     // 圆的样式
        polylineOptions: styleOptions,   // 线的样式
        polygonOptions: styleOptions,    // 多边形的样式
        rectangleOptions: styleOptions,  // 矩形的样式
        labelOptions: labelOptions,      // label样式
    });  
    drawingManager.addEventListener('overlaycomplete', function(e) {
    	var overlay=e.overlay;
    	var points_poly=[];
    	var minlat,minlng,maxlat,maxlng;
    	//for(var i=0;i<overlay.getPath().length;i++){
    	//points_poly.push({"lng":overlay.getPath()[i].lng,"lat":overlay.getPath()[i].lat});
    	//}
    	maxlat=overlay.getPath()[0].lat;
    	minlng=overlay.getPath()[0].lng;
    	minlat=overlay.getPath()[2].lat;
    	maxlng=overlay.getPath()[2].lng;
    	map.removeOverlay(overlay);
    	//var param=(JSON.stringify(points_poly))
    	openWindow2("区域选车","gps/dialogs/area_cars.jsp?minlat="+minlat+"&minlng="+minlng+"&maxlat="+maxlat+"&maxlng="+maxlng);
    	
    });
$(function(){
	$("#mapBtn1").click(function(){
		lgxInfo("开启测距");
		myDistanceToolObject.open();
	});
		
	$("#mapBtn3").click(function(){
		lgxInfo("开启区域选车");
		 drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
         drawingManager.open();
	});
});
Ext.onReady(function(){
			Ext.create("Ext.Window",{
				id:"alarmWindow",
				title:"报警消息",
				width:200,
				height:100,
				closeAction:'hide',
				x:Ext.getBody().getWidth()-200,
				y:0,
				contentEl:"alarm",
				iconCls:'Flagred'});
			
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
    		title:"全部",
    		 split: true,
            collapsible: true,
    		width:210,
    		//closable: true,
            animate:false,
            autoScroll: true,
            rootVisible: false,
            store: store,
			xtype:"tree",
            iconCls:"Outline",
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
		                     if(temp.indexOf("_")!=-1)
								addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
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
    		border:false,
        	contentEl:"container"
    	}]
    	
       
    }).show();
    alarm();
    resetTitle();
	
});

function addCar(carid,carno){
	if(endTimeTids.indexOf(","+carid+",")>-1){
		alert("车辆【"+carno+"】服务已到期，请联系管理员进行处理！");
		return;
	}
	var exists=false;
	var index=-1;
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		if(carid==obj.getTitle()){
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
		Lingx.post(getGpsUrl,{carid:carid,carno:carno,mapType:"baidu"},function(json){
			if(!json.data){
				lgxInfo("该终端设备当前不在线!");
				return;
			}
			
			//var map=Ext.getCmp("gmap").getMap();
			var m=getMarker(json,map);
			markerArray.push(m);
			
		});
	}
}
function remove(id){
	var array=markerArray;
	var max=array.length;
	while(array.length>0&&max>0){
		var obj=array.shift();
		if(id==obj.getTitle()){
			map.removeOverlay(obj);
			delete obj;
			//obj.setMap(null);
			break;
		}else{
			array.push(obj);
		}
		max--;
	}

	var iw=infoWinCache.get(id);
	if(iw){
	//iw.setMap(null);
	map.removeOverlay(im);
	infoWinCache.remove(id);}
}

function replayMarker(options){
	console.log(options);
	options=options.data;
	var index=-1;
	var newArr=[];
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		//console.log(markerArray.length+":"+options.carid+" VS "+obj.getTitle());
		if(options.tid==obj.getTitle()){
			exists=true;
			index=i;
			//break;
		}else{
			newArr.push(obj);
		}
	}
	
	var marker=markerArray[index];
	//map.removeOverlay(marker);
	//marker.setMap(null);
	//delete marker;
	
	//marker=getMarker(options,map);
	//newArr.push(marker);
	//markerArray=newArr;
	var point = new BMapGL.Point(options.lng_baidu,options.lat_baidu );
	 marker.setPosition(point);
	 var icon=new BMapGL.Icon('images/car/car3.png?rand='+Math.random(),new BMapGL.Size(40, 40),{anchor:new BMapGL.Size(0, 0)}); //shadow
	marker.setIcon(icon);
	//if(options.car_img){
	//	var icon=new BMapGL.Icon({image:options.car_img+'?rand='+Math.random(),size:new BMapGL.Size(150, 70),imageOffset:new BMapGL.Pixel(0, 0)}); //shadow
	//    marker.setPosition(point);
	//	marker.setIcon(icon);
		
	//}else{
		//var icon=new BMapGL.Icon({image:'images/car/'+options.id+'_'+options.imageStatus+'.png?rand='+Math.random(),size:new BMapGL.Size(150, 70),imageOffset:new BMapGL.Pixel(0, 0)}); //shadow
       // marker.setPosition(point);
		//marker.setIcon(icon);
		
	//}
	
}

function getMarker2(options,map){
	options=options.data;
	var point = new BMapGL.Point(options.lng_baidu,options.lat_baidu );
	//map.setCenter(point); 
	//console.log('<%=basePath%>images/car/'+options.id+'_'+options.imageStatus+'.png?rand='+Math.random());
	var marker=null;
	
	//if(options.car_img){
	//	marker=new BMapGL.Marker({
	//		title:options.id,
	///		title:options.title,
	//        position:point,
	        //icon:new BMapGL.Icon(options.car_img+'?rand='+Math.random(),new BMapGL.Size(200, 71),new BMapGL.Pixel(0, 0),new BMapGL.Pixel(13, 55)), //shadow
	//        icon:new BMapGL.Icon({image:options.car_img+'?rand='+Math.random(),size:new BMapGL.Size(150, 70),imageOffset:new BMapGL.Pixel(0, 0)}), //shadow
	//        offset:new BMapGL.Pixel(-20, -60),
	//        map:map
	//    });
	//}else{
		marker=new BMapGL.Marker({
			title:options.id,
			title:options.title,
	        position:point,
	        icon:new BMapGL.Icon({image:'images/car/'+options.id+'_'+options.imageStatus+'.png?rand='+Math.random(),size:new BMapGL.Size(150, 70),imageOffset:new BMapGL.Pixel(0, 0)}), //shadow
	        offset:new BMapGL.Pixel(-20, -60),
	        map:map
	    });
	//}
	
	
	BMapGL.event.addEventListener(marker,"click",function(e){
		var _marker=this;
    	map.panTo(this.position);
    	if(infoWinCache.containsKey(this.id||this.getTitle()))return;//当窗口已打开
    	Lingx.post(handlerJsp,{c:"getCarInfo2",carid:this.id||this.getTitle()},function(json){
    		var text=getCarWinInfo(json,videoEnabled);
    		var infoWindow =new BMapGL.InfoWindow({visible: true,map:map,position: _marker.position,content: text,width:300,height:100,title:json.info.id}); 
    		infoWindow.open(map,_marker.getPosition());
    		infoWinCache.put(_marker.getTitle(),infoWindow);
    		BMapGL.event.addEventListener(infoWindow, "close", function(e){
    	    	var _infoWin=this;
    	    	//alert(_infoWin.id);
    	    	infoWinCache.remove(_infoWin.getTitle());
    	    }); 
    	});
 });
	return marker;
}

function getMarker(options,map){
	options=options.data;
	var point = new BMapGL.Point(options.lng_baidu,options.lat_baidu );
	map.setCenter(point); 
	//console.log('<%=basePath%>images/car/'+options.id+'_'+options.imageStatus+'.png?rand='+Math.random());
	var marker=null;
	
		marker=new BMapGL.Marker(point,{
			id:options.tid,
			title:options.tid,
	        icon:new BMapGL.Icon('images/car/car3.png?rand='+Math.random(),new BMapGL.Size(40, 40),{anchor:new BMapGL.Size(0, 0)}), //shadow
	        offset:new BMapGL.Size(-20, -20)
	    });
		map.addOverlay(marker);
	
		marker.addEventListener("click",function(e){
		var _marker=this;
		var carid=(this.getTitle());
    	if(infoWinCache.containsKey(carid))return;//当窗口已打开
    	Lingx.post(handlerJsp,{c:"getCarInfo2",carid:carid},function(json){
    		var text=getCarWinInfo(json,videoEnabled);
    		var infoWindow =new BMapGL.InfoWindow(text,{visible: true,map:map,position: _marker.getPosition(),width:'${WindowInfoWidth}'-0+20,height:'${WindowInfoHeight}',title1:json.info.id}); 
    		map.openInfoWindow(infoWindow,_marker.getPosition());
    		//infoWindow.open(map,_marker.getPosition());
    		//infoWinCache.put(_marker.getTitle(),infoWindow);
    		//infoWindow.addEventListener( "close", function(e){
    	    //	var _infoWin=this;
    	    	//alert(_infoWin.id);
    	    //	infoWinCache.remove(_infoWin.getTitle());
    	    //}); 
    	});
 });
	return marker;
}

function reload(){
	//lgxInfo("进行定时刷新...");
	//console.log("进行定时刷新...");
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		
		Lingx.post(getGpsUrl,{carid:obj.getTitle(),carno:obj.getTitle(),mapType:"baidu"},function(json){
			var m=replayMarker(json);
			if(infoWinCache.containsKey(json.id)){
				var myLatLng_1=new BMapGL.Point(json.lng,json.lat);
				infoWinCache.get(json.id).setPosition(myLatLng_1);
			}
		});
		/* if(infoWinCache.containsKey(obj.getTitle())){
			Lingx.post(handlerJsp,{c:"getCarInfo2",carid:obj.getTitle()},function(json){
				var text=getCarWinInfo(json,videoEnabled);
	    		infoWinCache.get(obj.getTitle()).setContent(text);
			});
		} */
	}
	resetTitle();
	
}
function alarm(){
}
function resetTitle(){}
function reloadSsjk(){
	
}
function removeAllAlarm(){
}
function getEndServiceList(){

}

function oc(p1){
}


//////////////2018-09-08
function updateNode(node,options){
node.eachChild(function(child) { 

//lgxInfo((child.data.id+"")+" VS "+(options.id+""));
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

//intervalProcess2=setInterval("reloadTreeNode()",3*1000);
</script>
<div style="display:none;">
<div id="text" style="font-size:14px;line-height:24px;">
<ul>
<li>车辆列表
<ol>
   <li>在分组树中，复选框打勾，该车辆在地图上显示，否则在地图上移除</li>
</ol>
</li>
<li>实时监控
<ol>
   <li>正常<img style="margin-bottom:-5px;" alt="" src="images/car/car1.png"> </li>
   <li>故障<img style="margin-bottom:-5px;" alt="" src="images/car/car2.png"> </li>
   <li>离线<img style="margin-bottom:-5px;" alt="" src="images/car/car4.png"> </li>
   <li>停车<img style="margin-bottom:-5px;" alt="" src="images/car/car5.png"> </li>
   <li>启动/停车时间，需要GPS终端正常接入，并行驶一次才会显示</li>
   <li>单击车标地图居中，双击车标地图放大</li>
</ol>
</li>
<li>车辆信息
<ol>
   <li>在分组树中，选中“所有分组”，车辆信息上显示操作说明</li>
   <li>在分组树中，选中“车牌号码”，车辆信息上显示该车的相关信息</li>
</ol>
</li>
</ul>
 </div>


</div>
<div id="command">

</div>

<div id="alarm" style="padding:2px;font-size:12px;height:68px;overflow:auto;">

</div>
<div id="alarmAudio" style="display:none;">

</div>
</body>
</html> 