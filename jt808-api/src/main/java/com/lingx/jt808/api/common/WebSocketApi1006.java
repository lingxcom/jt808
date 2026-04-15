package com.lingx.jt808.api.common;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.IJT808Cache;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi1006 extends AbstractAuthApi {
	@Override
	public Map<String, Object> api(Map<String, Object> params,ChannelHandlerContext ctx) {
		String token=IApi.getParamString(params, "lingxtoken", "");
		String car_id=params.get("car_id").toString();
		String array[]=car_id.split(",");
		if(IJT808Cache.REALTIME_TIDS.getIfPresent(token)!=null){
			for(String id:array) {
				IJT808Cache.REALTIME_TIDS.getIfPresent(token).remove(id);
			}
			
		}
		
		return this.getRetMap(1, "SUCCESS");
	}
	@Override
	public int getApiCode() {
		return 1006;
	}
	@Override
	public String getApiName() {
		return "WebSocketApi1006";
	}

	public boolean isLog() {
		return false;
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		return null;
	}
}
