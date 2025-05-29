package com.tracbds.core.cmd;

import com.lingx.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 启动实时视频指令
 * @author lingx
 *
 */
public class Cmd9101  extends AbstractJT808Command {
	
	public Cmd9101(String tid,String ip,int port,int tdh,boolean isVersion){
		super(0x9101,tid,getBody(ip,port,tdh,0,1),isVersion);
	}
	public Cmd9101(String tid,String ip,int port,int tdh,int type,int ml,boolean isVersion){
		super(0x9101,tid,getBody(ip,port,tdh,type,ml),isVersion);
	}

	
	public static byte[] getBody(String ip,int port,int tdh,int type,int ml){
		ByteBuf buff=Unpooled.buffer();
		byte array[]=ip.getBytes();
		buff.writeByte(array.length);
		buff.writeBytes(array);
		
		buff.writeShort(port);
		buff.writeShort(0);//不传UDP端口
		buff.writeByte(tdh);
		buff.writeByte(type);
		buff.writeByte(ml);
		return returnByteBuf(buff);
		
	}
}
