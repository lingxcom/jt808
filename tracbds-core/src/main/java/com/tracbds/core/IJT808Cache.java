package com.tracbds.core;

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
	 */
	public static Map<String, String> WHITE_LIST = Collections.synchronizedMap(new HashMap<>());
	/**
	 * 实时定位数据
	 */
	public static ConcurrentLinkedQueue<Map<String,Object>> GPS_DATA_QUEUE=new ConcurrentLinkedQueue<>();
	/**
	 * 历史补传数据
	 */
	public static ConcurrentLinkedQueue<String> GPS_HISTORY_DATA_QUEUE=new ConcurrentLinkedQueue<String>();
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
	 * JT808终端设备应答缓存
	 */
	public static Cache<String, String> JT808_RES_CACHE=CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterWrite(15, TimeUnit.MINUTES).build();
	/**
	 * GPS终端数据最新上报缓存
	 */
	public static Cache<String, String> GPS_DATA_CACHE=CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LENGTH)
			.expireAfterAccess(10, TimeUnit.DAYS).build();
	/**
	 * 下发指令队列
	 */
	public static ConcurrentLinkedQueue<String> SEND_COMMAND_QUEUE=new ConcurrentLinkedQueue<String>();
	public static void cache(String key,String json,int time) {
		JT808_RES_CACHE.put(key, json);
	}
	public static String cache(String key) {
		return JT808_RES_CACHE.getIfPresent(key);
	}
	public static void cacheDel(String key) {
		JT808_RES_CACHE.invalidate(key);
	}
	public static void set(String key,String value) {
		GPS_DATA_CACHE.put(key, value);
	}
	public static String get(String key) {
		return GPS_DATA_CACHE.getIfPresent(key);
	}
	public static void del(String key) {
		GPS_DATA_CACHE.invalidate(key);
	}
}
