package com.lingx.jt808.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 通用应答 1
 * @author lingx.com
 *
 */
public class Cmd8001 extends AbstractJT808Command {
	public Cmd8001(String tid,int msgId,int msgSn,int ret) {
		super(0x8001, tid,getBody(msgId,msgSn,(byte)ret));
	}
	public Cmd8001(String tid,int msgId,int msgSn,byte ret) {
		super(0x8001, tid,getBody(msgId,msgSn,ret));
	}
	public Cmd8001(String tid,int msgId,int msgSn) {
		this(tid, msgId, msgSn, (byte)0);
	}
	public static void main(String args[]) {
		Cmd8001 msg=new Cmd8001("012345678912",1,1,(byte)1);
		System.out.println(msg.toMessageHexstring());
	}
	public static byte[] getBody(int msgId,int msgSn,byte ret) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(msgSn);
		buff.writeShort(msgId);
		buff.writeByte(ret);
		return returnByteBuf(buff);
	}
}
