package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.alibaba.druid.Constants;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.utils.Utils;

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
		
		//this.redisService.push(Constants.JT808_0701_DATA, Utils.bytesToHex(temp));
	}


}
