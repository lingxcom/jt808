package com.lingx.jt808.core.listeners;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.event.JT808OnlineEvent;
import com.lingx.jt808.core.service.RedisService;
@Component
public class OnlineListener implements ApplicationListener<JT808OnlineEvent> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RedisService redisService;
	@Override
	public void onApplicationEvent(JT808OnlineEvent event) {
		String carid=IJT808Cache.WHITE_LIST.get(event.getTid());
		this.jdbcTemplate.update("update tgps_car set online='1' where id=?",carid);

		noticeWebsocket(carid);
	}
	private void noticeWebsocket(String carid) {
		String json=redisService.get(carid);
		if(json==null)return;
		Map<String,Object> map=JSON.parseObject(json);
		map.put("online", "1");
		json=JSON.toJSONString(map);
		this.redisService.set(carid, json);
		this.redisService.publish(Constants.TOPIC_GPS_DATA, json);
	}


}
