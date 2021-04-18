package com.lingx.jt808.msg;

import javax.annotation.Resource;

import com.lingx.jt808.service.JT808DataService;

import io.netty.buffer.ByteBuf;

public abstract class AbstrctMsgHandler {
 
	@Resource
	JT808DataService databaseService;
	
	public byte[] readBytes(ByteBuf byteBuf,int length) {
		byte[] temp=new byte[length];
		byteBuf.readBytes(temp);
		return temp;
	}
}	
