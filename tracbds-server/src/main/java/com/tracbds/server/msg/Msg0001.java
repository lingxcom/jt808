package com.tracbds.server.msg;

import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0001 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0001;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes) throws Exception {
		if(data.readableBytes()<5)return;//内容小于5字节属于异常的回复报文
		int resMsgSn=data.readUnsignedShort();
		int resMsgId=data.readUnsignedShort();
		int ret=data.readByte();//0：成功/确认；1：失败；2：消息有误；3：不支持

		String key=JT808Utils.getResKey(tid, resMsgId, resMsgSn);
		IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(ret), 10);
	}


}
