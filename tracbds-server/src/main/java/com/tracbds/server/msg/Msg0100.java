package com.tracbds.server.msg;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.cmd.Cmd8100;
import com.tracbds.core.event.JT808Location0100Event;
import com.tracbds.core.support.MyByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0100 extends AbstrctMsgHandler implements IJT808MsgHandler {

	@Autowired
	private ApplicationContext spring;
 
	@Override
	public int getMsgId() {
		return 0x0100;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes12)throws Exception {
		int zzcidLen=5,zdxhLen=8,zdidLen=7;//2011
		if(data.readableBytes()>=42) {//2013
			zdxhLen=20;
		}
		if(isVersion) {//2019
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
		if(spring!=null)
			spring.publishEvent(new JT808Location0100Event(spring,map,bytes12));
		if(this.databaseService==null) {
			System.out.println(JSON.toJSONString(map));
			return;
		}
		int ret=this.databaseService.handle0100(map);
		if(ret==0) {
			String code=this.databaseService.get0100Code();
			Cmd8100 cmd=new Cmd8100(tid,msgSn,(byte)ret,code,isVersion);
			ctx.writeAndFlush(cmd.toMessageByteBuf());
			
		}else {
			Cmd8100 cmd=new Cmd8100(tid,msgSn,(byte)ret,isVersion);
			ctx.writeAndFlush(cmd.toMessageByteBuf());
			
		}
		//this.mongoTemplate.save(map, "jt808_0100_data");
		
		//this.jedisPool.getResource().push
	}

}
