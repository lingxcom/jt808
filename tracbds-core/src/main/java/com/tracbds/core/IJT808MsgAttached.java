package com.tracbds.core;

import java.util.List;
import java.util.Map;

import com.tracbds.core.bean.AttachedBean0x0200;

import io.netty.channel.ChannelHandlerContext;
/**
 * 0x0200附加消息处理接口
 * @author lingx
 *
 */
public interface IJT808MsgAttached {

	public int getAttachedId();
	/**
	 * 
	 * @param bytes
	 * @param tid
	 * @param ctx
	 * @param map 封装Map
	 * @param listAttachedBean0x0200 附加消息列表
	 * @return
	 */
	public Object getValue(byte[] bytes,String tid,ChannelHandlerContext ctx,Map<String,Object> map,List<AttachedBean0x0200> listAttachedBean0x0200,boolean isVersion);
}
