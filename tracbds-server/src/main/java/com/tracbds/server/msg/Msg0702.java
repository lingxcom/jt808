package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0702 extends AbstrctMsgHandler implements IJT808MsgHandler{
	
//7E070240070100000000012917000922031102221102152004477E
	@Override
	public int getMsgId() {
		return 0x0702;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		MyByteBuf mbuff=new MyByteBuf(data);

		try {
			byte p1=mbuff.readByte();
			String p2="20"+mbuff.readStringBCD(6);
			if(mbuff.readableBytes()<26)return;
			int p3=mbuff.readByte();
			int len=mbuff.readByte();
			String p4=mbuff.readStringGBK(len);
			String p5=mbuff.readString(20);
			 len=mbuff.readByte();
			String p6=mbuff.readStringGBK(len);
			String p7=mbuff.readStringBCD(4);
			String p8=mbuff.readString(mbuff.readableBytes());

			Map<String, Object> map = new HashMap<>();
			map.put("tid", tid);
			map.put("p1", p1);
			map.put("p2", p2);
			map.put("p3", p3);
			map.put("p4", p4);
			map.put("p5", p5);
			map.put("p6", p6);
			map.put("p7", p7);
			map.put("p8", p8);
			//String json=JSON.toJSONString(map);
			//System.out.println(json);
			//this.redisService.push(Constants.JT808_0702_DATA,json );
			map.clear();
			map=null;
		} catch (Exception e) {
			System.out.println("0x0702:"+Utils.bytesToHex(bytes));
			e.printStackTrace();
		}
	}


}
