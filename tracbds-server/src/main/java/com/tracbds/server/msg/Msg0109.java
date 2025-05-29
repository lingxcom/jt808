package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.cmd.Cmd8109;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 请求同步时间
 * @author lingx.com
 *
 */
@Component
public class Msg0109 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
 
	@Override
	public int getMsgId() {
		return 0x0109;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		ctx.writeAndFlush(new Cmd8109(tid,isVersion).toMessageByteBuf());
		
	}

}
