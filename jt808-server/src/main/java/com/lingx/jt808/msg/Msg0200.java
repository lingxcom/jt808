package com.lingx.jt808.msg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.msg.x0200.IAttached;
import com.lingx.jt808.server.netty.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0200 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Resource
	private List<IAttached> listAttached;

	@Override
	public int getMsgId() {
		return 0x0200;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion) {
		
		Map<String,Object> map=new HashMap<>();
		long bj=data.readUnsignedInt();
		long zt=data.readUnsignedInt();
		double lat=data.readUnsignedInt()/1000000f;
		double lng=data.readUnsignedInt()/1000000f;
		int height = data.readUnsignedShort();
		int speed = data.readUnsignedShort();
		int fx = data.readUnsignedShort();
		byte[] bytes=new byte[6];
		data.readBytes(bytes);
		String gpstime = "20" +Utils.bytesToHex(bytes);
		map.put("tid", tid);
		map.put("type", "0x0200");
		map.put("alarm", bj);
		map.put("status", zt);
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("height", height);
		map.put("speed", speed/10f);
		map.put("direction", fx);
		map.put("gpstime", gpstime);
		map.put("systemtime", Utils.getTime());
		map.put("ts", System.currentTimeMillis());
		//在这里把网关ID传到处理器
		int aid,alen;
		while(data.readableBytes()>0) {
			aid=data.readByte();
			alen=data.readByte();
			bytes=new byte[alen];
			data.readBytes(bytes);
			for(IAttached temp:this.listAttached) {
				if(temp.getAttachedId()==aid) {
					map.put(String.format("A%02x", aid), temp.getValue(bytes,tid));
					break;
				}
			}
		}
		this.databaseService.handler0200(tid, map);
		
	}

}
