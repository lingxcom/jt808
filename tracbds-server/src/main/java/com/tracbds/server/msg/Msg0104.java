package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 查询终端参数应答
 * @author lingx.com
 *
 */
@Component
public class Msg0104 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
 
	@Override
	public int getMsgId() {
		return 0x0104;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		int resMsgSn=data.readUnsignedShort();
		int len=data.readByte();
		Map<String,String> map=new HashMap<>();
		for(int i=0;i<len;i++) {
			int id=data.readInt();
			int l=data.readByte();
			byte temp[]=new byte[l];
			data.readBytes(temp);
			map.put(String.valueOf(id), Utils.bytesToHex(temp));
		}
		
		String key=tid+"_"+resMsgSn;
		String value=JSON.toJSONString(map);
		///System.out.println("0x0104:"+key+",value:"+value);
		IJT808Cache.cache(key, value, 10);//在redis中,缓存10秒
	}

}
