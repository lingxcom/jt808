package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 行驶记录参数设置命令
 * 数据块内容格式见 GB/T 19056 中相关内容，包含
	GB/T 19056 要求的完整数据包。
 * @author lingx.com
 *
 */
public class Cmd8701 extends AbstractJT808Command {

	public Cmd8701(String tid,byte cmd,byte[] param,boolean isVersion) {
		super(0x8701, tid,getBody(cmd,param),isVersion);
	}
	public static byte[] getBody(byte cmd,byte[] param) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(cmd);
		buff.writeBytes(param);
		
		return returnByteBuf(buff);
	}
}
