package com.lingx.jt808.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.support.MyByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0800 extends AbstrctMsgHandler implements IJT808MsgHandler {

	@Override
	public int getMsgId() {
		return 0x0800;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes) throws Exception{
		MyByteBuf mbuff = new MyByteBuf(data);
		int p1 = mbuff.readInt();
		byte p2 = mbuff.readByte();
		byte p3 = mbuff.readByte();
		byte p4 = mbuff.readByte();
		byte p5 = mbuff.readByte();

		Map<String, Object> map = new HashMap<>();
		map.put("tid", tid);
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		map.put("p4", p4);
		map.put("p5", p5);

		this.redisService.push(Constants.JT808_0800_DATA, JSON.toJSONString(map));

	}

}
