package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0900 extends AbstrctMsgHandler implements IJT808MsgHandler {

	@Override
	public int getMsgId() {
		return 0x0900;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes) throws Exception{
		byte type = data.readByte();
		byte buff[]=new byte[data.readableBytes()];
		data.readBytes(buff);
		
		this.databaseService.handl0900(tid, type, buff);
		

	}

}
