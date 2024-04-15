package com.lingx.jt808.server.netty.websocket;

import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public interface IApi {
	
	/**
	 * API匹配
	 * @param data
	 * @return
	 */
	public boolean matching(Map<String,Object> param);
	/**
	 * 执行API
	 * @param param
	 * @return
	 */
	public Map<String,Object> execute(Map<String,Object> param,ChannelHandlerContext ctx);
}
