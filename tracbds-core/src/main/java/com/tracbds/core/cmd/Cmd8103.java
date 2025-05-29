package com.tracbds.core.cmd;

import com.tracbds.core.bean.Cmd8103Bean;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 终端参数设置
 * @author lingx.com
 *
 */
public class Cmd8103 extends AbstractJT808Command {
	public static final String BYTE="BYTE";
	public static final String WORD="WORD";
	public static final String DWORD="DWORD";
	public static final String STRING="STRING";
	public static final String HEXSTRING="HEXSTRING";
	public Cmd8103(String tid,Cmd8103Bean[] beans,boolean isVersion) {
		super(0x8103, tid,getBody(beans),isVersion);
	}

	public static byte[] getBody(Cmd8103Bean[] beans) {
		ByteBuf buff = Unpooled.buffer();
		try {
			buff.writeByte(beans.length);
			for(Cmd8103Bean bean:beans) {
				buff.writeInt(bean.getParamId());
				switch(bean.getType()) {
				case "BYTE":
					buff.writeByte(1);
					buff.writeByte(Integer.parseInt(bean.getValue()));
					break;
				case "WORD":
					buff.writeByte(2);
					buff.writeShort(Integer.parseInt(bean.getValue()));
					break;
				case "DWORD":
					buff.writeByte(4);
					buff.writeInt(Integer.parseInt(bean.getValue()));
					break;
				case "STRING":
					byte[] bytes=bean.getValue().getBytes("GBK");
					buff.writeByte(bytes.length);
					buff.writeBytes(bytes);
					break;
				case "HEXSTRING":
						buff.writeByte(bean.getValue().length()/2);
						buff.writeBytes(Utils.hexToBytes(bean.getValue()));
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnByteBuf(buff);
	}
}
