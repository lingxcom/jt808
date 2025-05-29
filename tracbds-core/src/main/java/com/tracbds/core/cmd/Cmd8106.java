package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 查询全部参数
 * @author lingx.com
 *
 */
public class Cmd8106 extends AbstractJT808Command {

	public Cmd8106(String tid,int ids[],boolean isVersion) {
		super(0x8106, tid,getBody(ids),isVersion);
	}
	public static byte[] getBody(int ids[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(ids.length);
		for(int id:ids) {
			buff.writeInt(id);
		}
		return returnByteBuf(buff);
	}
	
}
