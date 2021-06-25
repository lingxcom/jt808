package com.lingx.jt808;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.jt808.bean.RetBean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IJT808Cache {

	public static final int MAX_CACHE_LENGTH=1000000;
	/**
	 * 终端连接会话
	 */
	public static Cache<String, ChannelHandlerContext> SESSIONS = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	/**
	 * 回复
	 */
	public static Cache<String, RetBean> RESPONSES = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	/**
	 * JT808分包缓存
	 */
	public static Cache<String, ByteBuf> FB_CACHE = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(10, TimeUnit.MINUTES).build();
	/**
	 * 最新定位数据
	 */
	public static Cache<String, Map<String,Object>> CACHE_0x0200 = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(10, TimeUnit.DAYS).build();
}
