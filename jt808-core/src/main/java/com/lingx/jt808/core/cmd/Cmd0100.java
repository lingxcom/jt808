package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 车辆注册
 * @author lingx.com
 *
 */
public class Cmd0100 extends AbstractJT808Command {
	
	public Cmd0100(String tid,int id1,int id2,String zzcid,String zdxh,String zdid,int color,String carno) {
		super(0x0100, tid,getBody(tid,id1,id2,zzcid,zdxh,zdid,color,carno));
	}
	
	public static byte[] getBody(String tid,int id1,int id2,String zzcid,String zdxh,String zdid,int color,String carno) {
		boolean isVersion=false;
		int zzcidLen=5,zdxhLen=20,zdidLen=7;
		isVersion=tid.length()>12;
		if(isVersion) {
			zzcidLen=11;
			zdxhLen=30;
			zdidLen=30;
		}
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(id1);
		buff.writeShort(id2);
		writeString(buff,zzcid,zzcidLen);
		writeString(buff,zdxh,zdxhLen);
		writeString(buff,zdid,zdidLen);
		buff.writeByte(color);
		try {
			buff.writeBytes(carno.getBytes("GBK"));
		} catch (Exception e) {
			buff.writeBytes(carno.getBytes());
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
