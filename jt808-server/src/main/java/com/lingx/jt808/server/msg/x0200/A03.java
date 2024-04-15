package com.lingx.jt808.server.msg.x0200;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.jt808.core.IJT808MsgAttached;
import com.lingx.jt808.core.bean.AttachedBean0x0200;
import com.lingx.jt808.core.utils.Utils;

import io.netty.channel.ChannelHandlerContext;
@Component
public class A03 implements IJT808MsgAttached {

	@Override
	public int getAttachedId() {
		return 0x03;
	}

	@Override
	public Object getValue(byte[] bytes,String tid,ChannelHandlerContext ctx,Map<String,Object> map,List<AttachedBean0x0200> listAttachedBean0x0200) {
		
		return Utils.byteArrayToInt(bytes)/10f;
	}

}
