package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 电话回拨
 * @author lingx.com
 *
 */
public class Cmd8400 extends AbstractJT808Command {

	public Cmd8400(String tid,int type,String text) {
		super(0x8400, tid,getBody(type,text));
	}

	public static byte[] getBody(int type,String text) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeBytes(text.getBytes());
		return returnByteBuf(buff);
	}
}
