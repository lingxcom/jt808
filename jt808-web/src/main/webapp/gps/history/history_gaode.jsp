<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.gps.service.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!
public String date1(Object obj){
	try{
	String temp=obj.toString();
	return temp.substring(0,4)+"-"+temp.substring(4,6)+"-"+temp.substring(6,8)+" "+temp.substring(8,10)+":"+temp.substring(10,12);
	}catch(Exception e){
		return "0000-00-00 00:00";
	}
}
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ApplicationContext spring = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
	org.springframework.jdbc.core.JdbcTemplate jdbc=spring.getBean(org.springframework.jdbc.core.JdbcTemplate.class);
	if(session.getAttribute(com.lingx.core.Constants.SESSION_USER)==null){
		response.sendRedirect(basePath+"login.jsp");
		return ;
	}
	com.lingx.core.model.bean.UserBean userBean=(com.lingx.core.model.bean.UserBean)session.getAttribute(com.lingx.core.Constants.SESSION_USER);

	Map<String,Object> car=jdbc.queryForMap("select * from tgps_car where id=?",request.getParameter("id"));
	
	request.setAttribute("car", car);

	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	Date date=new Date();
	request.setAttribute("edate",sdf.format(date));
	date.setDate(date.getDate()-1);
	request.setAttribute("sdate",sdf.format(date));
	
	
%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>历史轨迹</title>
    <link rel="stylesheet" href="//cache.amap.com/lbs/static/main1119.css"/>
    <script src="//cache.amap.com/lbs/static/es5.min.js"></script>
<script type="text/javascript" src="//webapi.amap.com/maps?v=1.3&key=e607415290b3792980c29664c6bd5b5b&plugin=AMap.MapType"></script> 
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>   
<script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>

<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>

<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="lingx/js/lingx.js"></script>
<script type="text/javascript" src="lingx/js/commonApi.js"></script>


<link href="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<style type="text/css">  
#container{height:100%}  

#dialog{
width:304px;
height:320px;
position:fixed;
top:10px;
right:100px;
background-color:#fff;
z-index:9999;
}
</style>  
</head>
<body>
<div id="dialog" class="panel panel-primary">
<div class="panel-heading">${car.carno } [${car.id }]</div>
<div class="col-md-12">
&nbsp;
<form>

  <div class="form-inline">
  <div class="form-group">
    <label for="exampleInputEmail1">开始时间：</label>
    <input id="stime" name="stime" type="input" class="form-control form_date"  data-date-format="yyyy-mm-dd hh:ii"  id="exampleInputEmail1" placeholder="开始时间" value="${sdate }">
  </div>
  </div>
  
  <div class="form-inline" style="margin-top:10px;">
  <div class="form-group">
    <label for="exampleInputEmail1">结束时间：</label>
    <input id="etime" name="etime" type="input" class="form-control form_date"  data-date-format="yyyy-mm-dd hh:ii"  id="exampleInputEmail1" placeholder="结束时间" value="${edate }">
  </div>
  </div>
  
  
  
  
  <div class="form-group" style="display:none;">
   <div class="checkbox">
        <label>
          <input id="isLine" type="checkbox" > 绑定线路
        </label>
      </div>
      </div>
      
<div class="progress"  style="margin-top:10px;">
  <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
    0%
  </div>
</div>
   <div  class="form-group">
  <button id="btn1" type="button" class="btn btn-primary" >加载数据</button>
  <button id="btn2" type="button" class="btn btn-success" disabled>开始播放</button>
  <button id="btn3" type="button" class="btn btn-warning" disabled>暂停播放</button>
  </div>
  <div class="form-group">
  <button id="btn4" type="button" class="btn btn-danger" disabled>清空数据</button>
   <button id="btn5" type="button" onclick="closeWindow()" class="btn btn-danger" >关闭窗口</button>
  </div>
</form>

</div>
</div>

<script type="text/javascript">
//日期插件
/*
  
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0

 */
$(".form_date").datetimepicker({
	language:  'zh-CN',
    weekStart: 1,
    todayBtn:  1,
	autoclose: 1,
	todayHighlight: 1,
	startView: 2,
	forceParse: 0,
    showMeridian: 1

});
</script>

<div id="container"></div> 
<script type="text/javascript">
var latDef= 39.916385;
var lngDef=116.396621;
var tempPathArray=[],latlngList,intervalProcess,jj=1,index=1,infoWindow,car,progress=0,flightPath2;
var map = new AMap.Map('container',{
    resizeEnable: true,
    zoom: 14,
    center: [lngDef, latDef]
});
var poly=new AMap.Polyline({
	map:map,
	 strokeColor: "#F00",  //线颜色
     // strokeOpacity: 1,     //线透明度
     strokeWeight: 5  
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

$(function(){
	$("#btn1").bind("click",function(){
		//$("#btn1").attr("disabled","disabled");
		$("#btn2").removeAttr("disabled");
		$("#btn3").removeAttr("disabled");
		var isLine=$("#isLine")[0].checked;//是否绑定线路
		if(isLine){
			//绑定线路开始==================================
			//绑定线路结束==================================
		}else{
			//非绑定线路

			//if($("#stime").val().substring(0,10)!=$("#etime").val().substring(0,10)){alert("不支持跨天查轨迹，日期必须一致！");return;}
			$.post("gps/history/handler.jsp",{c:"history2",id:"${car.id}",stime:$("#stime").val(),etime:$("#etime").val(),acc:0},function(json){
				if(json.stops)
					setStopPoint(json.stops);
				var lat,lng;
				if(json.list&&json.list.length>0){
		        	lat=json.list[0].lat;
		        	lng=json.list[0].lng;
		        	latlngList=json.list;
		        	
		        }else{
		        	alert("该时间段内无行驶记录");
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
				car=createMarket(ll,"起点");
				
				var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(json.list[0].direction)+".png?rand="+Math.random(),
						size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
			    
				 car.setIcon(icon);
				 tempPathArray.push([lng,lat]);
				 poly.setPath(tempPathArray);
				 poly.show();
				 
				 var text=getText(json.list[0]);
		    		 infoWindow =new AMap.InfoWindow({visible: true,map:map,position: car.position,content: text,width:300,height:100}); 
		    		infoWindow.open(map,car.getPosition());
				 
				 $("#btn2").bind("click",function(){
					 $("#btn2").attr("disabled","disabled");
					 $("#btn3").removeAttr("disabled");
					 intervalProcess=setInterval("play()",300);
				 });
				 $("#btn3").bind("click",function(){
					 $("#btn2").removeAttr("disabled");
					 $("#btn3").attr("disabled","disabled");
					 clearInterval(intervalProcess);
				 });
				 $("#btn4").bind("click",function(){
					 window.location.reload();
				 });
				 $("#btn4").removeAttr("disabled");
				 $("#btn1").attr("disabled","disabled");
				 $(".form_date").attr("disabled","disabled");
				 
			},"json");
			//
		}
	});
});

function play(){
	
	if(jj==1){
		if(index>=latlngList.length){
			 clearInterval(intervalProcess);
			alert("历史轨迹回放完毕！");
			return;
		}
			
		var obj=latlngList[index];
		var ll=new AMap.LngLat( obj.lng,obj.lat);
		tempPathArray.push([obj.lng,obj.lat]);
		poly.setPath(tempPathArray);
		poly.show();
		
		
		//map.setCenter(ll);
		if(car){
			//console.log(car.getIcon());
			car.setPosition(ll);
			car.setTitle(getText(obj));
			var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(obj.direction)+".png?rand="+Math.random(),
				size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
	    
		 	car.setIcon(icon);

		 	infoWindow.setPosition(ll);
		 	infoWindow.setContent(getText(obj));
		}

		index=index+jj;
		setProgress(parseInt(index/latlngList.length*100));
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
			var title="<%=i18n.text("方向",session)%>:"+getDirection(obj.direction)+",<%=i18n.text("时速",session)%>:"+obj.speed+"KM/H,<%=i18n.text("时间",session)%>:"+formatTime14(obj.gpstime)+",ACC:"+(obj.acc==1?"<%=i18n.text("开",session)%>":"<%=i18n.text("关",session)%>")+",lat:"+obj.lat+",lng:"+obj.lng;
			if(car){
				
				//map.setCenter(ll);
				//console.log(car.getIcon());
				car.setPosition(ll);
				car.setTitle(title);
				var icon=new AMap.Icon({image:"images/car/360/"+getCarIMGBy(obj.direction)+".png?rand="+Math.random(),
					size:new AMap.Size(40, 40),imageOffset:new AMap.Pixel(0, 0)}); //shadow
		    
			 	car.setIcon(icon);
			}
		}
	}
	var jd=index/latlngList.length;
	//lgxInfo(jd*100);
}

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

function createMarket(latlng,title){
	
	var marker = new AMap.Marker({
	         position: latlng,
	         title: title,
	         //icon:image,
	         offset:new AMap.Pixel(-15, -25),
	         map:map
	       });
	//marker.open(map,latlng);
	 AMap.event.addListener(marker, "click", function(e){
	    	var _marker=this;
	    	var text=_marker.getTitle();
	    	text=text.replace(/,/g,"<br>");
	    	var iw=new AMap.InfoWindow({visible: true,map:map,position: _marker.position,content: text,width:300,height:100}); 
	    	iw.open(map,_marker.getPosition());
	    	/*
	    	if(!infoWindow){
				 var text=getText(latlngList[index]);
		    		 infoWindow =new AMap.InfoWindow({visible: true,map:map,position: _marker.position,content: text,width:300,height:100}); 
		    		infoWindow.open(map,_marker.getPosition());
	    	}else{
	    		infoWindow.open(map,_marker.getPosition());
	    	}
	    	*/
	    }); 
	//markerArray.push(marker);
	return marker;
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
function formatTime14(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12);
}
function getText(json){
	return "方向:"+getDirection(json.direction)+"<br>时速:"+json.speed+"KM/H<br>时间:"+formatTime14(json.gpstime);
}

function setProgress(num){
	$(".progress-bar").html(num+"%");
	$(".progress-bar").css("width",num+"%");
}
function setPoints(list){
	for(var i=0;i<list.length;i++){
		var json=list[i];
		var title="方向:"+getDirection(json.direction)+",时速:"+parseInt(json.speed)+"KM/H,时间:"+(json.create_time);
		var ll=new AMap.LngLat(json.longitude,json.latitude);
		//images/analysisiconon.png
		var marker = new AMap.Marker({
	         position: ll,
	         title: title,
	         icon:new AMap.Icon({image:'images/analysisiconon.png',size:new AMap.Size(16, 16),imageOffset:new AMap.Pixel(0, 0)}), //shadow
		     offset:new AMap.Pixel(-8, -8),
		     extData:{lat:json.latitude,lng:json.longitude},
	         //visible:false,
	         //icon:image,
	         //offset:new AMap.Pixel(-15, -25),
	         map:map
	       });
		 AMap.event.addListener(marker, "click", function(e){
		    	var _marker=this;
		    	var extData=_marker.getExtData();
		    	
		    	$.post("gps/action/getAddress.jsp",extData,function(json){
		    		var text=_marker.getTitle();
			    	text=text.replace(/,/g,"<br>");
			    	text+="<br>地址:"+json.address;
			    	var iw=new AMap.InfoWindow({visible: true,map:map,position: _marker.position,content: text,width:300,height:100}); 
			    	iw.open(map,_marker.getPosition());
		    	},"json");
		    	
		    	/*
		    	if(!infoWindow){
					 var text=getText(latlngList[index]);
			    		 infoWindow =new AMap.InfoWindow({visible: true,map:map,position: _marker.position,content: text,width:300,height:100}); 
			    		infoWindow.open(map,_marker.getPosition());
		    	}else{
		    		infoWindow.open(map,_marker.getPosition());
		    	}
		    	*/
		    });
	}
}

function lingxSubmit(){
	closeWindow();
}
</script>
</body>
</html> 