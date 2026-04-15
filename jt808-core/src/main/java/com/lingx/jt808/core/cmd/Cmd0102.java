package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 车辆鉴权
 * @author lingx.com
 *
 */
public class Cmd0102 extends AbstractJT808Command {

	
	public Cmd0102(String tid,String code) {
		super(0x0102, tid,getBody(tid,code));
	}
	public Cmd0102(String tid,byte[] code) {
		super(0x0102, tid,getBody(tid,code));
	}

	public static byte[] getBody(String tid,byte[] code) {
		boolean isVersion=false;
		isVersion=tid.length()>12;
		ByteBuf buff=Unpooled.buffer();
		if(isVersion) {
			buff.writeByte(code.length);
			buff.writeBytes(code);
			writeString(buff,"123456",15);
			writeString(buff,"567890",20);
		}else {
			buff.writeBytes(code);
		}
		
		return returnByteBuf(buff);
	}
	public static byte[] getBody(String tid,String code) {
		boolean isVersion=false;
		isVersion=tid.length()>12;
		ByteBuf buff=Unpooled.buffer();
		if(isVersion) {
			buff.writeByte(code.getBytes().length);
			buff.writeBytes(code.getBytes());
			writeString(buff,"123456",15);
			writeString(buff,"567890",20);
		}else {
			buff.writeBytes(code.getBytes());
		}
		
		return returnByteBuf(buff);
	}
	
	public static void writeString(ByteBuf buff,String str,int length) {
		byte array[]=new byte[length];
		byte temp[]=null;
		try {
			temp=str.getBytes("GBK");
		} catch (Exception e) {
			temp=str.getBytes();
		}
		//System.out.println(Utils.bytesToHex(temp));
		for(int i=0;i<array.length&&i<temp.length;i++) {
			array[i]=temp[i];
		}
		//System.out.println(Utils.bytesToHex(array));
		buff.writeBytes(array);
	}
}
