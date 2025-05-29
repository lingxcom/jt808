package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 通用应答
 * @author lingx.com
 *
 */
public class Cmd8001 extends AbstractJT808Command {
	public Cmd8001(String tid,int msgId,int msgSn,int ret,boolean isVersion) {
		super(0x8001, tid,getBody(msgId,msgSn,(byte)ret),isVersion);
	}
	public Cmd8001(String tid,int msgId,int msgSn,byte ret,boolean isVersion) {
		super(0x8001, tid,getBody(msgId,msgSn,ret),isVersion);
	}
	public Cmd8001(String tid,int msgId,int msgSn,boolean isVersion) {
		this(tid, msgId, msgSn, (byte)0,isVersion);
	}
	public static void main(String args[]) {
		Cmd8001 msg=new Cmd8001("012345678912",1,1,(byte)1,false);
		msg.toMessageByteBuf();
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
