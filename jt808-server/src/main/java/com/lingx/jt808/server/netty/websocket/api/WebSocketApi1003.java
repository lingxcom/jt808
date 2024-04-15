package com.lingx.jt808.server.netty.websocket.api;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.jt808.server.utils.WebsocketUtils;

import io.netty.channel.ChannelHandlerContext;
/**
 * WebSocket报文推送注册
 * @author lingx.com
 *
 */
@Component
public class WebSocketApi1003 extends AbstractApi {
	public static final int WAIT_TIME=10;
	public WebSocketApi1003(){
		this.setCmd("1003");
	}
	
	@Override
	public Map<String, Object> execute(Map<String, Object> param,ChannelHandlerContext ctx) {
		WebsocketUtils.addChannel(ctx.channel());
		
		if(param.containsKey("text")&&param.get("text")!=null) {
			String text=param.get("text").toString();
			WebsocketUtils.setText(ctx.channel(),text);
		}
		Map<String,Object> ret=this.getRetMap(1, "报文推送注册成功");
		ret.put("time", Utils.getTime());
		ret.put("hexstring", "报文推送注册成功");
		
		return ret;
	}
	
}
