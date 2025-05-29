package com.tracbds.core.cmd;

import java.util.Calendar;

import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 下发时间
 * 
 * @author lingx.com
 *
 */
public class Cmd8109 extends AbstractJT808Command {

	public Cmd8109(String tid,boolean isVersion) {
		super(0x8109, tid, getBody(),isVersion);
	}

	public static byte[] getBody() {
		ByteBuf buff = Unpooled.buffer();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		buff.writeShort(year);
		buff.writeByte(month);
		buff.writeByte(date);
		buff.writeByte(hour);
		buff.writeByte(minute);
		buff.writeByte(second);
		buff.writeByte(0);
		return returnByteBuf(buff);
	}
	public static void main(String args[]) {
		System.out.println(Utils.bytesToHex(getBody()));
	}
}
