package com.lingx.jt808.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 终端注册应答
 * @author lingx.com
 *
 */
public class Cmd8100 extends AbstractJT808Command {
	
	public Cmd8100(String tid,int msgSN,byte ret) {
		super(0x8100, tid,getBody(msgSN,ret,null));
	}
	public Cmd8100(String tid,int msgSN,byte ret,String code) {
		super(0x8100, tid,getBody(msgSN,ret,code));
	}

	public static byte[] getBody(int msgSN,byte ret,String code) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeShort(msgSN);
		buff.writeByte(ret);
		if(code!=null)
		buff.writeBytes(code.getBytes());
		return returnByteBuf(buff);
	}
	public static void main(String args[]) {
		Cmd8100 msg=new Cmd8100("012345678912",1,(byte)0,"1234567890A");
		System.out.println(msg.toMessageHexstring());
	}
}
