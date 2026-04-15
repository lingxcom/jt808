package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 立即录音
 * @author lingx.com
 *
 */
public class Cmd8804 extends AbstractJT808Command {
	public Cmd8804(String tid,int second) {
		super(0x8804, tid,getBody(second));
	}
	public static byte[] getBody(int second) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(1);
		buff.writeShort(second);
		buff.writeByte(0);
		buff.writeByte(0);
		return returnByteBuf(buff);
	}
}
