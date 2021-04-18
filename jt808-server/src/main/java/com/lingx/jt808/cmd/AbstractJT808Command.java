package com.lingx.jt808.cmd;

import com.lingx.jt808.IJT808Command;
import com.lingx.jt808.server.netty.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class AbstractJT808Command implements IJT808Command {
	private int msgId;
	private String tid;
	private byte[] content;
	private int max, ind,msgSn=0;
	
	public AbstractJT808Command(int msgId, String tid) {
		this(msgId, tid, new byte[] {}, 0, 0);
	}

	public AbstractJT808Command(int msgId, String tid, byte[] content) {
		this(msgId, tid, content, 0, 0);
	}

	public AbstractJT808Command(int msgId, String tid, byte[] content, int max, int ind) {
		this.msgId = msgId;
		this.tid = tid;
		this.content = content;
		this.max = max;
		this.ind = ind;
	}
	@Override
	public byte[] toMessageBytes() {
		ByteBuf buff=this.toMessageByteBuf();
		byte[] temp=new byte[buff.readableBytes()];
		buff.readBytes(temp);
		buff.release();
		return temp;
	}
	@Override
	public String toMessageHexstring() {
		return Utils.bytesToHex(this.toMessageBytes());
	}
	@Override
	public ByteBuf toMessageByteBuf() {
		
		boolean isVersion=this.tid.length()>12,isFB=max>0;
		int length=this.content.length;
		this.msgSn++;
		if(this.msgSn>65535)this.msgSn=0;
		
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

	private byte getCode(byte[] bytes) {
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
