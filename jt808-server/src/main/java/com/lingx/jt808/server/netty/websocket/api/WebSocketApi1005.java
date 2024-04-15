package com.lingx.jt808.server.netty.websocket.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.service.RedisSubscribe;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi1005 extends AbstractApi {
	
	@Autowired
	private RedisService redisService;
	@Autowired 
	private JT808CommonService jt808CommonService;
	public WebSocketApi1005(){
		this.setCmd("1005");
	}
	@Override
	public Map<String, Object> execute(Map<String, Object> param, ChannelHandlerContext ctx) {
		String tid=param.get("tid").toString();
		String ldata=this.redisService.get(tid);
		//System.out.println("API1005:"+tid);
		if(tid.length()<8)return new HashMap<>();
		if(ldata==null) {
			System.out.println("API1005:"+tid);
			ldata=this.jt808CommonService.updateLast0x0200DataByRedis(tid);
		}
		String cid=ctx.channel().id().asLongText();
		if(IJT808Cache.REALTIME_TIDS.getIfPresent(cid)==null) {
			Set<String> sets=new HashSet<String>();
			sets.add(tid);
			IJT808Cache.REALTIME_TIDS.put(cid, sets);
			RedisSubscribe rs=new RedisSubscribe(ctx);
			new Thread(new Runnable() {
				@Override
				public void run() {
					//redisService.subscribe(rs, "PUBLISH_"+tid);
					redisService.subscribe(rs, "PUBLISH_"+cid);
				}
				
			}).start();
		}else {
			if(param.containsKey("single"))IJT808Cache.REALTIME_TIDS.getIfPresent(cid).clear();
			IJT808Cache.REALTIME_TIDS.getIfPresent(cid).add(tid);
		}
		//this.redisService.publish("PUBLISH_"+tid, "unsubscribe:"+ctx.channel().id().asShortText());
		
		Map<String, Object> map=(Map<String, Object>)JSON.parse(ldata);
		map.put("online", IJT808Cache.SESSIONS.getIfPresent(tid)!=null?"1":"0");
		this.jt808CommonService.addJT808Info(map);

		this.jt808CommonService.addJT808Address(map);
		
		return map;
	}


}
