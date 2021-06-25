package com.lingx.jt808.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.server.netty.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0704 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
	@Override
	public int getMsgId() {
		return 0x0704;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion) {
		int len = data.readUnsignedShort();
		byte type = data.readByte();
		for (int i = 0; i < len; i++) {
			int len2 = data.readUnsignedShort();
			byte[] array2=new byte[len2];
			data.readBytes(array2);
			ByteBuf data2=Unpooled.wrappedBuffer(array2);
			long bj = data2.readUnsignedInt();
			long zt = data2.readUnsignedInt();
			double lat = data2.readUnsignedInt() / 1000000f;
			double lng = data2.readUnsignedInt() / 1000000f;
			int height = data2.readUnsignedShort();
			int speed = data2.readUnsignedShort();
			int fx = data2.readUnsignedShort();
			byte[] bytes = new byte[6];
			data2.readBytes(bytes);
			String gpstime = "20" + Utils.bytesToHex(bytes);
			Map<String, Object> map = new HashMap<>();
			
			map.put("tid", tid);
			map.put("alarm", bj);
			map.put("status", zt);
			map.put("lat", lat);
			map.put("lng", lng);
			map.put("height", height);
			map.put("speed", speed / 10f);
			map.put("direction", fx);
			map.put("gpstime", gpstime);
			
			this.databaseService.handler0704(tid,map);
		}
	}

}
