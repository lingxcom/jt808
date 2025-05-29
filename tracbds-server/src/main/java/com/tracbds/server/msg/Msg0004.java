package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.cmd.Cmd8004;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0004 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0004;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		Cmd8004 cmd=new Cmd8004(tid,isVersion);
		ctx.writeAndFlush(cmd.toMessageByteBuf());
		
	}


}
