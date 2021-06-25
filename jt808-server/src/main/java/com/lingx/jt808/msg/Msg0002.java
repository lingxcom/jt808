package com.lingx.jt808.msg;

import org.springframework.stereotype.Component;

import com.lingx.jt808.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0002 implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0002;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion) {
		
	}


}
