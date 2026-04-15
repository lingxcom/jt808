package com.lingx.jt808.server.msg;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.cmd.Cmd8109;
import com.lingx.jt808.core.utils.JT808Utils;

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
		ctx.writeAndFlush(new Cmd8109(JT808Utils.getTidByVersion(tid, isVersion)).toMessageByteBuf());
		
	}

}
