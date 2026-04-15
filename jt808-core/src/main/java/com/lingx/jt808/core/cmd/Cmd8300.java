package com.lingx.jt808.core.cmd;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 文本信息下发 
 * @author lingx.com
 *
 */
public class Cmd8300 extends AbstractJT808Command {

	public Cmd8300(String tid,int tag,String text) {
		super(0x8300, tid,getBody(tag,text,"GBK"));
	}

	public Cmd8300(String tid,int tag,String text,String charset) {
		super(0x8300, tid,getBody(tag,text,charset));
	}
	public static byte[] getBody(int tag,String text,String charset) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(tag);
		try {
			if("GBK".equals(charset)) {
				buff.writeBytes(text.getBytes("GBK"));
			}else {
				buff.writeBytes(text.getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			buff.writeBytes(text.getBytes());
			System.out.println("Cmd8300 :"+e.getLocalizedMessage());
		}
		return returnByteBuf(buff);
	}
	
}
