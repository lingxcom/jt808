package com.lingx.gt06.server.msg;

import org.springframework.stereotype.Component;

import com.lingx.gt06.server.utils.CRCUtil;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

@Component
public class Msg0x01 implements IMsgHandler{

	@Override
	public int getMsgId() {
		return 0x01;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,
			byte[] bytes,String sn) throws Exception {
		MyByteBuf buff=new MyByteBuf(data);
		String tid1=buff.readStringBCD(8);
		ctx.channel().attr(AttributeKey.valueOf("TID1")).set(tid1);
		buff.release();
		
		String body="0501"+sn;
		String msg="7878"+body+Utils.leftAdd0(CRCUtil.getCRC16(Utils.hexToBytes(body)).toUpperCase(), 4)+"0D0A";
		ctx.channel().writeAndFlush(Utils.hexToBytes(msg));
	}

}
