package com.lingx.jt808.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.utils.Utils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class WebsocketUtils {

	public static boolean isPush=false;
	private static List<Channel> channels=new ArrayList<>();
	
	public static void push(String hexstring) {
		Channel ce=null;
		for(Channel c:channels) {
			try {
				Object text=c.attr(AttributeKey.valueOf("text")).get();
				if(text==null||"".equals(text)) {
					send(hexstring,c);
				}else {
					if(hexstring.indexOf(text.toString())>-1) {
						send(hexstring,c);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ce=c;
			}
		}
		if(ce!=null) {
			removeChannel(ce);
		}
	}
	private static void send(String hexstring,Channel c) {
		String text="";
		Object obj=c.attr(AttributeKey.valueOf("text")).get();
		if(obj!=null) {
			text=obj.toString();
		}
		if(hexstring.indexOf(text)==-1)return;
		Map<String,Object> ret=new HashMap<>();
		ret.put("cmd", "2003");
		ret.put("time", Utils.getTime());
		ret.put("hexstring", hexstring);
		c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(ret)));
	}
	public static void addChannel(Channel c) {
		isPush=true;
		channels.add(c);
		
	}
	public static void removeChannel(Channel c) {
		channels.remove(c);
		if(channels.size()==0)isPush=false;
	}
	public static void setText(Channel c,String text) {
		c.attr(AttributeKey.valueOf("text")).set(text);
	}
	public static void main(String args[]) {}
}
