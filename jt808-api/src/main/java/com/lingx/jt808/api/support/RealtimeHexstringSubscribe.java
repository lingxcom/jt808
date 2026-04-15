package com.lingx.jt808.api.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lingx.jt808.api.common.WebSocketApi1007;
import com.lingx.jt808.core.utils.Utils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import redis.clients.jedis.JedisPubSub;
@Component
public class RealtimeHexstringSubscribe  extends JedisPubSub{
	 public void onMessage(String channel222, String message) {
		 Set<Channel> sets=new HashSet<>();
		for(Channel channel:WebSocketApi1007.channels) {
			if(!channel.isActive()) {
				sets.add(channel);
				continue;
			}
			String text=channel.attr(WebSocketApi1007.text_key).get().toString();
			if(Utils.isNotNull(text)) {
				if(message.contains(text))channel.writeAndFlush(new TextWebSocketFrame(message));
			}else {
				channel.writeAndFlush(new TextWebSocketFrame(message));
			}
			
		}
		
		if(sets.size()>0) {
			WebSocketApi1007.channels.removeAll(sets);
		}
	 }

}
