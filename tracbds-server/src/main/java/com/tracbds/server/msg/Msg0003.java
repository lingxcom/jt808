package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0003 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0003;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		
		this.databaseService.handle0003(tid);
		ctx.close();
	}


}
