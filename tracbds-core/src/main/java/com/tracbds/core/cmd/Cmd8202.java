package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 临时位置跟踪控制
 * @author lingx.com
 *
 */
public class Cmd8202 extends AbstractJT808Command {
	/**
	 * 
	 * @param tid
	 * @param time1 时间间隔,单位为秒（s）
	 * @param time2 位置跟踪有效期,单位为秒（s），
	 */
	public Cmd8202(String tid,int time1,int time2,boolean isVersion) {
		super(0x8202, tid,getBody(time1,time2),isVersion);
	}

	public static byte[] getBody(int time1,int time2) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(time1);
		buff.writeInt(time2);
		return returnByteBuf(buff);
	}
}
