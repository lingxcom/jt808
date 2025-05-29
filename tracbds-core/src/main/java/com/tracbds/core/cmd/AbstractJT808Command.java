package com.tracbds.core.cmd;

import com.tracbds.core.IJT808Command;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class AbstractJT808Command implements IJT808Command {
	private int msgId;
	private String tid;
	private byte[] content;
	private int max, ind;
	private static int msgSn=0;
	private byte[] cmdbytes;
	private boolean isVersion;
	
	public AbstractJT808Command(int msgId, String tid,boolean version) {
		this(msgId, tid, new byte[] {}, 0, 0, version);
	}
	
	public AbstractJT808Command(int msgId, String tid, byte[] content,boolean version) {
		this(msgId, tid, content, 0, 0,version);
	}

	public AbstractJT808Command(int msgId, String tid, byte[] content, int max, int ind,boolean version) {
		this.msgId = msgId;
		//this.tid = tid;
		this.content = content;
		this.max = max;
		this.ind = ind;
		if(version) {
			this.tid=Utils.leftAdd0(tid, 20);
		}else {
			this.tid=Utils.leftAdd0(tid, 12);
		}
		this.isVersion=version;
		this.msgSn++;
		if(this.msgSn>=65535)this.msgSn=0;
	}
	@Override
	public byte[] toMessageBytes() {
		if(this.cmdbytes==null) {
			this.toMessageByteBuf();
		}
		return this.cmdbytes;
	}
	@Override
	public String toMessageHexstring() {
		if(this.cmdbytes==null) {
			this.toMessageByteBuf();
		}
		return Utils.bytesToHex(this.cmdbytes);
	}
	@Override
	public ByteBuf toMessageByteBuf() {
		
		boolean isFB=max>0;//isVersion=this.tid.length()>12,
		int length=this.content.length;
		
		if(isVersion) {
			length=length|0b0100000000000000;
		}
		if(isFB) {
			length=length|0b0010000000000000;
		}
		ByteBuf bytebuf = Unpooled.buffer();
		bytebuf.writeByte(0x7e);
		bytebuf.writeShort(this.msgId);
		bytebuf.writeShort(length);
		if(isVersion) bytebuf.writeByte(0x01);
		bytebuf.writeBytes(Utils.hexToBytes(this.tid));
		bytebuf.writeShort(this.msgSn);
		if(this.max>0) {
			bytebuf.writeShort(this.max);
			bytebuf.writeShort(this.ind);
		}
		bytebuf.writeBytes(this.content);
		byte[] temp=new byte[bytebuf.readableBytes()-1];
		bytebuf.getBytes(1, temp);
		bytebuf.writeByte(this.getCode(temp));
		bytebuf.writeByte(0x7e);
		bytebuf=JT808Utils.encode(bytebuf);
		cmdbytes=new byte[bytebuf.readableBytes()];
		bytebuf.getBytes(bytebuf.readerIndex(), cmdbytes);
		return bytebuf;
	}
	public String getTid() {
		return this.tid;
	}
	public int getMsgId() {
		return this.msgId;
	}

	public int getMsgSn() {
		return this.msgSn;
	}

	public static byte getCode(byte[] bytes) {
		byte xor=0;
		for(byte b:bytes) {
			xor ^= b;
		}
		
		return xor;
	}
	
	public static byte[] returnByteBuf(ByteBuf buf) {
		byte[] data=new byte[buf.readableBytes()];
		buf.readBytes(data);
		buf.release();
		return data;
	}
}
