package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0700 extends AbstrctMsgHandler implements IJT808MsgHandler {

	@Override
	public int getMsgId() {
		return 0x0700;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes)throws Exception {
		int resMsgSn = data.readUnsignedShort();
		byte cmd = data.readByte();
		byte temp[] = this.readBytes(data, data.readableBytes());
		Map<String, Object> map = new HashMap<>();
		map.put("tid", tid);
		map.put("p1", cmd);
		map.put("p2", Utils.bytesToHex(temp));
		String key=JT808Utils.getResKey(tid, 0x8700, resMsgSn);
		IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(0,JSON.toJSONString(map)), 10);
		//this.redisService.push(QueueNames.JT808_0700_DATA, JSON.toJSONString(map));
	}

}
