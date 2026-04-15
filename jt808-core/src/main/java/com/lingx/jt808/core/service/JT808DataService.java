package com.lingx.jt808.core.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.service.LingxService;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.utils.Utils;

@Component(value="JT808DataService")
public class JT808DataService {
	
	public static ConcurrentLinkedQueue<Map<String,Object>> LocatoinData=new ConcurrentLinkedQueue<Map<String,Object>>();
	
	public static Cache<String, Integer> UPDATE_CAR_CACHE = CacheBuilder.newBuilder().maximumSize(1000000).expireAfterWrite(5, TimeUnit.MINUTES).build();
	
	public static Cache<String, List<Map<String,Object>>> OIL_CACHE=CacheBuilder.newBuilder().maximumSize(200000).expireAfterAccess(30, TimeUnit.MINUTES).build();

	@Value("#{configs['jt808.server.0x0200.autoadd']}")
	private String autoadd="true";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Autowired
	private JT808CommonService commonService;
	@Autowired
	private RedisService redisService;
	/**
	 * 允许上报的设备ID
	 */
	//private Set<String> tids=Collections.synchronizedSet(new HashSet<>());
	/**
	 * 车辆注册 
	 * {"p0":"2013","p1":0,"p2":0,"p3":"3031313131","p4":"4C494E4758313131313131313131313131313131","p5":"31313131313131","p6":1,"p7":"闽D12345","tid":"013800138999"}
	 * @param map
	 * @return 0：成功；1：车辆已被注册；2：数据库中无该车辆；
		3：终端已被注册；4：数据库中无该终端
	 */
	public int handle0100(Map<String,Object> map) {
		
		return 0;
	}
	/**
	 * 终端注销
	 * @param tid
	 */
	public void handle0003(String tid) {
		
	}
	/**
	 * 下发鉴权码
	 * @return
	 */
	public String get0100Code() {
		return "1234567890";
	}
	/**
	 * 检查设备上报的鉴权码
	 * @param code
	 * @return
	 */
	public boolean check0102Code(String code) {
		return true ;
	}
	
	public void saveEvent(String tid, String event) {
		//this.jdbcTemplate.update("insert into tgps_event(car_id,event,ts) values(?,?,?)",tid,event,Utils.getTime());
		this.commonService.pushMessageByCarid(tid, event);
	}
	
	public void handl0900(String tid,int type,byte[] data) {
			String hexstring=Utils.bytesToHex(data);
		
		this.jdbcTemplate.update("insert into tgps_jt808_touchuan(tid,type,data,ascii_code,uord,ts,remark) values(?,?,?,?,?,?,?)",
				tid,String.format("0x%02X", type),hexstring,new String(data),"上行",Utils.getTime(),"");
	}
	
	public String getOil(String tid,String oil) {
		
		float ret=0;
		if(OIL_CACHE.getIfPresent(tid)==null) {
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select key1,value1 from tgps_oiltype_item where oiltype_id in (select bylc from tgps_car where id=? ) order by key1 asc",tid);
			OIL_CACHE.put(tid, list);
		}
		if(OIL_CACHE.getIfPresent(tid)!=null) {
			 List<Map<String,Object>> list=OIL_CACHE.getIfPresent(tid);
			 if(list.size()==0)return oil;
			 int index=-1;
			 for(int i=0;i<list.size();i++) {
				 Map<String,Object> m=list.get(i);
				 if(Float.parseFloat(oil)>=Float.parseFloat(m.get("key1").toString())) {
					 index=i;
				 }
			 }
			 if(index>=0) {
				 ret=Float.parseFloat(list.get(index).get("value1").toString());
				 if(index<list.size()-1) {
					 float temp=Float.parseFloat(oil);
					 float temp1=Float.parseFloat(list.get(index).get("key1").toString());
					 float temp2=Float.parseFloat(list.get(index+1).get("key1").toString());
					 float percentage=(temp-temp1)/(temp2-temp1);

					 temp1=Float.parseFloat(list.get(index).get("value1").toString());
					 temp2=Float.parseFloat(list.get(index+1).get("value1").toString());
					 ret=ret+(temp2-temp1)*percentage;
				 }
			 }
		}
		return String.format("%.2f", ret);
	}
	/**
	 * 多媒体事件信息上传
	 */
	public void save0800(Map<String, Object> map) {
		String sql="insert into tgps_0800(tid,mid,type,gs,event,tdh,systime) values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,map.get("tid"),map.get("p1"),map.get("p2"),map.get("p3"),map.get("p4"),map.get("p5"),Utils.getTime());
	}
	/**
	 * 媒体文件上报
	 */
	public void save0801(Map<String, Object> map) {
		String sql="insert into tgps_car_photo(car_id,path,acc,lat,lng,speed,fx,gpstime,mid,type,gs,event,tdh,systime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,map.get("car_id"),map.get("filepath"),map.get("acc"),map.get("lat"),map.get("lng"),map.get("speed"),map.get("direction"),map.get("gpstime"),
				map.get("p1"),map.get("p2"),map.get("p3"),map.get("p4"),map.get("p5"),Utils.getTime());
	}
	
	
	public void save0200(Map<String, Object> map) {
		if(map.get("tid")==null)return;
		
		if(map.get("speed")==null){
			map.put("speed", "0");
		}
		if(map.get("direction")==null){
			map.put("direction", "0");
		}
		if(map.get("alarm")==null){
			map.put("alarm", "0");
		}
		if(map.get("status")==null){
			map.put("status", "0");
		}
		if(map.get("height")==null){
			map.put("height", "0");
		}
		if(map.get("A01")==null){
			map.put("A01", "0");
		}
		if(map.get("A02")==null){
			map.put("A02", "0");
		}
		if(map.get("A30")==null){
			map.put("A30", "0");
		}
		if(map.get("A31")==null){
			map.put("A31", "0");
		}
		if(map.get("status_str")==null){
			map.put("status_str", "");
		}
		if(map.get("alarm_str")==null){
			map.put("alarm_str", "");
		}
		if(map.get("lat")==null){
			map.put("lat", "0");
		}
		if(map.get("lng")==null){
			map.put("lng", "0");
		}
		//long status=Long.parseLong(map.get("status").toString());
		if(UPDATE_CAR_CACHE.getIfPresent(map.get("tid").toString())==null) {
			UPDATE_CAR_CACHE.put(map.get("tid").toString(), 1);
			updateCar(map,"true".equals(this.autoadd));
			
		}
		LocatoinData.add(map);
		/*
		try {
			this.jdbcTemplate.update(String.format("insert into %s(car_id,acc,location,alarm,status,lat,lng,height,speed,direction,gpstime,systime,mileage,oil,txxh,wxxh,status_str,alarm_str) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", this.jt808CommonService.get0x0200TableName(map.get("gpstime").toString())),
					map.get("tid"),(status&0b01)>0?"1":"0",(status&0b10)>0?"1":"0",map.get("alarm"),map.get("status"),map.get("lat"),map.get("lng"),map.get("height"),map.get("speed"),map.get("direction"),map.get("gpstime"),Utils.getTime(),map.get("A01"),map.get("A02"),map.get("A30"),map.get("A31")
							,map.get("status_str"),map.get("alarm_str"));
		} catch (Exception e) {
			System.out.println(JSON.toJSONString(map));
			e.printStackTrace();
		}
		*/
	}
	
	public void updateCar(Map<String, Object> map, boolean isAutoAdd) {
		long status = Long.parseLong(map.get("status").toString());
		String sql = "update tgps_car set online='1', acc=?,location=?,alarm=?,status=?,lat=?,lng=?,height=?,speed=?,direction=?,mileage=?,gpstime=?,systime=? where id=?";
		this.jdbcTemplate.update(sql, (status & 0b01) > 0 ? "1" : "0", (status & 0b10) > 0 ? "1" : "0",
				map.get("alarm"), map.get("status"), map.get("lat"), map.get("lng"), map.get("height"),
				map.get("speed"), map.get("direction"), map.get("A01"), map.get("gpstime"), Utils.getTime(),
				map.get("car_id"));
		
	}
	
	public void addCar(String tid) {
		if(!StringUtils.isNumber(tid))return;
		tid=tid.trim();
		while (tid.charAt(0) == '0') {
			tid = tid.substring(1);
		}
		String time = Utils.getTime();
		String ets = this.lingxService.getTime(1, 1);
		String sql = "insert into tgps_car(tid,carno,sim,czxm,tel,remark,version,create_time,modify_time,stime,etime)values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, tid, tid, "","", "", "", "", time, time,
				time.substring(0, 8), ets.substring(0, 8));
		int car_id=this.jdbcTemplate.queryForObject("select id from tgps_car where tid=?", Integer.class,tid);
		IJT808Cache.WHITE_LIST.put(tid, String.valueOf(car_id));
	}

	public List<Map<Integer,Object>> getListByCsv(String filepath){
		try {
			String temp=FileUtils.readFileToString(new File(System.getProperty("user.dir")+"/files/"+filepath), "GBK");
			String array[]=temp.split("\n");
			List<Map<Integer,Object>> list=new ArrayList<>();
			for(String str:array) {
				String array2[]=str.trim().split(",");
				Map<Integer,Object> map=new HashMap<>();
				for(int i=0;i<array2.length;i++) {
					map.put(new Integer(i), array2[i]);
				}
				list.add(map);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public String importCar(String filepath,String groupId) {
		List<Map<Integer,Object>> list=getListByCsv(filepath);
		String tid=null;
		int count=0;
		int count2=0;
		int index=0;
		String time=Utils.getTime();
		String ets=this.lingxService.getTime(1,1);
		try {
			for(Map<Integer,Object> map:list) {
				if(index++==0)continue;
				tid=map.get(0).toString();
				tid=Utils.parseTid(tid);
				for(int i=2;i<=7;i++) {
					if(map.get(i)==null)map.put(Integer.valueOf(i), "-");
				}
				System.out.println(JSON.toJSONString(map));
				if(this.lingxService.queryForInt("select count(*) from tgps_car where tid=?",tid)==0){
					count+=this.jdbcTemplate.update("insert into tgps_car(tid,carno,sim,czxm,tel,online,stime,etime,lat,lng,speed,direction,location,acc,gpstime,systime,status,remark,create_time,modify_time,group_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							tid,map.get(1),map.get(2),map.get(3),map.get(4),3,time.substring(0, 8),ets.substring(0, 8),39.916385,116.396621,0,0,0,0,null,null,1,"",time,time,groupId);
				}else{
					count2+=this.jdbcTemplate.update("update tgps_car set carno=?,sim=?,czxm=?,tel=?,group_id=? where tid=?",map.get(1),map.get(2),map.get(3),map.get(4),groupId,tid);

				}
			}
			this.redisService.publishSystemCommand(3003);
			return String.format("导入%s条数据，更新%s条数据，合计：%s。", count,count2,count+count2);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	public String importOil(String filepath,String oilTypeId) {
		List<Map<Integer,Object>> list=getListByCsv(filepath);
		int count=0;
		int index=0;
		try {
			this.jdbcTemplate.update("delete from tgps_oiltype_item where oiltype_id=?",oilTypeId);
			for(Map<Integer,Object> map:list) {
				if(index++==0)continue;
				System.out.println(JSON.toJSONString(map));
				count+=this.jdbcTemplate.update("insert into tgps_oiltype_item(oiltype_id,key1,value1) values(?,?,?)",oilTypeId,map.get(0),map.get(1));
			}
			OIL_CACHE.invalidateAll();
		//this.redisService.publishSystemCommand(3003);
			return String.format("导入%s条数据。", count);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	

	/**
	 * 驾驶员身份信息采集上报
	 */
	public void save0702(Map<String, Object> map) {
		String tid=map.get("car_id").toString();
		String status=map.get("p1").toString();
		String time=map.get("p2").toString();

		String ret=map.get("p3").toString();
		String name=map.get("p4").toString();
		String code=map.get("p5").toString();
		String org=map.get("p6").toString();
		String date=map.get("p7").toString();
		switch(Integer.parseInt(status)){
		
		case 1://上班
			this.jdbcTemplate.update("insert into tgps_0702(car_id,type,ts,ret,name,code,org,yxq) values(?,?,?,?,?,?,?,?)",
					tid,status,time,ret,name,code,org,date);
			
			break;
		case 2://下班
			this.jdbcTemplate.update("insert into tgps_0702(car_id,type,ts,ret,name,code,org,yxq) values(?,?,?,?,?,?,?,?)",
					tid,status,time,ret,name,code,org,date);
			break;
		default:
		}
			
	}
}
