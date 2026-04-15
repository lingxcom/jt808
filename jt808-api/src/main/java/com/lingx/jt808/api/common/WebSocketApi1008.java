package com.lingx.jt808.api.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi1008 extends AbstractAuthApi {
	@Autowired
	private RedisService redisService;
	@Override
	public Map<String, Object> api(Map<String, Object> params,ChannelHandlerContext arg0) {
		Map<String, Object> ret=IApi.getRetMap(1, "SUCCESS");
		WebSocketApi1007.channels.remove(arg0.channel());
		if(WebSocketApi1007.channels.size()==0)
		this.redisService.publish(Constants.TOPIC_SYSTEM_COMMAND, "3004");
		return ret;
	}
	@Override
	public int getApiCode() {
		return 1008;
	}
	@Override
	public String getApiName() {
		return "实时报文-WebSocketApi1008";
	}
	

	public boolean isLog() {
		return false;
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		return null;
	}
}
