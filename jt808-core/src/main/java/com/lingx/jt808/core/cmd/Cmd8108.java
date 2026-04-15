package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 下发终端升级包，zzcid必须是5个字符
 * @author lingx.com
 *
 */
public class Cmd8108 extends AbstractJT808Command {

	public Cmd8108(String tid,int type,String zzcid,String version,byte[] data,int max,int ind) {
		super(0x8108, tid,getBody(type,zzcid,version,data),max,ind);
	}
	public static byte[] getBody(int type,String zzcid,String version,byte[] data) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeBytes(zzcid.getBytes());
		buff.writeByte(version.length());
		buff.writeBytes(version.getBytes());
		buff.writeInt(data.length);
		buff.writeBytes(data);
		return returnByteBuf(buff);
	}
}
