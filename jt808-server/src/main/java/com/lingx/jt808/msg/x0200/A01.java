package com.lingx.jt808.msg.x0200;

import org.springframework.stereotype.Component;

import com.lingx.jt808.server.netty.Utils;
@Component
public class A01 implements IAttached {

	@Override
	public int getAttachedId() {
		return 0x01;
	}

	@Override
	public Object getValue(byte[] bytes,String tid) {
		
		return Utils.byteArrayToInt(bytes)/10f;
	}

}
