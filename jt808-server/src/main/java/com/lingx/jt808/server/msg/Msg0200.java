package com.lingx.jt808.server.msg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808MsgAttached;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.bean.AttachedBean0x0200;
import com.lingx.jt808.core.event.JT808Location0200Event;
import com.lingx.jt808.core.event.JT808OnlineAndLoctionEvent;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.jt808.server.service.JT808ServerConfigService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
@Component
public class Msg0200 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Autowired
	private List<IJT808MsgAttached> listAttached;
	@Autowired
	private JT808ServerConfigService configService;
	@Autowired
	private ApplicationContext spring;
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
		int fx = data.readUnsignedShort();
		byte[] bytes=new byte[6];
		data.readBytes(bytes);
		String gpstime = "20" +Utils.bytesToHex(bytes);
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
		
		map.put("tid", tid);
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
		if(ctx!=null)
		map.put("online", ctx.channel().hasAttr(AttributeKey.valueOf("TID"))?"1":"0");
		//在这里把网关ID传到处理器
		if(this.configService!=null)
		map.put(Constants.JT808_SERVER_ID, this.configService.getJt808ServerId());
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
						map.put(String.format("A%02X", bean.getId()), temp.getValue(bean.getData(),tid,ctx,map,listAttachedBean0x0200));
						break;
					}
				}
			}
		} catch (Exception e) {
		//	e.printStackTrace();
		}
		if(spring!=null)
		spring.publishEvent(new JT808Location0200Event(spring,map,bytes1));
		String json=(JSON.toJSONString(map));
		//System.out.println(json);
		if(this.redisService!=null) {
			this.redisService.push(Constants.JT808_0200_DATA, json);
		}else {
			System.out.println(json);
		}
		map.clear();
		map=null;
		json=null;
		bytes=null;
		
	}
	public void setListAttached(List<IJT808MsgAttached> listAttached) {
		this.listAttached = listAttached;
	}
	public void setConfigService(JT808ServerConfigService configService) {
		this.configService = configService;
	}
	
}
