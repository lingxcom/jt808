package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 指令透传
 * @author lingx.com
 *
 */
public class Cmd8900 extends AbstractJT808Command {
	public Cmd8900(String tid,int type,byte data[],boolean isVersion) {
		super(0x8900, tid,getBody(type,data),isVersion);
	}
	
	public static byte[] getBody(int type,byte data[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeBytes(data);
		
		return returnByteBuf(buff);
	}
}
