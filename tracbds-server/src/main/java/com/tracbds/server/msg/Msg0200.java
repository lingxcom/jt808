package com.tracbds.server.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgAttached;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.bean.AttachedBean0x0200;
import com.tracbds.core.event.JT808Location0200Event;
import com.tracbds.core.event.JT808OnlineAndLoctionEvent;
import com.tracbds.core.service.StatusParserService;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
@Component
public class Msg0200 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Autowired
	private List<IJT808MsgAttached> listAttached;
	@Autowired
	private ApplicationContext spring;
	@Autowired
	private StatusParserService statusParserService;
	@Override
	public int getMsgId() {
		return 0x0200;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes1)throws Exception {
		String systime=Utils.getTime();
		Map<String,Object> map=new HashMap<>();
		long bj=data.readUnsignedInt();
		long zt=data.readUnsignedInt();
		double lat=data.readUnsignedInt()/1000000f;
		double lng=data.readUnsignedInt()/1000000f;
		int height = data.readUnsignedShort();
		float speed = data.readUnsignedShort();
		int fx = data.readUnsignedShort()%360;
		byte[] bytes=new byte[6];
		data.readBytes(bytes);
		String gpstime = "20" +Utils.bytesToHex(bytes);
		//System.out.println(gpstime);
		if(lat>999||lng>999||lat<-999||lng<-999)return;//无效经纬度
		if(!gpstime.startsWith(systime.substring(0,4)))return;//北京陈工设备在不定位时，时间错误
		if(!Utils.isNumber(gpstime))return;//卫星时间不是全数字，说明此包数据有问题，直接丢了
		if(ctx!=null){//if(msgId==0x0200) 
			//第一次上线且定位,触发下事件
			Object obj=ctx.channel().attr(AttributeKey.valueOf("0x0200")).get();
			if(obj==null&&(zt&0b1000)>0) {
				ctx.channel().attr(AttributeKey.valueOf("0x0200")).set("JT808OnlineEvent");
				JT808OnlineAndLoctionEvent event=new JT808OnlineAndLoctionEvent(spring,tid,ctx.channel());
				spring.publishEvent(event);
			}
		}
		speed=speed/10f;
		if(speed<1)speed=0;
		if(speed>200)return;//时速大于200，设备上报有问题，忽略该条报文
		//System.out.println("0B"+Long.toBinaryString(zt));
		if((zt&0b0100)>0){
			lat=-lat;
		}
		if((zt&0b01000)>0){
			lng=-lng;
		}
		map.put("tid", tid);
		map.put("car_id", IJT808Cache.WHITE_LIST.get(tid));
		map.put("type", "0x0200");
		map.put("alarm", bj);
		map.put("status", zt);
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("height", height);
		map.put("speed", speed);
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
		try {
			for(AttachedBean0x0200 bean:listAttachedBean0x0200) {
				for(IJT808MsgAttached temp:this.listAttached) {
					if(temp.getAttachedId()==bean.getId()) {
						map.put(String.format("A%02X", bean.getId()), temp.getValue(bean.getData(),tid,ctx,map,listAttachedBean0x0200,isVersion));
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		
		statusParserService.parse(map, listAttachedBean0x0200);
		
		if(spring!=null)
		spring.publishEvent(new JT808Location0200Event(spring,map,bytes1));
		//String json=(JSON.toJSONString(map));
		
		IJT808Cache.GPS_DATA_QUEUE.add(map);
		
		map=null;
		//json=null;
		bytes=null;
		
	}
	public void setListAttached(List<IJT808MsgAttached> listAttached) {
		this.listAttached = listAttached;
	}
	
}
