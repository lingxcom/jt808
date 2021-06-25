package com.lingx.jt808.msg;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.cmd.Cmd8100;
import com.lingx.jt808.server.netty.MyByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0100 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
 
	@Override
	public int getMsgId() {
		return 0x0100;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion) {
		
		int zzcidLen=5,zdxhLen=20,zdidLen=7;
		if(isVersion) {
			zzcidLen=11;
			zdxhLen=30;
			zdidLen=30;
		}
		MyByteBuf buff = new MyByteBuf(data);
		int sid1 = buff.readUnsignedShort();
		int sid2 = buff.readUnsignedShort();
		String zzcid = buff.readString(zzcidLen);
		String zdxh = buff.readString(zdxhLen);
		String zdid = buff.readString(zdidLen);
		byte color = buff.readByte();

		String carno = "";
		try {
			carno = buff.readStringGBK(buff.readableBytes());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<>();
		map.put("tid", tid);
		map.put("p0", isVersion?"2019":"2013");
		map.put("p1", sid1);
		map.put("p2", sid2);
		map.put("p3", zzcid);
		map.put("p4", zdxh);
		map.put("p5", zdid);
		map.put("p6", color);
		map.put("p7", carno);
		
		int ret=this.databaseService.handle0100(tid,map);
		if(ret==0) {
			String code=this.databaseService.get0100Code(tid);
			Cmd8100 cmd=new Cmd8100(tid,msgSn,(byte)ret,code);
			ctx.writeAndFlush(cmd.toMessageByteBuf());
		}else {
			Cmd8100 cmd=new Cmd8100(tid,msgSn,(byte)ret);
			ctx.writeAndFlush(cmd.toMessageByteBuf());
		}
	}

}
