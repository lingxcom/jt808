package com.tracbds.core.cmd;

import java.io.UnsupportedEncodingException;

import com.tracbds.core.bean.Cmd8301Bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 事件设置 在2019已删除
 * @author lingx.com
 *
 */
public class Cmd8301 extends AbstractJT808Command {

	public Cmd8301(String tid,int type,Cmd8301Bean events[],boolean isVersion) {
		super(0x8301, tid,getBody(type,events),isVersion);
	}

	public static byte[] getBody(int type,Cmd8301Bean events[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(type);
		buff.writeByte(events.length);
		for(Cmd8301Bean e:events) {
			buff.writeByte(e.getId());
			buff.writeByte(e.getEvent().length());
			String text=e.getEvent();
			try {
				buff.writeBytes(text.getBytes("GBK"));
			} catch (UnsupportedEncodingException e1) {
				buff.writeBytes(text.getBytes());
			}
		}

		return returnByteBuf(buff);
	}
}
