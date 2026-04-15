package com.lingx.jt808.core.listeners;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.event.JT808OfflineEvent;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.service.RedisService;
@Component
public class OfflineListener implements ApplicationListener<JT808OfflineEvent> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
	@Autowired
	private RedisService redisService;
	
	@Value("#{configs['jt808.offline.listener1']}")
	private String enabled="true";
	
	@Override
	public void onApplicationEvent(JT808OfflineEvent event) {
		if(!enabled.equals("true"))return;
		String carid=IJT808Cache.WHITE_LIST.get(event.getTid());
		this.jdbcTemplate.update("update tgps_car set online='0' where id=?", carid);
		
		noticeWebsocket(carid);
	}
	private void noticeWebsocket(String carid) {
		String json=redisService.get(carid);
		Map<String,Object> map=JSON.parseObject(json);
		map.put("online", "0");
		json=JSON.toJSONString(map);
		IJT808Cache.SESSIONS.invalidate(map.get("tid").toString());
		this.redisService.set(carid, json);
		this.redisService.publish(Constants.TOPIC_GPS_DATA, json);
	}

}
