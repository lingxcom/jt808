package com.lingx.jt808.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.IJT808Cache;
import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.bean.RetBean;
import com.lingx.jt808.server.netty.JT808Utils;
import com.lingx.jt808.server.netty.MyByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg1205 extends AbstrctMsgHandler implements IJT808MsgHandler{

	@Override
	public int getMsgId() {
		return 0x1205;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion)
			 {
		System.out.println("收到0x9205");
		MyByteBuf mydata=new MyByteBuf(data);
		int resMsgId=0x9205;
		int resMsgSn=data.readUnsignedShort();
		long len=data.readUnsignedInt();
		List<Map<String,Object>> list=new ArrayList<>();
		for(int i=0;i<len;i++){
			Map<String,Object> map=new HashMap<>();
			map.put("tdh", mydata.readByte());
			map.put("stime", "20"+mydata.readStringBCD(6));
			map.put("etime", "20"+mydata.readStringBCD(6));
			map.put("bjzt", mydata.readStringBCD(8));

			map.put("type", mydata.readByte());
			map.put("mltype", mydata.readByte());
			map.put("zcqtype", mydata.readByte());
			map.put("length",data.readUnsignedInt());
			
			list.add(map);
		}
		
		String key=JT808Utils.getResKey(tid, resMsgId, resMsgSn);
		IJT808Cache.RESPONSES.put(key,  JT808Utils.getResBean(0,(list)));

		//this.redisService.cache(key, JT808Utils.getResBeanAndToString(0,JSON.toJSONString(list)), 10);
	}
	
	public static void main(String args[]) {
		List<Map<String,Object>> list=new ArrayList<>();
			Map<String,Object> map=new HashMap<>();
			map.put("tdh", 1);
			map.put("stime", "20");
			map.put("etime", "20");
			map.put("bjzt", "121212");

			map.put("type", 1);
			map.put("mltype", 1);
			map.put("zcqtype",1);
			map.put("length",1);
			
			list.add(map);
		RetBean aaa=JT808Utils.getResBean(0,(list));
		System.out.println(JSON.toJSONString(aaa));
	}
/*
 * StringToJsonFormat format2 = new StringToJsonFormat(cont);
		String lsh=format2.formatByte(2);
		System.out.println(tid+"_"+lsh);
		Session.command_response.put(tid+"_"+lsh, 0);//透传回复
		String len=format2.formatByte(4,Parse.getX16ToIntParse());
		//System.out.println("媒体记录数:"+len);
		int len1=Integer.parseInt(len);
		List<Map<String,Object>> list=new ArrayList<>();
		for(int i=0;i<len1;i++){
			Map<String,Object> map=new HashMap<>();
			map.put("tdh", format2.formatByte(1,Parse.getX16ToIntParse()));
			map.put("stime", "20"+format2.formatByte(6));
			map.put("etime", "20"+format2.formatByte(6));
			map.put("bjzt", format2.formatByte(8));

			map.put("type", format2.formatByte(1,Parse.getX16ToIntParse()));
			map.put("mltype", format2.formatByte(1,Parse.getX16ToIntParse()));
			map.put("zcqtype", format2.formatByte(1,Parse.getX16ToIntParse()));
			map.put("length", format2.formatByte(4,Parse.getX16ToIntParse()));
			
			list.add(map);
		}
		MEDIA_QUERY.put(tid, list);
 * 
 * */
}
