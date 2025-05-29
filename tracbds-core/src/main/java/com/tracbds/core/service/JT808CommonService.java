package com.tracbds.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.utils.DateUtils;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.LatLngUtils;
import com.tracbds.core.utils.Utils;
import com.lingx.service.LanguageService;

@Component(value = "JT808CommonService")
public class JT808CommonService {
	public static final Map<String, String> TID_CARNO = new HashMap<>();// tid与carno的映射缓存
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//@Autowired
	//private JT808AlarmService alarmService;
	@Autowired
	private DatabaseConfigService databaseConfigService;
	@Resource 
	private LanguageService languageService;
	@Value("#{configs['jt808.server.0x0200.autoadd']}")
	private String autoadd = "true";

	@Value("#{configs['gps.car.endtime.enabled']}")
	private String enabledEndTime="false";
	@PostConstruct
	public void init() {
		
	}

	public String get0x0200TableName() {
		return this.get0x0200TableName(new Date());
	}

	public String get0x0200TableName(String date) {
		return String.format("tgps_data_%s", date.substring(0, 8));
	}

	public String get0x0200TableName(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return get0x0200TableName(sdf.format(date));
	}
	public Map<String,Object> getLast0x0200Data(String car_id){
		String temp=IJT808Cache.get(car_id);
		if(temp==null) {
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select * from tgps_car where id=?",car_id);
			list.get(0).put("car_id", list.get(0).get("id"));
			
			List<Map<String,Object>> listChannel=this.jdbcTemplate.queryForList("select name,tdh channel from tgps_car_tdh where car_id=? order by tdh asc",car_id);
			list.get(0).put("channels", listChannel);
			IJT808Cache.set(car_id, JSON.toJSONString(list.get(0)));
			return list.get(0);
		}
		return JSON.parseObject(temp);
	}
	public void refreshCache() {}
	public void refreshCache(Object id) {
		IJT808Cache.del(id.toString());;
	}

	public void addJT808Info(Map<String, Object> map,String language) {
		if (map == null)
			return;
		if(language==null)language="zh-CN";
		String car_id = map.get("car_id").toString();
		map.put("type", "0x0200");
		map.put("online", this.getOnline(map));
		map.put("status_jt808", this.getJT808Status(map));
		map.put("is_address", "false");
		map.put("comment", this.getComment(map,language));
		map.put("status_leftcar", this.getLeftCarStatusString(map,language));// 左侧树型状态描述
		map.put("dtlc", this.getDtlc(car_id));
		if(!map.containsKey("status_str"))map.put("status_str", "-");
		if(!map.containsKey("alarm_str"))map.put("alarm_str", "-");
		//JT808Utils.putMap("status_str", alarmService.handleStatus(Long.parseLong(map.get("status").toString()),language), map);
		//JT808Utils.putMap("alarm_str", alarmService.handleAlarm(Long.parseLong(map.get("alarm").toString()),language), map);
		
		map.put("address", "-");// 改为不是每次上报都需要取地址，被调用时再取

		// map.put("address",
		// this.addressService.getAddress(map.get("lat").toString(),map.get("lng").toString()));
	}

	public String getLeftCarStatusString(Map<String, Object> map,String language) {
		if("true".equals(enabledEndTime)&&map.containsKey("etime")&&!"".equals(map.get("etime").toString())) {
			String etime=map.get("etime").toString();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			try {
				Date date=sdf.parse(etime);
				long time=date.getTime()-System.currentTimeMillis();
				if(time<(15*24*60*60*1000l)) {
					if(time<0)
					return this.languageService.text("平台到期", language);
					else {
						time=time/(24*60*60*1000)+1;
						return time+this.languageService.text("天后到期", language);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (map == null)
			return "";
		if (map.get("speed") == null)
			map.put("speed", 0);
		if (map.get("online") == null || map.get("comment") == null)
			return "";
		// this.addJT808Info(map);
		if ("0".equals(map.get("online").toString()))
			return map.get("comment").toString();
		float speed = Float.parseFloat(map.get("speed").toString());
		if (speed > 0) {
			return new Float(speed).intValue() + " KM/H";
		} else {
			return this.languageService.text("静止", language);
		}
	}
	public String getDtlc(String tid) {
		String temp = IJT808Cache.get("dtlc_" + tid);
		if (temp == null)
			return "0";
		else
			return String.format("%.1f", new Float(temp));//new Float(temp).intValue();
	}

	

	public String getComment(Map<String, Object> map,String language) {
		String comment = "";
		try {
			if ("1".equals(map.get("online").toString())) {
				String tid = map.get("tid").toString();
				String changeTime = IJT808Cache.get(tid + "_speed_change");
				if (changeTime == null) {
					changeTime = Utils.getTime();
					IJT808Cache.set(tid + "_speed_change", Utils.getTime());
				}
				comment = this.languageService.text("在线", language)+ (0 == Float.parseFloat(map.get("speed").toString()) ? this.languageService.text("停止", language) : this.languageService.text("行驶", language)) + "("
						+ getDistanceTime(Utils.getTime(), changeTime,language) + ")";
			} else {
				comment = this.languageService.text("离线", language)+"(" + getDistanceTime(Utils.getTime(), map.get("systime").toString(),language) + ")";
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return comment;
	}
	 private  String getDistanceTime(String str1, String str2,String language) {  
		  SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
	        Date one;  
	        Date two;  
	        long day = 0;  
	        long hour = 0;  
	        long min = 0;  
	        long sec = 0;  
	        try {  
	            one = df.parse(str1);  
	            two = df.parse(str2);  
	            long time1 = one.getTime();  
	            long time2 = two.getTime();  
	            long diff ;  
	            if(time1<time2) {  
	                diff = time2 - time1;  
	            } else {  
	                diff = time1 - time2;  
	            }  
	            day = diff / (24 * 60 * 60 * 1000);  
	            hour = (diff / (60 * 60 * 1000) - day * 24);  
	            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        }  
	        StringBuilder sb=new StringBuilder();
	        if(day>0){
	        	sb.append(day).append(this.languageService.text("天", language));
	        }
	        if(hour>0){
	        	sb.append(hour).append(this.languageService.text("小时", language));
	        }
	        if(min>0&&day<=0){
	        	sb.append(min).append(this.languageService.text("分", language));
	        }
	        if(sec>0&&day<=0&&hour<=0){
	        	sb.append(sec).append(this.languageService.text("秒", language));
	        }
	        if(sb.length()==0){
	        	sb.append(this.languageService.text("1秒", language));
	        }
	        //return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
	        return sb.toString();
	    }  
/*
	public void setStatusAlarmString(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			put("status_str", alarmService.handleStatus(Long.parseLong(map.get("status").toString())), map);
			put("alarm_str", alarmService.handleAlarm(Long.parseLong(map.get("alarm").toString())), map);
		}
	}
*/

	public void setStatusAlarmString(List<Map<String, Object>> list,String language) {
		//for (Map<String, Object> map : list) {
			//JT808Utils.putMap("status_str", alarmService.handleStatus(Long.parseLong(map.get("status").toString()),language), map);
			//JT808Utils.putMap("alarm_str", alarmService.handleAlarm(Long.parseLong(map.get("alarm").toString()),language), map);
		//}
	}

	
	/**
	 * 1 报警， 2 行驶 ，3 定位，4 离线，5在线不定位
	 * 
	 * @param map
	 * @return
	 */
	public int getJT808Status(Map<String, Object> map) {
		try {
			// Map<String,Object>
			// map=this.getLast0x0200Data(tid);//this.cacheService.getCar(tid);//this.jdbcTemplate.queryForMap("select
			// * from tgps_car where id=?",tid);
			if (map == null)
				return 4;
			int online = this.getOnline(map);
			if (online == 0) {
				return 4;//
			}
			if (map.get("speed") == null)
				map.put("speed", 0);
			if (map.get("alarm") == null)
				map.put("alarm", 0);
			if (map.get("status") == null)
				map.put("status", 0);

			//long alarm = Long.parseLong(map.get("alarm").toString());
			//if ((alarm & this.alarmService.getAlarmConfigValue()) > 0)
			//	return 1;
			if (map.containsKey("alarm_str") && Utils.isNotNull(map.get("alarm_str").toString())
					&& !"-".equals(map.get("alarm_str").toString()))
				return 1;

			long status = Long.parseLong(map.get("status").toString());
			//long acc = status & 0b01;
			long location = status & 0b10;
			float speed = Float.parseFloat(map.get("speed").toString());// String temp=map.get("speed").toString();
			if (speed > 0) {// acc>0&&,有时速就行驶，不判断ACC，2023-11-28
				return 2;// 行驶
			} else {
				if (location > 0)
					return 3;// 定位
				else
					return 5;// 在线不定位，原本为5，还是改成在线免得客户误会
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
	}

	public static void main(String args[]) {
		String gpstime = "20220530133955";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			long stime = sdf.parse(gpstime).getTime();
			System.out.println((System.currentTimeMillis() - stime) / 1000 / 60);
		} catch (Exception e) {
		}
	}

	public int getOnline(Map<String, Object> obj) {
		if (obj.containsKey("online")) {
			return Integer.parseInt(obj.get("online").toString());
		} else {
			return 1;
		}
	}

	//public String getAlarmConfig() {
	//	return JSON.toJSONString(this.alarmService.getAlarmConfig());
	//}

	//public String getStatusConfig() {
	//	return JSON.toJSONString(this.alarmService.getStatusConfig());
	//}

	public void pushMessageToAdmin(String content) {
		String userid, roleid = databaseConfigService.getConfigValue("lingx.super.role.code", ""),
				time = Utils.getTime();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				"select id from tlingx_user where id in(select user_id from tlingx_user_role where role_id =?)",
				roleid);
		for (Map<String, Object> map : list) {
			userid = map.get("id").toString();
			this.jdbcTemplate.update(
					"insert into tlingx_message(content,to_user_id,from_user_id,type,status,is_push,route_path,create_time,read_time) values(?,?,?,?,?,?,?,?,?)",
					content, userid, "", 1, 1, 0, "", time, time);
		}
	}

	public void pushMessageToUserId(String userid, String content) {
		String time = Utils.getTime();
		this.jdbcTemplate.update(
				"insert into tlingx_message(content,to_user_id,from_user_id,type,status,is_push,route_path,create_time,read_time) values(?,?,?,?,?,?,?,?,?)",
				content, userid, "", 1, 1, 0, "", time, time);
	}

	public void pushMessageByCarid(String tid, String content) {
		this.pushMessageByCarid(tid, content, "");
	}

	public void pushMessageByCarid(String tid, String content, String route_path) {
		String time = Utils.getTime(), userid;
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				"select id from tlingx_user where id in(select user_id from tgps_group_user where group_id in(select group_id from tgps_group_car where car_id=?))",
				tid);
		for (Map<String, Object> map : list) {
			userid = map.get("id").toString();
			this.jdbcTemplate.update(
					"insert into tlingx_message(content,to_user_id,from_user_id,type,status,is_push,route_path,create_time,read_time) values(?,?,?,?,?,?,?,?,?)",
					content, userid, "", 1, 1, 0, route_path, time, time);
		}
	}

	public String getCarnoById(String id) {
		try {
			if (TID_CARNO.containsKey(id)) {
				return TID_CARNO.get(id);
			} else {
				String carno = this.jdbcTemplate.queryForObject("select carno from tgps_car where id=?", String.class,
						id);
				TID_CARNO.put(id, carno);
				return carno;
			}
		} catch (Exception e) {
			e.printStackTrace();
			TID_CARNO.put(id, id);
			return id;
		}

	}

	public Set<String> getUserIdsById(String id) {
		Set<String> sets=new HashSet<>();
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select user_id from tgps_group_user where group_id in (select group_id from tgps_group_car where car_id=?)",id);
		for(Map<String,Object> map:list) {
			sets.add(map.get("user_id").toString());
		}
		return sets;
	}
	public String getFx(int number) {
		int nums[] = new int[] { 0, 45, 90, 135, 180, 225, 270, 315, 360 };
		String array[] = new String[] { "北", "东北", "东", "东南", "南", "西南", "西", "西北", "北" };
		int min = 1000, index = 0;
		for (int i = 0; i < nums.length; i++) {
			int t = Math.abs(number - nums[i]);
			if (t < min) {
				min = t;
				index = i;
			}
		}
		return array[index];
	}

	/**
	 * 按分页获得流水数据，可以跨轨迹表
	 * 
	 * @param tid
	 * @param stime
	 * @param etime
	 * @param rows
	 * @param page
	 * @return
	 */
	public Map<String, Object> getHistoryDataListResult(String tid, String stime, String etime, int rows, int page) {
		Map<String, Object> ret = new HashMap<String, Object>();
		int count = 0;
		List<Map<String, Object>> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		try {
			Date sdate = sdf.parse(stime);
			Date edate = sdf.parse(etime);
			String sql = "select count(*) from %s where car_id=? and gpstime>? and gpstime<?";
			while (sdf2.format(sdate).compareTo(sdf2.format(edate)) != 1) {
				String tableName = this.get0x0200TableName(sdate);
				String sql2 = String.format(sql, tableName);
				System.out.println(sql2);
				count += this.jdbcTemplate.queryForObject(sql2, Integer.class, tid, stime, etime);
				// list.addAll(this.jdbcTemplate.queryForList(sql2,car_id,stime,etime));
				sdate.setDate(sdate.getDate() + 1);
			}
			sdate = sdf.parse(stime);
			edate = sdf.parse(etime);
			int rowsTemp = 0;
			while (sdf2.format(edate).compareTo(sdf2.format(sdate)) != -1) {
				String tableName = this.get0x0200TableName(edate);
				int start = (page - 1) * rows - rowsTemp;
				if (start < 0)
					start = 0;
				sql = "select * from %s where car_id=? and gpstime>? and gpstime<? order by gpstime desc limit " + start
						+ "," + rows;

				String sql2 = String.format(sql, tableName);
				System.out.println(sql2);
				list.addAll(this.jdbcTemplate.queryForList(sql2, tid, stime, etime));
				if (list.size() >= rows)
					break;
				rowsTemp += this.jdbcTemplate.queryForObject("select count(*) from " + this.get0x0200TableName(edate)
						+ " where car_id=? and gpstime>? and gpstime<?", Integer.class, tid, stime, etime);
				edate.setDate(edate.getDate() - 1);
			}
			while (list.size() > rows) {
				list.remove(list.size() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("total", count);
		ret.put("rows", list);
		return ret;
	}



	public void batadd(Object groupId, String stid, String etid) {
		String ts = Utils.getTime();
		long stidl = Long.parseLong(stid);
		long etidl = Long.parseLong(etid);
		String tid = null;
		int len = etid.length();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date edate = new Date();
		edate.setYear(edate.getYear() + 1);
		String edateStr = sdf.format(edate);
		for (long i = stidl; i <= etidl; i++) {
			tid = Utils.leftAdd0(String.valueOf(i), len);
			this.jdbcTemplate.update(
					"insert into tgps_car(id,carno,sim,czxm,tel,stime,etime,create_time,modify_time,lat,lng) values(?,?,?,?,?,?,?,?,?,39.916385,116.396621)",
					tid, tid,  tid, "", "", ts.substring(0, 8), edateStr, ts, ts);
			this.jdbcTemplate.update("insert into tgps_group_car(group_id,car_id) values(?,?)", groupId, tid);
		}

	}
	
	public void clearAllRedis() {
		String sql="select id from tgps_car";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		for(Map<String,Object> map:list) {
			IJT808Cache.del(map.get("id").toString());
		}
	}
}
