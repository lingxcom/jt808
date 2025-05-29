package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 终端分包补传请求
 * @author lingx.com
 *
 */
@Component
public class Msg0005 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0005;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		int resMsgSn=data.readUnsignedShort();
		int total=data.readShort();
		int ids[]=new int[total];
		for(int i=0;i<total;i++) {
			ids[i]=data.readShort();
		}
		
	}


}
