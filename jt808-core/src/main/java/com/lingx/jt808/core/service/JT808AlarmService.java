package com.lingx.jt808.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;

@Component("JT808AlarmService")
public class JT808AlarmService {
	private static Cache<String, Map<String,Object>> alarmCache = CacheBuilder.newBuilder().maximumSize(1000000)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	@Autowired
	private JT808CommonService commonService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void saveAlarm(int carid,String name, Map<String,Object>map, boolean isPushMessage){
		String key=carid+name;
		if(alarmCache.getIfPresent(key)!=null)return;//太过连续上报就不处理，当作同一报警
		alarmCache.put(key, map);
		if(!map.containsKey("A01"))map.put("A01", 0);
		String address="";//this.addressService.getAddressOffsetGCJ02(map.get("lat").toString(), map.get("lng").toString());
		this.jdbcTemplate.update(
				"insert into tgps_car_alarm(car_id,name,systime,speed,lat,lng,gpstime,mileage,group_name,address) values(?,?,?,?,?,?,?,?,?,?)",
				carid, name, Utils.getTime(),  map.get("speed"),
				map.get("lat"), map.get("lng"), map.get("gpstime"), map.get("A01"),"",address);
		//this.syncGroup();
		JT808Utils.putMap("alarm_str", name, map);
		if(isPushMessage) {
			String carno=this.commonService.getCarnoById(String.valueOf(carid));
			String alarmInfo=carno+" -> "+name;
			this.commonService.pushMessageByCarid(String.valueOf(carid), alarmInfo);
			
		}
	}
	
}
