package com.lingx.jt808.server.msg;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.JT808Utils;

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
		this.redisService.cache(key, JT808Utils.getResBeanAndToString(ret,sb.toString()), 10);
		mbuff.release();
		mbuff=null;
	}

}
