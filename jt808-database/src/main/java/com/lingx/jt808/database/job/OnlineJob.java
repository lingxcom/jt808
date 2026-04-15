package com.lingx.jt808.database.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.service.RedisService;

@Component
public class OnlineJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
	@Autowired
	private RedisService redisService;
	@PostConstruct
	public void init() {
		this.execute();
	}
	@Scheduled(cron="0 0/5 * * * ?")//每分钟计算
	public void execute() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		long ts=System.currentTimeMillis();
		ts=ts-(10*60*1000);
		String time=sdf.format(new Date(ts));
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tgps_car where online =? and gpstime<?","1",time);
		for(Map<String,Object> map:list) {
			this.jdbcTemplate.update("update tgps_car set online=? where id=?","0",map.get("id").toString());
			String json=redisService.get(map.get("id").toString());
			if(json==null) {
				continue;
			}
			Map<String,Object> m=JSON.parseObject(json);
			if(m==null){
				redisService.del(map.get("id").toString());
				continue;
			}
			m.put("online", "0");
			IJT808Cache.SESSIONS.invalidate(m.get("tid").toString());
			json=JSON.toJSONString(m);
			redisService.set(map.get("id").toString(), json);
			pushWebSocket(map.get("id").toString(), m);
		}
		list=this.jdbcTemplate.queryForList("select id from tgps_group order by id desc");
		int total=0,online=0;
		for(Map<String,Object> map:list) {
			total=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where group_id=?", Integer.class,map.get("id"));
			online=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where group_id=? and online='1'", Integer.class,map.get("id"));
			List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select total,online from tgps_group where fid=?",map.get("id"));
			for(Map<String,Object> map2:list2) {
				total+=Integer.parseInt(map2.get("total").toString());
				online+=Integer.parseInt(map2.get("online").toString());
			}
			this.jdbcTemplate.update("update tgps_group set total=?,online=? where id=?",total,online,map.get("id"));
		}
	}

	private void pushWebSocket(String tid,Map<String,Object> m) {
		this.redisService.publish(Constants.TOPIC_GPS_DATA, JSON.toJSONString(m));
		
	}
	
}
