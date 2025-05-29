package com.tracbds.core.cmd;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 文本信息下发 
 * @author lingx.com
 *
 */
public class Cmd8300 extends AbstractJT808Command {

	public Cmd8300(String tid,int tag,String text,boolean isVersion) {
		super(0x8300, tid,getBody(tag,text),isVersion);
	}

	public static byte[] getBody(int tag,String text) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(tag);
		try {
			buff.writeBytes(text.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			buff.writeBytes(text.getBytes());
		}
		return returnByteBuf(buff);
	}
	
}
