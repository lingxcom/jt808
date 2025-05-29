package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 人工确认报警消息
 * @author lingx.com
 *
 */
public class Cmd8203 extends AbstractJT808Command {

	public Cmd8203(String tid,boolean isVersion) {
		this(tid,0,0xFFFFFFFF,isVersion);
	}
	public Cmd8203(String tid,int msgSn,int type,boolean isVersion) {
		super(0x8203, tid,getBody(msgSn,type),isVersion);
	}

	public static byte[] getBody(int msgSn,int type) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(msgSn);
		buff.writeInt(type);
		return returnByteBuf(buff);
	}
}
