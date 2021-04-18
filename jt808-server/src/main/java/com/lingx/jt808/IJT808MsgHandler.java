package com.lingx.jt808;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IJT808MsgHandler {
	public int getMsgId();

	public void handle(ByteBuf data,String tid,int msgId,int msgSn,ChannelHandlerContext ctx,boolean isVersion);
}
