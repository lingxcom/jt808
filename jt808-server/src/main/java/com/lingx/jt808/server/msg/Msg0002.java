package com.lingx.jt808.server.msg;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
@Component
public class Msg0002 extends AbstrctMsgHandler implements IJT808MsgHandler{
	@Override
	public int getMsgId() {
		return 0x0002;//终端心跳
	}
	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		String json=this.redisService.get(tid);
		if(Utils.isNotNull(json)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(json);
			if(map==null)return;
			map.put("ts", System.currentTimeMillis());
			this.redisService.set(tid, JSON.toJSONString(map));
		}
	}


}
