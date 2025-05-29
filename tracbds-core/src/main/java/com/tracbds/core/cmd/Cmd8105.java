package com.tracbds.core.cmd;

import java.io.UnsupportedEncodingException;

import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 终端控制
 * @author lingx.com
 *
 */
public class Cmd8105 extends AbstractJT808Command {

	public Cmd8105(String tid,int cmd,String param,boolean isVersion) {
		super(0x8105, tid,getBody(cmd,param),isVersion);
	}
	public static byte[] getBody(int cmd,String param) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(cmd);
		if(Utils.isNotNull(param)) {
			param=param.trim();
		try {
			buff.writeBytes(param.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			buff.writeBytes(param.getBytes());
		}}
		return returnByteBuf(buff);
	}

}
