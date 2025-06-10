package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Cmd9102 extends AbstractJT808Command {
	
	public Cmd9102(String tid,int tdh,int type,boolean isVersion){
		super(0x9102,tid,getBody(tdh,0),isVersion);
	}

	public Cmd9102(String tid,int tdh,boolean isVersion){
		this(tid,tdh,0,isVersion);
	}
	

	
	public static byte[] getBody(int tdh,int type){
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(tdh);
		buff.writeByte(type);
		buff.writeByte(0);
		buff.writeByte(1);
		return returnByteBuf(buff);
		
	}
}
