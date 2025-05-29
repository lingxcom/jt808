package com.tracbds.server.msg;

import org.springframework.beans.factory.annotation.Autowired;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.server.service.JT808DataService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class AbstrctMsgHandler {
	@Autowired
	protected JT808DataService databaseService;
	
	public byte[] readBytes(ByteBuf byteBuf,int length) {
		byte[] temp=new byte[length];
		byteBuf.readBytes(temp);
		return temp;
	}
	
	public static ByteBuf getBody(byte data[]) {//获取主体报文
		MyByteBuf buff = new MyByteBuf(JT808Utils.decode(data));
		buff.readByte();

		int msgId = buff.readUnsignedShort();
		int length = buff.readUnsignedShort();
		String tid = "";

		boolean isFB = (length & 0b0010000000000000) > 0;// 是否分包
		boolean isVersion = (length & 0b0100000000000000) > 0;// 是否版本标识
		if (isVersion) {
			buff.readByte();
			tid = buff.readStringBCD(10);
		} else {
			tid = buff.readStringBCD(6);
		}
		//System.out.println("TID:"+tid);
		int msgSn = buff.readUnsignedShort();// 消息流水号
		
		length = length & 0x3ff;
		ByteBuf content = null;
		//System.out.println("是否分包:"+isFB);
		if (isFB) {//分包处理
			int max = buff.readUnsignedShort();
			int ind = buff.readUnsignedShort();
			//System.out.println("分包:"+max+","+ind);
			content = buff.readByteBuf(length);
			String key = tid + "_" + msgId;
			if (ind == 1) {
				ByteBuf bigBuff=Unpooled.buffer(1024, 1024*1000);//最大1M
				bigBuff.writeBytes(content);
				IJT808Cache.FB_CACHE.put(key, bigBuff);
				return null;
			} else if (ind == max) {
				IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
				content = IJT808Cache.FB_CACHE.getIfPresent(key);
				IJT808Cache.FB_CACHE.invalidate(key);
			} else {
				IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
				return null;
			}
		} else {
			content = buff.readByteBuf(length);
		}

		byte check = buff.readByte();
		buff.readByte();

		
		return content;
	}
}	
