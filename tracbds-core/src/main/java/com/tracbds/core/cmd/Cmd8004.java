package com.tracbds.core.cmd;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 查询服务器时间应答
 * @author lingx.com
 *
 */
public class Cmd8004 extends AbstractJT808Command {

	public Cmd8004(String tid,boolean isVersion) {
		super(0x8004, tid,getBody(Utils.getTime().substring(2)),isVersion);
	}
	public Cmd8004(String tid,String time,boolean isVersion) {
		super(0x8004, tid,getBody(time),isVersion);
	}
	public static byte[] getBody(String time) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeBytes(Utils.hexToBytes(time));
		
		return returnByteBuf(buff);
	}
}
