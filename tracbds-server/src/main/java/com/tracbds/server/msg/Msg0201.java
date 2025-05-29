package com.tracbds.server.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgAttached;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.bean.AttachedBean0x0200;
import com.tracbds.core.service.StatusParserService;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0201 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Autowired
	private List<IJT808MsgAttached> listAttached;

	@Autowired
	private StatusParserService statusParserService;
	@Override
	public int getMsgId() {
		return 0x0201;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes1)throws Exception {
		Map<String,Object> map=new HashMap<>();
		int resMsgSn=data.readUnsignedShort();
		//String key=tid+"_"+0x8201+"_"+resMsgSn;
		String key=JT808Utils.getResKey(tid, 0x8201, resMsgSn);
		IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(0), 10);
		String systime=Utils.getTime();
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
		map.put("car_id", IJT808Cache.WHITE_LIST.get(tid));
		map.put("type", "0x0200");
		map.put("alarm", bj);
		map.put("status", zt);
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("height", height);
		map.put("speed", speed/10f);
		map.put("direction", fx);
		map.put("gpstime", gpstime);

		map.put("systime", systime);
		map.put("ts", System.currentTimeMillis());
		map.put("isVersion", isVersion);
		if(ctx!=null)
		map.put("online", "1");
		int aid,alen;
		List<AttachedBean0x0200> listAttachedBean0x0200=new ArrayList<>();
		while(data.readableBytes()>2) {
			aid=data.readUnsignedByte();
			alen=data.readUnsignedByte();
			bytes=new byte[alen];
			if(data.readableBytes()<alen) {break;}
			data.readBytes(bytes);
			AttachedBean0x0200 bean=new AttachedBean0x0200();
			bean.setId(aid);
			bean.setLen(alen);
			bean.setData(bytes);
			listAttachedBean0x0200.add(bean);
		}
		for(AttachedBean0x0200 bean:listAttachedBean0x0200) {
			for(IJT808MsgAttached temp:this.listAttached) {
				if(temp.getAttachedId()==bean.getId()) {
					map.put(String.format("A%02X", bean.getId()), temp.getValue(bean.getData(),tid,ctx,map,listAttachedBean0x0200,isVersion));
					break;
				}
			}
		}
		statusParserService.parse(map, listAttachedBean0x0200);
		
		//String json=(JSON.toJSONString(map));
		IJT808Cache.GPS_DATA_QUEUE.add(map);
		
	}

	public void setListAttached(List<IJT808MsgAttached> listAttached) {
		this.listAttached = listAttached;
	}

}
