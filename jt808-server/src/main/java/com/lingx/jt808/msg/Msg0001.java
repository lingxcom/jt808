package com.lingx.jt808.msg;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.IJT808Cache;
import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.server.netty.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0001 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0001;
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion) {
		int resMsgSn=data.readShort();
		int resMsgId=data.readShort();
		int ret=data.readByte();//0：成功/确认；1：失败；2：消息有误；3：不支持

		String key=JT808Utils.getResKey(tid, resMsgId, resMsgSn);
		IJT808Cache.RESPONSES.put(key,  JT808Utils.getResBean(ret));
	}


}
