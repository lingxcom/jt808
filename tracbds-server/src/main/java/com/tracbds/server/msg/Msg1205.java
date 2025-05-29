package com.tracbds.server.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg1205 extends AbstrctMsgHandler implements IJT808MsgHandler{

	@Override
	public int getMsgId() {
		return 0x1205;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes)
			throws Exception {
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
		IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(0,JSON.toJSONString(list)), 10);
	}
}
