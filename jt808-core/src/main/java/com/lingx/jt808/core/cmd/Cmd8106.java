package com.lingx.jt808.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 查询全部参数
 * @author lingx.com
 *
 */
public class Cmd8106 extends AbstractJT808Command {

	public Cmd8106(String tid,int ids[]) {
		super(0x8106, tid,getBody(ids));
	}
	public static byte[] getBody(int ids[]) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(ids.length);
		for(int id:ids) {
			buff.writeInt(id);
		}
		return returnByteBuf(buff);
	}
	
	public static void main(String args[]) {
		Cmd8106 cmd=new Cmd8106("018000032600",new int[] {1});
		System.out.println(cmd.toMessageHexstring());
	}
}
