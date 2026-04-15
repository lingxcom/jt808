package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 指令透传
 * @author lingx.com
 *
 */
public class Cmd8900 extends AbstractJT808Command {
	public Cmd8900(String tid,int type,byte data[]) {
		super(0x8900, tid,getBody(type,data));
	}
	
	public static byte[] getBody(int type,byte data[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeBytes(data);
		
		return returnByteBuf(buff);
	}
}
