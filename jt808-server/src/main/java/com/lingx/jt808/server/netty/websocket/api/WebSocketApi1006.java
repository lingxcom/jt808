package com.lingx.jt808.server.netty.websocket.api;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.IJT808Cache;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi1006 extends AbstractApi {
	
	public WebSocketApi1006(){
		this.setCmd("1006");
	}
	@Override
	public Map<String, Object> execute(Map<String, Object> param, ChannelHandlerContext ctx) {
		String tid=param.get("tid").toString();
		String cid=ctx.channel().id().asLongText();
		if(IJT808Cache.REALTIME_TIDS.getIfPresent(cid)!=null){
			IJT808Cache.REALTIME_TIDS.getIfPresent(cid).remove(tid);
		}
		//this.redisService.publish("PUBLISH_"+tid, "unsubscribe:"+ctx.channel().id().asShortText());
		
		
		return this.getRetMap(1, "SUCCESS");
	}


}
