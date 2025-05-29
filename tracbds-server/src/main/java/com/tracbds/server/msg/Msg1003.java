package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg1003 extends AbstrctMsgHandler implements IJT808MsgHandler{

	@Override
	public int getMsgId() {
		return 0x1003;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes)
			throws Exception {
		int p0=data.readUnsignedByte();
		int p1=data.readUnsignedByte();
		int p2=data.readUnsignedByte();
		int p3=data.readUnsignedByte();
		int p4=data.readUnsignedShort();
		int p6=data.readUnsignedByte();
		int p7=data.readUnsignedByte();
		int p8=data.readUnsignedByte();
		int p9=data.readUnsignedByte();
		Map<String,Object> map=new HashMap<>();
		map.put("p0", p0);
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		map.put("p4", p4);
		map.put("p6", p6);
		map.put("p7", p7);
		map.put("p8", p8);
		map.put("p9", p9);
		IJT808Cache.cache(tid+"_1003", JSON.toJSONString(map), 60);//在redis中,缓存60秒
		}
}
