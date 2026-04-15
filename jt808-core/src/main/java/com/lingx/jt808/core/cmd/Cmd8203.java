package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 人工确认报警消息
 * @author lingx.com
 *
 */
public class Cmd8203 extends AbstractJT808Command {

	public Cmd8203(String tid) {
		this(tid,0,0xFFFFFFFF);
	}
	public Cmd8203(String tid,int msgSn,int type) {
		super(0x8203, tid,getBody(msgSn,type));
	}

	public static byte[] getBody(int msgSn,int type) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(msgSn);
		buff.writeInt(type);
		return returnByteBuf(buff);
	}
}
