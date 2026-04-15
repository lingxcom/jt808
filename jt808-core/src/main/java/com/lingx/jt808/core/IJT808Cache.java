package com.lingx.jt808.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface IJT808Cache {
	
	public static final int MAX_CACHE_LENGTH=1000000;
	/**
	 * 设备号白名单，不在白名单内的设备号拒收数据
	 * tid,id
	 */
	public static Map<String, String> WHITE_LIST = Collections.synchronizedMap(new HashMap<>());
	
	/**
	 * 终端连接会话
	 */
	public static Cache<String, Channel> SESSIONS = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	/**
	 * JT808分包缓存
	 */
	public static Cache<String, ByteBuf> FB_CACHE = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(10, TimeUnit.MINUTES).build();
	/**
	 * 实时监控车辆关系维护，channelID - TID
	 */
	public static Cache<String, Set<String>> REALTIME_TIDS=CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(30, TimeUnit.MINUTES).build();
	
	/**
	 * 下发指令队列
	 */
	public static ConcurrentLinkedQueue<String> SEND_COMMAND_QUEUE=new ConcurrentLinkedQueue<String>();
	
}
