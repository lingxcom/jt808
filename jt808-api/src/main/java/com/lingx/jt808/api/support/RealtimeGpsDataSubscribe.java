package com.lingx.jt808.api.support;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.api.common.WebSocketApi1005;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.JT808CommonService;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import redis.clients.jedis.JedisPubSub;
@Component
public class RealtimeGpsDataSubscribe extends JedisPubSub {

	@Autowired
	private JT808CommonService commonService;
	@Override
    public void onMessage(String channel222, String message) {
		Map<String,Object> m=JSON.parseObject(message);
		String carid=m.get("car_id").toString();
		Map<String,Set<String>> map=IJT808Cache.REALTIME_TIDS.asMap();
		for(String key:map.keySet()) {
			if(map.get(key).contains(carid)) {
				Channel channel=WebSocketApi1005.WEBSOCKET_SESSIONS.getIfPresent(key);

				if(channel!=null) {
					String language=channel.attr(WebSocketApi1005.channel_langugae_key).get();
					this.commonService.addJT808Info(m,language,true);
					channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(m)));
				}
			}
		}
	}
	
	
}
