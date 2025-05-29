package com.tracbds.core.rule.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracbds.core.IJT808Action;
import com.tracbds.core.rule.RuleEvent;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;

public class WriteDatabaseAction implements IJT808Action {
	private static Cache<String, Map<String,Object>> alarmCache = CacheBuilder.newBuilder().maximumSize(1000000)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	private ApplicationContext spring;
	private JdbcTemplate jdbcTemplate;
	@Override
	public void init(Map<String, Object> map) {
		this.jdbcTemplate=spring.getBean(JdbcTemplate.class);
	}

	@Override
	public void exec(Map<String, Object> map0x0200,RuleEvent ruleEvent) {
		Map<String, Object> map=map0x0200;
		String name=ruleEvent.getName();
		String key=String.format("%s_%s", map.get("car_id").toString(),name);
		JT808Utils.putMap("alarm_str",name,map);
		if(ruleEvent.getAction()==1) {
			this.jdbcTemplate.update(
					"insert into tgps_car_alarm(car_id,name,systime,speed,lat,lng,gpstime,mileage,group_name,address,remark) values(?,?,?,?,?,?,?,?,?,?,?)",
					map.get("car_id"),  name, Utils.getTime(),  map.get("speed"),
					map.get("lat"), map.get("lng"), map.get("gpstime"), map.get("A01"),"","","");
			
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tgps_car_alarm where car_id=? and name=? and gpstime=? order by id desc limit 1",
					map.get("car_id").toString(),name,map.get("gpstime"));
			if(list.size()>0)map.put("DATABASES_ID", list.get(0).get("id"));
			alarmCache.put(key, map);
		}else {
			Map<String,Object> smap=alarmCache.getIfPresent(key);
			if(smap==null)return;
			if(!smap.containsKey("DATABASES_ID"))return;
			Object id=smap.get("DATABASES_ID");
			
			this.jdbcTemplate.update("update tgps_car_alarm set time=?,bjlc=?,end_lat=?,end_lng=?,end_speed=?,end_gpstime=?,end_mileage=? where id=?"
					,ruleEvent.getDuration(),(Float.parseFloat( map.get("A01").toString())-Float.parseFloat(smap.get("A01").toString()))
					,map.get("lat"), map.get("lng"),map.get("speed"), map.get("gpstime"), map.get("A01"),id);
			
		}
		
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}
}
