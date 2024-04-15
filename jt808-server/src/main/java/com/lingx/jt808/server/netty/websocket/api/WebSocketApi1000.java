package com.lingx.jt808.server.netty.websocket.api;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
/**
 * 用户授权
 * @author lingx.com
 *
 */
@Component
public class WebSocketApi1000 extends AbstractApi {
	public static final int WAIT_TIME=10;
	public WebSocketApi1000(){
		this.setCmd("1001");
	}
	
	@Override
	public Map<String, Object> execute(Map<String, Object> param,ChannelHandlerContext ctx) {
		
		return this.getRetMap(1, "");
	}
	
}
