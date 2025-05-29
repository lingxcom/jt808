package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0805 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
	@Override
	public int getMsgId() {
		return 0x0805;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes)throws Exception {
		MyByteBuf mbuff = new MyByteBuf(data);
		int resMsgSn = mbuff.readUnsignedShort();
		int ret = mbuff.readByte();
		int len = mbuff.readShort();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<len;i++) {
			sb.append(mbuff.readUnsignedInt()).append(",");
		}
		if(sb.length()>0) {sb.deleteCharAt(sb.length()-1);}
		
		
		String key=JT808Utils.getResKey(tid, 0x8801, resMsgSn);
		IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(ret,sb.toString()), 10);
		mbuff=null;
	}

}
