package com.tracbds.core.cmd;

import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 车辆位置上报
 * @author lingx.com
 *
 */
public class Cmd0200 extends AbstractJT808Command {
	public static void main(String args[]) {
	}
	
	public Cmd0200(String tid,double lat,double lng,float speed,int height,int fx,float meilage,float oil,int bj,int zt,boolean isVersion) {
		super(0x0200, tid,getBody(tid,lat,lng,speed,height,fx,meilage,oil,bj,zt),isVersion);
	}
	
	public static byte[] getBody(String tid,double lat,double lng,float speed,int height,int fx,float meilage,float oil,int bj,int zt) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeInt(bj);
		buff.writeInt(zt);
		lat=lat*1000000f;
		buff.writeInt(new Double(lat).intValue());
		lng=lng*1000000f;
		buff.writeInt(new Double(lng).intValue());
		buff.writeShort(height);
		buff.writeShort(new Float(speed*10).intValue());
		buff.writeShort(fx);
		buff.writeBytes(Utils.hexToBytes(Utils.getTime().substring(2)));
		
		buff.writeByte(1);
		buff.writeByte(4);
		buff.writeInt(new Float(meilage*10).intValue());

		buff.writeByte(2);
		buff.writeByte(2);
		buff.writeShort(new Float(oil*10).intValue());
		return returnByteBuf(buff);
	}
	
	
	public Cmd0200(String tid,double lat,double lng,float speed,int height,int fx,float meilage,float oil,int bj,int zt,int fjId,byte bytes[],boolean isVersion) {
		super(0x0200, tid,getBody(tid,lat,lng,speed,height,fx,meilage,oil,bj,zt, fjId, bytes),isVersion);
	}
	
	public static byte[] getBody(String tid,double lat,double lng,float speed,int height,int fx,float meilage,float oil,int bj,int zt,int fjId,byte bytes[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeInt(bj);
		buff.writeInt(zt);
		lat=lat*1000000f;
		buff.writeInt(new Double(lat).intValue());
		lng=lng*1000000f;
		buff.writeInt(new Double(lng).intValue());
		buff.writeShort(height);
		buff.writeShort(new Float(speed*10).intValue());
		buff.writeShort(fx);
		buff.writeBytes(Utils.hexToBytes(Utils.getTime().substring(2)));
		
		buff.writeByte(1);
		buff.writeByte(4);
		buff.writeInt(new Float(meilage*10).intValue());

		buff.writeByte(2);
		buff.writeByte(2);
		buff.writeShort(new Float(oil*10).intValue());
		
		buff.writeByte(fjId);
		buff.writeByte(bytes.length);
		buff.writeBytes(bytes);
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
