package com.lingx.jt808.server.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808MsgAttached;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.bean.AttachedBean0x0200;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0201 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Autowired
	private List<IJT808MsgAttached> listAttached;
	

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
		if(this.redisService!=null)
		this.redisService.cache(key, JT808Utils.getResBeanAndToString(0), 10);
		
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
		map.put("alarm", bj);
		map.put("status", zt);
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("height", height);
		map.put("speed", speed/10f);
		map.put("direction", fx);
		map.put("gpstime", gpstime);
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
					map.put(String.format("A%02X", bean.getId()), temp.getValue(bean.getData(),tid,ctx,map,listAttachedBean0x0200));
					break;
				}
			}
		}
		
		String json=(JSON.toJSONString(map));
		if(this.redisService!=null) {
			this.redisService.push(Constants.JT808_0200_DATA, json);
		}else {
			System.out.println(json);
		}
		
		
	}

	public void setListAttached(List<IJT808MsgAttached> listAttached) {
		this.listAttached = listAttached;
	}

}
