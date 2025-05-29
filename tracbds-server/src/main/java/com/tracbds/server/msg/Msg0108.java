package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 终端升级结果应答
 * @author lingx.com
 *
 */
@Component
public class Msg0108 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
 
	@Override
	public int getMsgId() {
		return 0x0108;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		byte type=data.readByte();
		byte ret=data.readByte();
		/*
		Map<String, Object> map = new HashMap<>();
		map.put("tid", tid);
		map.put("type", type);
		map.put("ret", ret);
		String json=(JSON.toJSONString(map));
		this.redisService.push(Constants.JT808_0108_DATA, json);
		*/
	}

}
