package com.tracbds.core.cmd;

import java.io.UnsupportedEncodingException;

import com.tracbds.core.bean.Cmd8302Bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 提问下发，在2019已删除
 * @author lingx.com
 *
 */
public class Cmd8302 extends AbstractJT808Command {

	public Cmd8302(String tid,int mark,String question,Cmd8302Bean answers[],boolean isVersion) {
		super(0x8302, tid,getBody(mark,question,answers),isVersion);
	}

	public static byte[] getBody(int type,String question,Cmd8302Bean answers[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeByte(question.length());
		String text=question;
		try {
			buff.writeBytes(text.getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			buff.writeBytes(text.getBytes());
		}
		for(Cmd8302Bean a:answers) {
			buff.writeByte(a.getId());
			buff.writeByte(a.getAnswer().length());
			 text=a.getAnswer();
			try {
				buff.writeBytes(text.getBytes("GBK"));
			} catch (UnsupportedEncodingException e1) {
				buff.writeBytes(text.getBytes());
			}
		}

		return returnByteBuf(buff);
	}
}
