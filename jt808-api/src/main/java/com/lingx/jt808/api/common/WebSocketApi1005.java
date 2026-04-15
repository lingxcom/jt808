package com.lingx.jt808.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
@Component
public class WebSocketApi1005 extends AbstractAuthApi {

	public static Cache<String, Channel> WEBSOCKET_SESSIONS = CacheBuilder.newBuilder().maximumSize(1000000)
			.expireAfterAccess(60, TimeUnit.MINUTES).build();
	public static AttributeKey<String> channel_langugae_key= AttributeKey.valueOf("Language");
	@Autowired 
	private JT808CommonService jt808CommonService;
	private Map<String,Object> getGpsData(String car_id,String cid,Map<String, Object> param,String language){
		Map<String, Object> map=jt808CommonService.getLast0x0200Data(car_id);
		if(map==null)return null;
		String tid=map.get("tid").toString();
		if(IJT808Cache.REALTIME_TIDS.getIfPresent(cid)==null) {
			Set<String> sets=ConcurrentHashMap.newKeySet();
			sets.add(car_id);
			IJT808Cache.REALTIME_TIDS.put(cid, sets);
			
		}else {
			if(param.containsKey("single"))IJT808Cache.REALTIME_TIDS.getIfPresent(cid).clear();
			IJT808Cache.REALTIME_TIDS.getIfPresent(cid).add(car_id);
		}
		
		map.put("online", IJT808Cache.SESSIONS.getIfPresent(tid)!=null?"1":"0");
		this.jt808CommonService.addJT808Info(map,language,true);
		
		return map;
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params,ChannelHandlerContext ctx) {
		Map<String, Object> ret=new HashMap<>();
		String token=IApi.getParamString(params, "lingxtoken", "");
        WEBSOCKET_SESSIONS.put(token, ctx.channel());
        if(params.containsKey("car_id")) {
        	String car_id=params.get("car_id").toString();
    		String language=IApi.getParamString(params, "language", "zh-CN");
    		String array[]=car_id.split(",");
    		List<Map<String,Object>> list=new ArrayList<>();
    		ctx.channel().attr(channel_langugae_key).set(language);
    		for(String id:array) {
    			if(Utils.isNotNull(id))
    			list.add(this.getGpsData(id, token, params,language));
    		}
    		ret.put("data", list);
    		ret.put("websocketDataType", "list");
        }else {
        	ret.put("data", new String[] {});
    		ret.put("websocketDataType", "list");
        }
		
		return ret;
	}
	@Override
	public int getApiCode() {
		return 1005;
	}
	@Override
	public String getApiName() {
		return "WebSocketApi1005";
	}
	

	public boolean isLog() {
		return false;
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		return null;
	}
}
