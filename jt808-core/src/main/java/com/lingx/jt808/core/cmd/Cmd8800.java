package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 多媒体上传应答
 * @author lingx.com
 *
 */
public class Cmd8800 extends AbstractJT808Command {
	public Cmd8800(String tid,long mediaId) {
		super(0x8800, tid,getBody(mediaId,new int[] {}));
	}
	public Cmd8800(String tid,long mediaId,int ids[]) {
		super(0x8800, tid,getBody(mediaId,ids));
	}
	public static byte[] getBody(long mediaId,int ids[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeInt((int)mediaId);
		buff.writeByte(ids.length);
		for(int id:ids) {
			buff.writeShort(id);
		}
		return returnByteBuf(buff);
	}
}
