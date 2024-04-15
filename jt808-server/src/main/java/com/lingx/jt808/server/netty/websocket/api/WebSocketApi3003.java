package com.lingx.jt808.server.netty.websocket.api;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.service.RedisSubscribe;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi3003 extends AbstractApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public WebSocketApi3003(){
		this.setCmd("3003");
	}
	@Override
	public Map<String, Object> execute(Map<String, Object> param, ChannelHandlerContext ctx) {
		String token=param.get("token").toString();
		
		return this.getRetMap(1, "SUCCESS");
	}


}
