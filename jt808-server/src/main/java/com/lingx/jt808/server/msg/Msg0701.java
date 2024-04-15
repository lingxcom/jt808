package com.lingx.jt808.server.msg;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0701 extends AbstrctMsgHandler implements IJT808MsgHandler{
	

	@Override
	public int getMsgId() {
		return 0x0701;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		int length=data.readInt();
	
		byte temp[]=this.readBytes(data, length);
		
		this.redisService.push(Constants.JT808_0701_DATA, Utils.bytesToHex(temp));
	}


}
