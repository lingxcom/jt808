package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 补传分包请求
 * @author lingx.com
 *
 */
public class Cmd8003 extends AbstractJT808Command {

	public Cmd8003(String tid,int msgSN,int ids[],boolean isVersion) {
		super(0x8003, tid,getBody(msgSN,ids),isVersion);
	}
	public static byte[] getBody(int msgSN,int ids[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(msgSN);
		buff.writeByte(ids.length);
		for(int id:ids) {
			buff.writeShort(id);
		}
		return returnByteBuf(buff);
	}
}
