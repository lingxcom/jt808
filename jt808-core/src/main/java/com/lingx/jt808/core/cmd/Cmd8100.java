package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 终端注册应答
 * @author lingx.com
 *
 */
public class Cmd8100 extends AbstractJT808Command {
	/**
	 * 
	 * @param tid
	 * @param msgSN 应答流水号
	 * @param ret 0：成功；1：车辆已被注册；2：数据库中无该车辆；
3：终端已被注册；4：数据库中无该终端 
	 * @param code 鉴权码
	 */
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
