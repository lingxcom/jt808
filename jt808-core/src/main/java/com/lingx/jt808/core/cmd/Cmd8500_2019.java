package com.lingx.jt808.core.cmd;

import com.lingx.jt808.core.bean.Cmd8500Bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 车辆控制
 * @author lingx.com
 *
 */
public class Cmd8500_2019 extends AbstractJT808Command {

	public Cmd8500_2019(String tid,Cmd8500Bean beans[]) {
		super(0x8500, tid,getBody(beans));
	}

	public static byte[] getBody(Cmd8500Bean beans[]) {
		ByteBuf buff=Unpooled.buffer();

		buff.writeShort(beans.length);
		for(Cmd8500Bean bean:beans) {
			buff.writeShort(bean.getId());
			buff.writeByte(bean.getParam());
		}
		return returnByteBuf(buff);
	}
}
