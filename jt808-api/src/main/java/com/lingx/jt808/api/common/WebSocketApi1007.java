package com.lingx.jt808.api.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
@Component
public class WebSocketApi1007 extends AbstractAuthApi {

	public static Set<Channel> channels=new HashSet<>();
	public static AttributeKey<String> text_key= AttributeKey.valueOf("text");
	@Autowired
	private RedisService redisService;
	@Override
	public Map<String, Object> api(Map<String, Object> params,ChannelHandlerContext ctx) {
		Channel channel=ctx.channel();
		channels.add(channel);
		Map<String, Object> ret=IApi.getRetMap(1, "SUCCESS");
		String text=IApi.getParamString(params, "text", "").trim();
		if(Utils.isNotNull(text))
		channel.attr(text_key).set(text);
		else channel.attr(text_key).set("");
		this.redisService.publish(Constants.TOPIC_SYSTEM_COMMAND, "3003");
		
		return ret;
	}
	@Override
	public int getApiCode() {
		return 1007;
	}
	@Override
	public String getApiName() {
		return "实时报文-WebSocketApi1007";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		return null;
	}

	public boolean isLog() {
		return false;
	}
}
