package com.tracbds.database.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.api.common.WebSocketApi1005;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.service.JT808CommonService;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
public class OnlineJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
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
			String json=IJT808Cache.get(map.get("id").toString());
			if(json==null) {
				continue;
			}
			Map<String,Object> m=JSON.parseObject(json);
			if(m==null){
				continue;
			}
			m.put("online", "0");
			json=JSON.toJSONString(m);
			IJT808Cache.set(map.get("id").toString(), json);
			pushWebSocket(map.get("id").toString(), m);
		}
	}

	private void pushWebSocket(String tid,Map<String,Object> m) {
		Map<String,Set<String>> map=IJT808Cache.REALTIME_TIDS.asMap();
		for(String key:map.keySet()) {
			if(map.get(key).contains(tid)) {
				Channel channel=WebSocketApi1005.WEBSOCKET_SESSIONS.getIfPresent(key);

				if(channel!=null) {
					String language=channel.attr(WebSocketApi1005.channel_langugae_key).get();
					this.commonService.addJT808Info(m,language);
					channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(m)));
				}
			}
		}
	}
	
}
