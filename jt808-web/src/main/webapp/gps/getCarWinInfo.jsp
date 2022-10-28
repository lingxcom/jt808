<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%
    		org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);

	com.lingx.core.service.ILingxService lingx=spring.getBean(com.lingx.core.service.ILingxService.class);
	String temp8=lingx.getConfigValue("lingx.gps.map.windowinfo", "1,400,240");
	String array8[]=temp8.split(",");
	request.setAttribute("WindowInfoType",array8[0] );
	request.setAttribute("WindowInfoWidth",array8[1] );
	request.setAttribute("WindowInfoHeight",array8[2] );
    %>
function getCarWinInfo(json,videoEnabled){
videoEnabled=videoEnabled||false;
var text="<div style='width:${WindowInfoWidth}px;height:${WindowInfoHeight}px;font-size:14px;'><table class='table' width='100%'>";
    		text=text+"<tr><td style='border-top:0 none;font-size:14px;'>"+json.info.carno+"</td></tr>";
    		text=text+"<tr><td style='font-size:14px;'>"+json.info.gpstime+" <span style='color:#aaa;'>（<%=i18n.text("终端上报时间",session)%>）</span></td></tr>";//定位
    		//客户:参数1产品:参数2重量:参数3起点:参数4终点:参数5
    		text=text+"<tr><td style='font-size:14px;'>"+json.info.address+"</td></tr>";
    		//text=text+"<tr><td style='font-size:14px;'>客户:参数1，产品:参数2，重量:参数3，起点:参数4，终点:参数5</td></tr>";
    		if(WindowInfoType=="1"){
    		text=text+"<tr><td style='font-size:14px;'><%=i18n.text("当前状态",session)%>："+json.info.status+"</td></tr>";
    		text=text+"<tr><td style='font-size:14px;'><%=i18n.text("车主",session)%>："+json.info.czxm+" | ";
    		text=text+"<%=i18n.text("电话",session)%>："+json.info.tel+" | ";
    		text=text+"<%=i18n.text("车载号码",session)%>："+json.info.tphone+"</td></tr>";
    		}
    		
    		//text=text+"油量："+json.info2.currentoil+"</td></tr>";
    		text=text+"<tr><td style='font-size:14px;'>";
    		text=text+(json.info.status.indexOf("ACC开")>-1?"<span style='color:green'>车辆点火</span>":"<span style='color:red'>车辆熄火</span>") +" | ";
    		
    		text=text+"<%=i18n.text("时速",session)%>："+json.info.speed+"KM | ";
    		text=text+"<%=i18n.text("方向",session)%>："+json.info.fx+" ";//+" | ";
    		if(json.info.csq){
    			text=text+" | 信号："+json.info.csq;
    		}
    		text=text+" | <%=i18n.text("电压",session)%>："+(json.info.voltage||0)+"V </td></tr>";
    		
    		
    		//text=text+"到期时间："+json.info1.endservertime+"";
    		
    		text=text+"<tr><td><a href='gps/single_gaode.jsp?id="+json.info.id+"' target='_blank'><%=i18n.text("实时轨迹",session)%></a>"
    		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("行程",session)%>\",\"e?e=tgps_xcjl&m=grid&car_id="+json.info.id+"\");'><%=i18n.text("行程记录",session)%></a>"
    		
    		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("指令",session)%>\",\"gps/dialogs/command.jsp?id="+json.info.id+"\");'><%=i18n.text("下发指令",session)%></a>"
    		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("参数",session)%>\",\"gps/dialogs/parameter.jsp?id="+json.info.id+"\");'><%=i18n.text("参数设置",session)%></a>"
    		
    		//+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("报警",session)%>\",\"e?e=tgps_car_alarm&m=grid&car_id="+json.info.id+"\");'><%=i18n.text("报警记录",session)%></a>"
    		//+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("指令",session)%>\",\"e?e=tgps_car_cmd&m=grid&car_id="+json.info.id+"\");'><%=i18n.text("指令日志",session)%></a>"
    		;
    		
    		if(json.info.ty){
    			text+=" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("胎压",session)%>\",\"gps/dialogs/ty.jsp?id="+json.info.id+"\");'><%=i18n.text("胎压监测",session)%></a>"
    		}
    		if(videoEnabled){
    		//text=text+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("视频播放",session)%>\",\"video/index.jsp?id="+json.info.id+"\");'><%=i18n.text("视频播放",session)%></a>"
        	//	+" | <a href='javascript:;' onclick='openReadonlyWindow(\""+json.info.carno+"-<%=i18n.text("视频回放",session)%>\",\"video/history.jsp?id="+json.info.id+"\",\"full1\");'><%=i18n.text("视频回放",session)%></a>"
        		
    		}
    		text +="</td></tr>";
    		if(json.info.isobd==1){
    			text=text+" <tr><td> <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("健康检查",session)%>\",\"gps/obd/jkjc.jsp?id="+json.info.id+"\");'><%=i18n.text("健康检查",session)%></a>"
        		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("车辆信息",session)%>\",\"gps/obd/clxx.jsp?id="+json.info.id+"\");'><%=i18n.text("车辆信息",session)%></a>"
        		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-OBD\",\"gps/obd/obd.jsp?id="+json.info.id+"\");'>OBD</a>"
        		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("故障码",session)%>\",\"gps/obd/gzm.jsp?id="+json.info.id+"\");'><%=i18n.text("故障码",session)%></a>"
        		+" | <a href='javascript:;' onclick='openWindow2(\""+json.info.carno+"-<%=i18n.text("行程记录",session)%>\",\"gps/obd/xcjl.jsp?id="+json.info.id+"\");'><%=i18n.text("行程记录",session)%></a></td></tr>"
        		;
    		}
    		
    		text +="</table></div>";
    		return text;
}