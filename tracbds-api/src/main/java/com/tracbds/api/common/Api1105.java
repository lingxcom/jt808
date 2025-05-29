package com.tracbds.api.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.service.JT808CommonService;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;
import com.lingx.service.LanguageService;
import com.lingx.service.LingxService;
import com.lingx.utils.SQLUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1105 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Autowired
	private JT808CommonService commonService;
	@Resource 
	private LanguageService languageService;
	@Override
	public int getApiCode() {
		return 1105;
	}
	@Override
	public String getApiName() {
		return "获取历史轨迹";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(params.containsKey("deviceId")&&!params.containsKey("car_id"))params.put("car_id", params.get("deviceId"));//兼容1202
		if(this.checkIsNull(params, ret, "car_id","stime","etime"))return ret;
		String car_id=IApi.getParamString(params, "car_id", "");
		String stime=IApi.getParamString(params, "stime", "");
		String etime=IApi.getParamString(params, "etime", "");
		///停车时间，分钟，小于该值不算停车
		String timeStop=IApi.getParamString(params, "timeStop", "3");//停车时间，小于这个数，不算停车
		String speedStop=IApi.getParamString(params, "speedStop", "10");//停车时速，小于这个时速算停车
		String invalid =IApi.getParamString(params, "invalid", "");
		String alarm =IApi.getParamString(params, "alarm", "");//tgps_car_alarm
		String dilution=IApi.getParamString(params, "dilution", "");//抽稀公里数，默认100米
		String language=IApi.getParamString(params, "language", "zh-CN");
		stime=SQLUtils.getValue(stime);
		etime=SQLUtils.getValue(etime);
		String where="";
		if("true".equals(invalid)) {
			where+=" and location='1' ";
		}

		String isAddr=this.lingxService.getConfigValue("jt808.history.address", "false");//是否启用轨迹的地址信息
		long st=System.currentTimeMillis();
		List<Map<String,Object>> list=this.getHistoryList(car_id, stime, etime, where);
		//System.out.println("从数据库读取历史轨迹数据，用时:"+(System.currentTimeMillis()-st)+" ms");
		
		List<Map<String,Object>> listStops=this.handlerStops(list,Integer.valueOf(timeStop),Integer.valueOf(speedStop));
		
		ret.put("stops", listStops);
		list.addAll(deepCopyList(listStops));
		 Collections.sort(list, new Comparator<Map<String,Object>>() {
	            @Override
	            public int compare(Map<String,Object> map1, Map<String,Object> map2) {
	                return map1.get("gpstime").toString().compareTo(map2.get("gpstime").toString()) ;
	            }
	        });

			//抽稀处理
		 if(Utils.isNotNull(dilution)) {
				list=dilutionExec(list,dilution);
			}
		
		//System.out.println("设置偏移经纬度，用时:"+(System.currentTimeMillis()-st)+" ms");
		this.commonService.setStatusAlarmString(list,language);
		
		if("true".equals(alarm)) {
			List<Map<String,Object>> listAlarm=this.jdbcTemplate.queryForList("select id,name,speed,gpstime,lat,lng,time,address from tgps_car_alarm where car_id=? and gpstime>? and gpstime<? order by gpstime asc",car_id, stime, etime);
			for(Map<String,Object> map:listAlarm) {
				map.put("name", this.languageService.text(map.get("name"), language));
					//map.put("address", this.addressService.getAddressOffsetGCJ02(map.get("lat").toString(),map.get("lng").toString()));
			}
			ret.put("alarms", listAlarm);
		}
		System.out.println(car_id+"获取轨迹数据记录"+list.size()+"，用时:"+(System.currentTimeMillis()-st)+" ms");
		ret.put("data", list);
		return ret;
	}
	private List<Map<String,Object>> deepCopyList(List<Map<String,Object>> list) {
		 List<Map<String, Object>> rList =new ArrayList<>();
		 for (Map<String, Object> maps : list) {
		 	Map<String, Object> map = new HashMap<String, Object>();
		 	Set entries = maps.entrySet();
		 	Iterator iterator = entries.iterator();
		 	while(iterator.hasNext()){
		 		Map.Entry entry = (Entry) iterator.next();
		 		map.put((String) entry.getKey(), entry.getValue ());
		 }
		  rList. add(map);
	}
	 return rList;
	 } 
	public List<Map<String,Object>> getHistoryList(String car_id,String stime,String etime,String where){
		List<Map<String,Object>> list=new ArrayList<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");
		try {
			Date sdate=sdf.parse(stime);
			Date edate=sdf.parse(etime);
			while(sdate.getTime()<=edate.getTime()+24*3600*1000) {
				String tableName=this.commonService.get0x0200TableName(sdate);
				String sql2="select * from "+tableName+" where car_id=? and gpstime>=? and gpstime<=? "+where+" and lat<>0 order by gpstime asc limit 100000";
				System.out.println(sql2);
				list.addAll(this.jdbcTemplate.queryForList(sql2,car_id,stime,etime));
				sdate.setDate(sdate.getDate()+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	/**
	 * 通过停车时间来判断是否停车
	 * @param list
	 * @param timeStop
	 * @return 返回停车列表点
	 */
	private List<Map<String,Object>> handlerStops(List<Map<String,Object>> list,int timeStop,int speedStop){
		boolean isRun=true;
		int sindex=-1,eindex=-1,maxSpeed=speedStop,runMax=100,whileIndex=0;
		float speed,speed_1;
		List<Map<String,Object>> listStopPoint=new ArrayList<>();
		Map<String,Object> stopPoint=null;
		
		while(isRun&&list.size()>0&&whileIndex<runMax) {
			whileIndex++;
			for(int i=0;i<list.size();i++) {
				Map<String,Object> map=list.get(i);
				speed=Float.parseFloat(map.get("speed").toString());
				speed_1=maxSpeed+1;
				if((i+1)<list.size())speed_1=Float.parseFloat(list.get(i+1).get("speed").toString());
				if(speed<=maxSpeed||(sindex!=-1&&speed_1<=maxSpeed)) {
					//停止
					if(sindex==-1) {
						//刚开始停车
						sindex=i;
					}else {
						//之前就停了，不用处理
					}
				}else {
					//行驶
					if(sindex>=0){//刚开车
						eindex=i-1;
						stopPoint=this.getStopPoint(list, sindex, eindex, timeStop);
						if(stopPoint!=null) {//满足停车条件
							listStopPoint.add(stopPoint);
							break;//跳出计算下个停车点，因为要删除list的元素
						}else {//不满足停车条件，重置标记
							sindex=-1;
							eindex=-1;
						}
						
					}
				}
				if(i>=list.size()-1) {//已经检查至最后一条数据结束处理
					isRun=false;
					if(sindex>=0){//如果是停车状态，拿最后一个点检查下
						eindex=i;
						stopPoint=this.getStopPoint(list, sindex, eindex, timeStop);
						if(stopPoint!=null) {//满足停车条件
							listStopPoint.add(stopPoint);
							break;//跳出计算下个停车点
						}
					}
				}
			}
			if(sindex>=0&&eindex>sindex) {//删除已标记停车的点
				for(int i=eindex;i>=sindex;i--) {
					list.remove(i);
				}
			}else {
				isRun=false;
			}

			sindex=-1;
			eindex=-1;
		}
	
		return listStopPoint;
	}
	
	private Map<String,Object> getStopPoint(List<Map<String,Object>> list,int sindex,int eindex,int timeStop){
		String stime=list.get(sindex).get("gpstime").toString();
		String etime=list.get(eindex).get("gpstime").toString();
		long time=difference(stime,etime);
		if(time<(timeStop*60))return null;
		Map<String,Object> mapNew=new HashMap<>();
		mapNew.putAll(list.get(sindex));
		mapNew.put("stopInfo", getTimeBy(stime,etime));
		mapNew.put("stopTime", getSecondsBy(stime,etime));
		mapNew.put("stime", stime);
		mapNew.put("etime", etime);
		return mapNew;
	}
	private List<Map<String,Object>> handlerStops(List<Map<String,Object>> list){
		List<Map<String,Object>> listRunning=new ArrayList<>();
		String type="",stime=null,etime;
		Map<String,Object> lastMap=null;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			
			if(isStop(map,list,i,type)){//停止
				if("stop".equals(type)){
					if(map.containsKey("alarm_str")) {
						String temp=map.get("alarm_str").toString();
						if(Utils.isNotNull(temp))listRunning.add(map);
					}
					
				}else{
					etime=map.get("gpstime").toString();
					stime=map.get("gpstime").toString();
					type="stop";
					StopLatlngUtils.clear();
				}
			}else {
				if("travel".equals(type)){
					if(lastMap!=null&&!listRunning.contains(lastMap))
					listRunning.add(lastMap);
					if(!listRunning.contains(map))
					listRunning.add(map);
				}else{
					if(Utils.isNotNull(stime)) {
						etime=map.get("gpstime").toString();
						Map<String,Object> mapNew=new HashMap<>();//lastMap;
						mapNew.putAll(lastMap);
						mapNew.put("stopInfo", getTimeBy(stime,etime));
						mapNew.put("stopTime", getSecondsBy(stime,etime));
						mapNew.put("stime", stime);
						mapNew.put("etime", etime);
						listRunning.add(mapNew);
						stime=null;
					}
					
					stime=map.get("gpstime").toString();
					type="travel";
					if(!listRunning.contains(map))
					listRunning.add(map);
				}
			}

			if(i==list.size()-1&&lastMap!=null){//数据点结束
				etime=map.get("gpstime").toString();
				if("stop".equals(type)&&Utils.isNotNull(stime)){
					Map<String,Object> mapNew=new HashMap<>();//lastMap;
					mapNew.putAll(lastMap);
					mapNew.put("stopInfo", getTimeBy(stime,etime));
					mapNew.put("stopTime", getSecondsBy(stime,etime));
					mapNew.put("stime", stime);
					mapNew.put("etime", etime);		
					
					listRunning.add(mapNew);
					stime=null;
				}else{
					if("stop".equals(type)&&!listRunning.contains(map))//if(!listRunning.contains(map))
					listRunning.add(map);
				}
				
				
			}
			//prevMap=map;
			lastMap=map;
		}
		
		list.clear();
		list=null;
		 return listRunning;
	}

	private double getJL(Map<String,Object> map1,Map<String,Object> map2) {

		return JT808Utils.distance(map1.get("lat").toString(), map1.get("lng").toString(), map2.get("lat").toString(), map2.get("lng").toString());
	}

	private boolean isStop(Map<String,Object> map,List<Map<String,Object>> list,int index,String type){
		boolean isStop=true;
		float count=0;
		if("stop".equals(type)&&Float.parseFloat(map.get("speed").toString())<2){//==0
			return true;
		}else if("travel".equals(type)&&Float.parseFloat(map.get("speed").toString())>=2){//!=0
			return false;
		}
		if(Float.parseFloat(map.get("speed").toString())<2){//==0
			int count0=0;
			boolean isDy20=false;//后面时速不能大于20
			//接下来的时速为0的有没有大于一半
			for(int i=index;i<list.size()&&i<(index+20);i++){
				count++;
				map=list.get(i);
				if(Float.parseFloat(map.get("speed").toString())<2)count0++;
				if(Float.parseFloat(map.get("speed").toString())>10&&!isDy20)isDy20=true;
			}
			if((count0/count)>0.3&&!isDy20){
				isStop=true;//车子在停止了
			}else{
				isStop=false;//还是行驶中
			}
			
		}else{
			int count0=0;
			boolean isDy20=false;
			//判断是不是在漂移，拿后续的20个点，如果时速为0低于半速，且有时速大于10KM为行驶
			for(int i=index;i<list.size()&&i<(index+20);i++){
				count++;
				map=list.get(i);
				if(Float.parseFloat(map.get("speed").toString())<2)count0++;
				if(Float.parseFloat(map.get("speed").toString())>10&&!isDy20)isDy20=true;
			}
			if(isDy20&&(count0/count)<0.5){
				isStop=false;//车子在行驶了
			}else{
				isStop=true;
			}
		}
		return isStop;
	}
	public static long difference(String stime,String etime) {
		long start = 0, end = 0;
		try {
			start = fromDateStringToLong(stime);
			end = fromDateStringToLong(etime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Math.abs(end - start)/1000;
		 
	}
	public static long getSecondsBy(String stime,String etime){
		long start = 0, end = 0;
		try {
			start = fromDateStringToLong(stime);
			end = fromDateStringToLong(etime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = end - start;

		return time / 1000;
	}
	public static String getTimeBy(String stime,String etime){
		long start = 0, end = 0;
		try {
			start = fromDateStringToLong(stime);
			end = fromDateStringToLong(etime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = end - start;

		long mint = time / 1000;

		//long s=mint%60;
		long m=mint/60%60;
		long h=mint/3600;
		if(m==0&&h==0)return "";
		if(h==0) {
			String temp1 = ( m + "分钟");
			return temp1;
		}else {
			String temp1 = (h + "小时" + m + "分钟");
			return temp1;
		}
		
	}
	
	public static long fromDateStringToLong(String inVal) {
		if("".equals(inVal))return 0l;
		Date date = null;
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		try {
			date = inputFormat.parse(inVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	/**
	 * 
	 * @param list
	 * @param dilution 公里
	 * @return
	 */
	private List<Map<String,Object>> dilutionExec(List<Map<String,Object>> list,String dilution){
		double distance1=Double.parseDouble(dilution)*1000;
		List<Map<String,Object>> listNew=new ArrayList<>();
		int j=0;
		for(int i=0;i<list.size();i++) {
			if(i==0) {
				listNew.add(list.get(i));
			}else if(i==(list.size()-1)) {
				listNew.add(list.get(i));
			}else {
				if(j==0)j=i;
				if(this.getJL(list.get(j), list.get(i+1))>=distance1) {
					listNew.add(list.get(j));
					j=0;
				}
			}
		}
		list.clear();
		list=null;
		return listNew;
	}
	static class StopLatlngUtils{
		private static Map<String,Integer> mapStop=new HashMap<>();
		 
		 public static void putLatlng(String latlng){
			 if(mapStop.containsKey(latlng)){
				 mapStop.put(latlng, mapStop.get(latlng)+1);
			 }else{
				 mapStop.put(latlng, 1);
			 }
		 }
		 
		 public static String getLatlng(){
			 String temp="";int num=0;
			 Set<String> keys=mapStop.keySet();
			 for(String key:keys){
				 if(mapStop.get(key)>num){
					 temp=key;
					 num=mapStop.get(key);
				 }
			 }
			 return temp;
		 }
		 
		 public static void clear(){
			 mapStop.clear();
		 }
	}
}
