package com.tracbds.core.cmd;

import java.io.UnsupportedEncodingException;

import com.tracbds.core.bean.Cmd8401Bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 设置电话本
 * @author lingx.com
 *
 */
public class Cmd8401 extends AbstractJT808Command {

	public Cmd8401(String tid,int type,Cmd8401Bean beans[],boolean isVersion) {
		super(0x8401, tid,getBody(type,beans),isVersion);
	}

	public static byte[] getBody(int type,Cmd8401Bean beans[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeByte(beans.length);
		for(Cmd8401Bean e:beans) {
			buff.writeByte(e.getMark());
			buff.writeByte(e.getTel().length());
			String text=e.getTel();
			buff.writeBytes(text.getBytes());
			
			buff.writeByte(e.getName().length());
			text=e.getName();
			try {
				buff.writeBytes(text.getBytes("GBK"));
			} catch (UnsupportedEncodingException e1) {
				buff.writeBytes(text.getBytes());
			}
		}

		return returnByteBuf(buff);
	}
}
