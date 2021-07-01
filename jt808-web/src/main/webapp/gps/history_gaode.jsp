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
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="//webapi.amap.com/maps?v=1.3&key=e607415290b3792980c29664c6bd5b5b"></script>
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>

<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="gps/car_icons.css?1">
<style type="text/css">  

#container{height:100%}  
</style>  
</head>
<body>

<div id="container"></div> 
<script type="text/javascript">


var flightPath2=null;
var fromPageId='${param.pageid}';
var map,poly,car,index=0,latlngList,tempPathArray=[];
var jj=1;//看是加1还是减1，前进和后退
var intervalProcess=0;
var getGpsUrl="<%=basePath %>gps/action/getGPS.jsp";
var getAddressUrl="<%=basePath %>gps/action/getAddress.jsp";
var handlerJsp="<%=basePath%>gps/history/handler.jsp";
var carid=null;
var store,extParams={};

var api="";
var token="${SESSION_USER.token}";
var markerArray=new Array();
var date24=new Date();
if(date24.getDate()>1){
	date24.setDate(date24.getDate()-1);
}

var timeCountTj2=0,timeTempTj2=0,mileageStartTj2=0;

var latDef= 39.916385;
var lngDef=116.396621;

var map = new AMap.Map('container',{
    resizeEnable: true,
    zoom: 13,
    center: [lngDef, latDef]
});
// 加载地图控件
AMapUI.loadUI(['control/BasicControl'], function (BasicControl) {

    //添加一个缩放控件
//    map.addControl(new BasicControl.Zoom({
//        position: {
//            bottom:'20px',
//            left:'10px'
//        }
//    }));

    //图层切换控件
    map.addControl(new BasicControl.LayerSwitcher({
        position:{
            top:'10px',
            right:'10px'
        }
    }));

});

poly=new AMap.Polyline({
	map:map,
	 strokeColor: "#F00",  //线颜色
     // strokeOpacity: 1,     //线透明度
     strokeWeight: 5   
      });

Ext.onReady(function(){
	Ext.define('Pangram', {
	    extend: 'Ext.data.Model',
	    fields:[ { name: 'id', type: 'string' },
	             { name: 'carno', type: 'string' },
	             { name: 'speed', type: 'int' },
	             { name: 'online', type: 'int' },
	             { name: 'jg', type: 'int' },
	             { name: 'acc', type: 'int' },
	             { name: 'lng', type: 'string' },
	             { name: 'lat', type: 'string' },//direction
	             { name: 'gpslng', type: 'string' },
	             { name: 'gpslat', type: 'string' },//direction
	             { name: 'direction', type: 'string' },
	             { name: 'gpstime', type: 'string' },
	             { name: 'tid', type: 'string' },
	             { name: 'cid', type: 'string' },
	             { name: 'status', type: 'string' },
	             { name: 'tckcsj', type: 'string' },
	             { name: 'address', type: 'string' },
	             { name: 'card', type: 'string' },
	             {name:"mileage",type:'int'}
	             ],
	    idProperty: "id"
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
		         url: 'gps/handler.jsp?c=getTreeList&checkbox=false',
		         reader: {
		             type: 'json'
		         },
		     },
		     root: {
		         text:"<%=i18n.text("所有分组",session)%>",
		         id: '0',
		         expanded: true
		    },
		     autoLoad: true,
		     listeners:{
	         		beforeload:function(){
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
    		region:"west",
    		layout:"border",
   		 split: true,
    		//border:false,
    		width:280,
    		items:[
    		       {
    		    		region:"south",
    		    	   height:330,
    		    		border:false,
    		    	   layout:"fit",
    		    	   items:[{
    		    		   xtype: 'form',
    		               title: '<%=i18n.text("回放参数",session)%>',
    		               //witdh:200,
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
		            	   fieldLabel: '<%=i18n.text("回放速度",session)%>',
	                    	id: 'hfsd',
		                    	name: 'hfsd',
   		        	        	xtype:'slider',width: 200,
   		        	         minValue: 0,
   		        	      maxValue: 2000,
   		        	   increment: 50,
		        	        	value:1500
		               
		               },{
		            	   fieldLabel: '<%=i18n.text("回放进度",session)%>',
	                    	id: 'hfjd',
		                    	name: 'hfjd',
  		        	        	xtype:'slider',width: 200,
  		        	         minValue: 0,
  		        	      maxValue: 100,
  		        	   increment: 1,
		        	        	value:0,
		        	        	disabled:true
		               
		               },{
		            	   fieldLabel: '<%=i18n.text("总里程",session)%>',
	                    	id: 'tj1',
		                    	name: 'tj1',
 		        	        	xtype:'displayfield',
 		        	        	value:"0<%=i18n.text("小时",session)%>0<%=i18n.text("分",session)%>，0KM"
		               
		               },{
		            	   fieldLabel: '<%=i18n.text("当前里程",session)%>',
	                    	id: 'tj2',
		                    	name: 'tj2',
		        	        	xtype:'displayfield',
		        	        	value:"0<%=i18n.text("小时",session)%>0<%=i18n.text("分",session)%>，0KM"
		               
		               },{
		            	   fieldLabel: '<%=i18n.text("数据点",session)%>',
	                    	id: 'sjd',
	                    	name: 'sjd',
		        	        xtype:'checkboxfield',
		        	        checked:false,
	        	        	value:1
	               
	               },{
	            	   fieldLabel: '<%=i18n.text("过滤ACC",session)%>',
                   	id: 'acc',
                   	name: 'acc',
	        	        xtype:'checkboxfield',
	        	        checked:false,
       	        	value:1
              
              },{
	            	   text: '<%=i18n.text("加载数据",session)%>',
	            	   id:"downloadBtn",
	            	   width:100,
	            	   style:"margin:2px;",
	        	        xtype:'button',
	        	        handler:downloadData
	               },{
	            	   text: '<%=i18n.text("导出本地",session)%>',
	            	   id:"downloadBtn2",
	            	   width:100,
	            	   style:"margin:2px;",
	        	        xtype:'button',
	        	        handler:downloadData2
	               },{
	            	   text: '<%=i18n.text("开始",session)%>',
	            	   width:100,
	            	   id:"btn1",
	            	   disabled:true,
	            	   style:"margin:2px;",
	        	        xtype:'button',
	        	        handler:function(){
	        	        	var time=2000-(Ext.getCmp("hfsd").getValue()-0);
	        	             intervalProcess=setInterval("play()",time);	
	        	             Ext.getCmp("btn1").setDisabled(true);//hfsd
	        	             Ext.getCmp("hfsd").setDisabled(true);
	        	             Ext.getCmp("btn2").setDisabled(false);//hfsd
	        	             Ext.getCmp("btn3").setDisabled(false);//hfsd
	        	             Ext.getCmp("btn4").setDisabled(false);//hfsd
	        	        }
	               },{
	            	   text: '<%=i18n.text("暂停",session)%>',
	            	   width:100,
	            	   id:"btn2",
	            	   style:"margin:2px;",
	        	        xtype:'button',
		            	disabled:true,
	        	        handler:function(){
	        	        	clearInterval(intervalProcess);
	        	        	lgxInfo("<%=i18n.text("暂停历史轨迹回放",session)%>");
	        	        	 Ext.getCmp("btn1").setDisabled(false);//hfsd
	        	             Ext.getCmp("hfsd").setDisabled(false);
	        	             Ext.getCmp("btn2").setDisabled(true);//hfsd
	        	             Ext.getCmp("btn3").setDisabled(true);//hfsd
	        	             Ext.getCmp("btn4").setDisabled(true);//hfsd
	        	        }
	               },{
	            	   text: '<%=i18n.text("后退",session)%>',
	            	   width:100,
	            	   id:"btn3",
		            	disabled:true,
	            	   style:"margin:2px;",
	        	        xtype:'button',   
	        	        handler:function(){
	        	        	jj=-1;
	        	        }
	               },{
	            	   text: '<%=i18n.text("前进",session)%>',
	            	   width:100,
	            	   id:"btn4",
		            	disabled:true,
	            	   style:"margin:2px;",
	        	        xtype:'button',
	        	        handler:function(){
	        	        	jj=1;
	        	        }
	               }]
    		    	   }]
    		       },{
        		id:'tree',
        		region:"center",
        		 split: true,
         		border:false,
        		//width:180,
                animate:false,
                autoScroll: true,
                rootVisible: true,
                store:store,
    			xtype:"tree",
    			dockedItems: [{
        	        xtype: 'toolbar',
        	        items:[{
        	        	id:"SearchText",
        	        	width:180,
        	            xtype    : 'textfield',
        	            name     : 'field1',
        	            emptyText: '<%=i18n.text("输入车牌号可筛选",session)%>',
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
        	            		
        	    	        	
        	            	}
    	                	
        	            }
        	        },{text:"<%=i18n.text("查询",session)%>",handler:function(){
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
        	        }]

    		}] ,   			listeners:{
				itemclick:function(view,record,item,index,event,obj){
					var temp=record.data.id+"";
					var index=temp.indexOf("_");
					if(index>=0){
    					carid=temp.substring(index+1);
    					lgxInfo("<%=i18n.text("已选中车辆，请在左下角选择时间",session)%>");
					}else{
						carid=null;
					}
				}
			}
    	}]},
    	{
    		region:"center",
    		border:false,
        	layout: 'border',
        	items:[{
        		region:"center",
        		border:false,
            	layout: 'border',
            	items:[{
            		id:'gmap',
            		region:"center",
                    center: {
                    	lat:latDef,
                        lng:lngDef
                    },

        			contentEl:"container"
            	}]
        	},{
        		region:"south",
        		height:150,
          		 split: true,
				id:'grid',
				 store: Ext.create('Ext.data.ArrayStore', {
				        model: 'Pangram'
				    }),
				xtype:'grid',

	    	        listeners:{
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		var myLatLng_1=new AMap.LngLat(record.data.lng,record.data.lat);
	    	        		map.setCenter(myLatLng_1);
	    	        		//parent.addCar(record.data.cid,record.data.carno);
	    	        	}
	    	        },
				  columns:[
						{ xtype: 'rownumberer'},
			            
			            { text: '<%=i18n.text("车速KM",session)%>', dataIndex: 'speed',width:80 },
			            { text: '<%=i18n.text("经度",session)%>', dataIndex: 'lng' },
			            { text: '<%=i18n.text("纬度",session)%>', dataIndex: 'lat' },
			            { text: '<%=i18n.text("方向",session)%>', dataIndex: 'direction',width:40  },
			            { text: '<%=i18n.text("GPS时间",session)%>', dataIndex: 'gpstime',width:150 ,renderer:function(value,p,record){
			            	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8)+" "+value.substring(8,10)+":"+value.substring(10,12)+":"+value.substring(12,14));
			            }},
			            { text: '<%=i18n.text("地址",session)%>', dataIndex: 'address' ,width:100,renderer:function(value,p,record){
			            	return "<a href='javascript:;' onclick='latlng("+record.data.gpslat+","+record.data.gpslng+");'><%=i18n.text("点击查看地址",session)%></a>";
			            }},
			            { text: '<%=i18n.text("总里程",session)%>', dataIndex: 'mileage'},
			            { text: '<%=i18n.text("状态",session)%>', dataIndex: 'status' ,width:300},
			            { text: '<%=i18n.text("磁卡",session)%>', dataIndex: 'card' ,width:200}
				  ]
        	}]
    	}/* ,{
    		region:"south",
    		height:100,
      		 split: true,
      		 style:{overflow:"auto"},
    		contentEl:"grid_table"
    	} */]
    	
       
    }).show();

});
function latlng(lat,lng){
	//lgxInfo(lat+":"+lng);
	Lingx.post(getAddressUrl,{lat:lat,lng:lng},function(json){
		alert(json.address);
	});
}
function downloadData(){
	var stime=Lingx.toTime14(Ext.getCmp("stime").getValue());
	var etime=Lingx.toTime14(Ext.getCmp("etime").getValue());
	var acc=Ext.getCmp("acc").getValue();
	acc=acc?1:2;
	if(!carid){
		lgxInfo("<%=i18n.text("请选择要查找的车辆",session)%>");
		return;
	}

	lgxInfo("<%=i18n.text("正在加载历史数据，请稍候...",session)%>");
	reset();
	Ext.getCmp("downloadBtn").setDisabled(true);
	$.post(handlerJsp,{c:"history",id:carid,stime:stime,etime:etime,acc:acc},function(json){
		if(json.code<0){
			alert(json.message);

    		Ext.getCmp("downloadBtn").setDisabled(false);
			return;
		}
		var lat=39.916385;
        var lng=116.396621;
    	Ext.getCmp("downloadBtn").setDisabled(false);
    	if(json.list.length==0){alert("<%=i18n.text("没有轨迹",session)%>");}
		tj1(json.list);
		
		if(json.stops)
		setStopPoint(json.stops);
        if(json.list.length>0){
        	lat=json.list[0].lat;
        	lng=json.list[0].lng;
        	latlngList=json.list;
        	setBtn4Disabled(false);
        	resetGrid(latlngList);
        }else{
        	setBtn4Disabled(true);
        	lgxInfo("<%=i18n.text("该时间段内无行驶记录",session)%>");
        	resetGrid(null);
        	return;
        }
       var  mapLatLngs=[];
        if(flightPath2!=null){
    		map.remove(flightPath2);
		}
    	for(var i = 0;i<latlngList.length;i++){
    		mapLatLngs.push(new AMap.LngLat(latlngList[i].lng,latlngList[i].lat));
    	}
    	 flightPath2=new AMap.Polyline({
    		  path:mapLatLngs,
    		  strokeColor:"#EE2C2C",
    		  strokeOpacity:0.8,
    		  strokeWeight:2
    		  });
		
    	flightPath2.setMap(map);
    		
    		
    		
		var ll=new AMap.LngLat(lng,lat);
		map.setCenter(ll);
		car=createMarket(ll,"<%=i18n.text("起点",session)%>");
		
		var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(json.list[0].direction)+".png",
				size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
	    
		 car.setIcon(icon);

			var path = poly.getPath();
			path.push(ll);
	},"json");
	
}
function downloadData2(){
	var stime=Lingx.toTime14(Ext.getCmp("stime").getValue());
	var etime=Lingx.toTime14(Ext.getCmp("etime").getValue());
	var acc=Ext.getCmp("acc").getValue();
	//carid
	if(!carid){
		lgxInfo("请选择车辆!");
		return;
	}
	window.location.href="gps/action/excel.jsp?id="+carid+"&stime="+stime+"&etime="+etime+"&acc="+acc;
	//alert("正在开发...");
}
/**
 * acc: "关"
	 address: "120.952,24.805"
		 etime: "20140623161822"
		 lat: 24.805
		 latitude: "24.805"
		 lng: 120.952
		 longitude: "120.952"
		 name: "6A-4231"
		 speed: 0
		 stime: "20140623072749"
		 systemtime: "20140623072749"
		 time: "8小时50分"
		 type: 0
 */
function setStopPoint(list){
	for(var i=0;i<list.length;i++){
		var obj=list[i];
		//console.log(obj);
		var ll=new AMap.LngLat(obj.lng,obj.lat);
		var stop=createMarket(ll,"<%=i18n.text("停车时间",session)%>:"+obj.time+",ACC:"+obj.acc+",<%=i18n.text("开始时间",session)%>:"+Lingx.formatTime14(obj.stime)+",<%=i18n.text("结束时间",session)%>:"+Lingx.formatTime14(obj.etime));
		var icon=new AMap.Icon({image:"images/car/stop.png",
					size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)});
		 stop.setIcon(icon);
	}
}
function setBtn4Disabled(bool){
	if(bool){
	Ext.getCmp("btn1").setDisabled(false);//hfjd
	Ext.getCmp("btn2").setDisabled(false);
	Ext.getCmp("btn3").setDisabled(false);
	Ext.getCmp("btn4").setDisabled(false);
	Ext.getCmp("hfjd").setDisabled(false);
	}else{
		Ext.getCmp("btn1").setDisabled(false);//hfjd
		Ext.getCmp("btn2").setDisabled(true);
		Ext.getCmp("btn3").setDisabled(true);
		Ext.getCmp("btn4").setDisabled(true);
		Ext.getCmp("hfjd").setDisabled(false);
	}
}
function createMarket(latlng,title){
	
	var marker = new AMap.Marker({
	         position: latlng,
	         title: title,
			 offset:new AMap.Pixel(-20, -20),
	         //icon:image,
	         map:map
	       });
	//marker.open(map,latlng);
	 AMap.event.addListener(marker, "click", function(e){
	    	var _marker=this;
	    	//this.map.setCenter(this.position);
	    		var text="<div style='line-height:20px;'>"+_marker.getTitle().replace(/,/g,"</br>") +"</div>";
	    		
	    		var options = {
	    				map : _marker.map,
	    				position : _marker.position,
	    				content : text
	    			};
	    		var infoWin =new AMap.InfoWindow(options);
	    		infoWin.open(map,latlng);
	    }); 
	markerArray.push(marker);
	return marker;
}

function play(){
	
	if(jj==1){
		if(index>=latlngList.length)return;
		var obj=latlngList[index];
		var ll=new AMap.LngLat( obj.lng,obj.lat);
		tempPathArray.push([obj.lng,obj.lat]);
		poly.setPath(tempPathArray);
		poly.show();
		var title="<%=i18n.text("方向",session)%>:"+getDirection(obj.direction)+",<%=i18n.text("时速",session)%>:"+obj.speed+"KM/H,<%=i18n.text("时间",session)%>:"+formatTime14(obj.gpstime)+",ACC:"+(obj.acc==1?"<%=i18n.text("开",session)%>":"<%=i18n.text("关",session)%>")+",lat:"+obj.lat+",lng:"+obj.lng+"";
		if(Ext.getCmp("sjd").getValue()){
			createMarket(ll,title);
		}
		
		map.setCenter(ll);
		if(car){
			//console.log(car.getIcon());
			car.setPosition(ll);
			car.setTitle(title);
			var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(obj.direction)+".png",
				size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
	    
		 	car.setIcon(icon);

		}
		tj2(obj);//向前，统计

		index=index+jj;
		var tempObj={};
		Ext.apply(tempObj,obj);
		tempObj.direction=getDirection(obj.direction);
		addStore(tempObj);
	}else{
		var path=poly.getPath();
		if(path.length>1){
			index=index+jj;
			var ll=path.pop();
			for(var i=0;i<markerArray.length;i++){
				var obj=markerArray[i];
				if(ll==obj.getPosition()&&obj!=car){
					obj.setMap(null);
				}
			}
			var obj=latlngList[index];
			tj2(obj);//向后，统计
			var title="<%=i18n.text("方向",session)%>:"+getDirection(obj.direction)+",<%=i18n.text("时速",session)%>:"+obj.speed+"KM/H,<%=i18n.text("时间",session)%>:"+formatTime14(obj.gpstime)+",ACC:"+(obj.acc==1?"<%=i18n.text("开",session)%>":"<%=i18n.text("关",session)%>")+",lat:"+obj.lat+",lng:"+obj.lng;
			if(car){
				
				map.setCenter(ll);
				//console.log(car.getIcon());
				car.setPosition(ll);
				car.setTitle(title);
				var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(obj.direction)+".png",
					size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
		    
			 	car.setIcon(icon);
			}
		}
	}
	var jd=index/latlngList.length;
	//lgxInfo(jd*100);
	Ext.getCmp("hfjd").setValue(jd*100);
}
function reset(){
	index=0;
	for(var i=0;i<markerArray.length;i++){
		var obj=markerArray[i];
		obj.setMap(null);
		
	}
	tempPathArray=[];
	poly.setPath([]);
	clearInterval(intervalProcess);
}

function formatTime14(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14);
}

function getDirection( obj) {
	var index = 0;
	var number = obj-0;
	var nums =[  0, 45, 90, 135, 180, 225, 270, 315, 360 ];
	var array= [  "<%=i18n.text("北",session)%>", "<%=i18n.text("东北",session)%>", "<%=i18n.text("东",session)%>", "<%=i18n.text("东南",session)%>", "<%=i18n.text("南",session)%>", "<%=i18n.text("西南",session)%>", "<%=i18n.text("西",session)%>",
			"<%=i18n.text("西北",session)%>", "<%=i18n.text("北",session)%>" ];
	var min = 1000;
	for (var i = 0; i < nums.length; i++) {
		var t = Math.abs(number - nums[i]);
		if (t < min) {
			min = t;
			index = i;
		}
	}
	return array[index];
}

function resetGrid(list){/* 
	var table=$("#grid_table");
	for(var i=0;i<list.length;i++){
		var obj=list[i];
		console.log(obj);
		table.append("<tr align='center'><td>"+obj.speed+"</td><td>"+obj.mileage/1000+"</td><td><a href='javascript:;'>点击查看地址</a></td><td>"+obj.gpstime+"</td><td>"+obj.acc+"</td></tr>");
	}
	 */
}
function getCarIMGBy(fx){
	var index = 0;
	var number = fx-0;
	var nums =[  0, 45, 90, 135, 180, 225, 270, 315, 360 ];
	var min = 1000;
	for (var i = 0; i < nums.length; i++) {
		var t = Math.abs(number - nums[i]);
		if (t < min) {
			min = t;
			index = i;
		}
	}
	return nums[index];
}
function tj1(list){//总计
	var mileageStart=0;
	var mileageEnd=0;
	var timeTemp=0;
	var timeCount=0;//秒
	for(var i=0;i<list.length;i++){
		var obj=list[i];
		if(i==0){
			mileageStart=obj.mileage;
		}
		if(i==(list.length-1)){
			mileageEnd=obj.mileage;
		}
		if(timeTemp==0){
			timeTemp=obj.gpstime;
		}else{
			timeCount=timeCount+(timeToSeconds(obj.gpstime)-timeToSeconds(timeTemp));
			timeTemp=obj.gpstime;
		}
		//console.log(list[i]);
	}
	var HH=timeCount/3600;
	var mm=(timeCount%3600)/60;
	Ext.getCmp("tj1").setValue(parseInt(HH)+"<%=i18n.text("小时",session)%>"+parseInt(mm)+"<%=i18n.text("分",session)%>，"+getResult((mileageEnd-mileageStart),2)+"KM");
}
function tj2(obj){//向前，加
	if(!obj)return;
	//var timeCountTj2=0,timeTempTj2=0;
	if(timeTempTj2==0){
		timeTempTj2=obj.gpstime;
		mileageStartTj2=obj.mileage;
	}else{
		timeCountTj2=timeCountTj2+(timeToSeconds(obj.gpstime)-timeToSeconds(timeTempTj2));
		timeTempTj2=obj.gpstime;
	}
	var HH=timeCountTj2/3600;
	var mm=(timeCountTj2%3600)/60;
	Ext.getCmp("tj2").setValue(parseInt(HH)+"<%=i18n.text("小时",session)%>"+parseInt(mm)+"<%=i18n.text("分",session)%>，"+getResult((obj.mileage-mileageStartTj2),2)+"KM");
}
function getResult(num,n){
	 return Math.round(num*Math.pow(10,n))/Math.pow(10,n);
}
function timeToSeconds(value){
	var year=value.substring(0,4)-0,month=value.substring(4,6)-0;
	var days=0;
	switch(month){
	case 1:
	case 3:
	case 5:
	case 7:
	case 8:
	case 10:
	case 12:
		days=31;
		break;
	case 4:
	case 6:
	case 9:
	case 11:
		days=30;
		break;
	case 2:
		if((year%4==0&&year%100!=0)||year%400==0){
			days=29;
		}else{
			days=28;
		}
		break;
	}
	var yyyy=value.substring(0,4)*60*60*24*days*12;
	var MM=value.substring(4,6)*60*60*24*days;
	var dd=value.substring(6,8)*60*60*24;
	var HH=value.substring(8,10)*60*60;
	var mm=value.substring(10,12)*60;
	var ss=value.substring(12,14)-0;
	return yyyy+MM+dd+HH+mm+ss;
}
/*
 *   { text: '车速KM', dataIndex: 'speed',width:80 },
 { text: '经度', dataIndex: 'lng' },
 { text: '纬度', dataIndex: 'lat' },
 { text: '方向', dataIndex: 'direction',width:40  },
 { text: 'GPS时间', dataIndex: 'gpstime',width:150 ,renderer:function(value,p,record){
 	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8)+" "+value.substring(8,10)+":"+value.substring(10,12)+":"+value.substring(12,14));
 }},
 { text: '附加信息', dataIndex: 'status' ,width:100}
 */
function addStore(options){
	var gridPanel=Ext.getCmp("grid");
	gridPanel.getStore().insert(0, options);  
	//Ext.getCmp("grid").getStore().addStored(options);
	//options={speed:78,lng:112.81241,lat:23.14125,direction:'北',gpstime:'2014-08-21 10:36',status:"ACC开"};
	//gridPanel.getStore().insert(gridPanel.getStore().getCount()-1, options);  
	 //gridPanel.select(0,true,true);  
	 //gridPanel.setScrollTop(0);  
}
</script>
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
<table id="grid_table" width="100%" style="display:none;">
<tr><th>速度(KM/H)</th><th>里程(KM)</th><th>地址</th><th>时间</th><th>状态</th></tr>
</table>
</body>
</html> 