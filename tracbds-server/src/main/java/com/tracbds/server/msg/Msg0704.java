package com.tracbds.server.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgAttached;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.event.JT808Location0704Event;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.netty.JT808Handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0704 implements IJT808MsgHandler {
	@Autowired
	private List<IJT808MsgAttached> listAttached;
	@Autowired
	private ApplicationContext spring;
	@Override
	public int getMsgId() {
		return 0x0704;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes1)throws Exception {
		int len = data.readUnsignedShort();
		byte type = data.readByte();
		List<Map<String,Object>> list=new ArrayList<>();
		if(ctx==null) {
			System.out.println("记录数："+len);
		}
		try {
			for (int i = 0; i < len&&data.readableBytes()>=20; i++) {
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

				if(!Utils.isNumber(gpstime))continue;//卫星时间不是全数字，说明此包数据有问题，直接丢了
				Map<String, Object> map = new HashMap<>();
				
				map.put("tid", tid);
				map.put("car_id", IJT808Cache.WHITE_LIST.get(tid));
				map.put("alarm", bj);
				map.put("status", zt);
				map.put("lat", lat);
				map.put("lng", lng);
				map.put("height", height);
				map.put("speed", speed / 10f);
				map.put("direction", fx);
				map.put("gpstime", gpstime);
				IJT808Cache.GPS_HISTORY_DATA_QUEUE.add(JSON.toJSONString(map));
				
				list.add(map);
			}
		} catch (Exception e) {//为了兼容部分搞错协议的设备
			list=new ArrayList<>();
			int len2 = data.readUnsignedShort();
			for (int i = 0; i < len&&data.readableBytes()>=len2; i++) {
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

				if(!Utils.isNumber(gpstime))continue;//卫星时间不是全数字，说明此包数据有问题，直接丢了
				Map<String, Object> map = new HashMap<>();
				
				map.put("tid", tid);
				map.put("car_id", IJT808Cache.WHITE_LIST.get(tid));
				map.put("alarm", bj);
				map.put("status", zt);
				map.put("lat", lat);
				map.put("lng", lng);
				map.put("height", height);
				map.put("speed", speed / 10f);
				map.put("direction", fx);
				map.put("gpstime", gpstime);
				IJT808Cache.GPS_HISTORY_DATA_QUEUE.add(JSON.toJSONString(map));
				list.add(map);
			}
			
		}
		if(spring!=null)
		spring.publishEvent(new JT808Location0704Event(spring,list,bytes1));
	}

}
