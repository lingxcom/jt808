package com.tracbds.core.rule;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracbds.core.IJT808Rule;

public class FatigueRule implements IJT808Rule{
	/**
	 * 累计时间
	 */
	public static Cache<String,Long> TOTAL_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(24, TimeUnit.HOURS).build();
	/**
	 * 连续驾驶时间
	 */
	public static Cache<String,Long> DRIVE_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(15, TimeUnit.MINUTES).build();
	/**
	 * 停车时间
	 */
	public static Cache<String,Long> STOP_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(4, TimeUnit.HOURS).build();
	/**
	 * 上一次上报时间
	 */
	public static Cache<String,Long> LAST_TIME_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(5, TimeUnit.MINUTES).build();
	
	private ApplicationContext spring;
	
	private int total=8,driving=4,rest=20;//当天驾驶总时长(小时)，连续时长(小时)，休息时间(分钟)
	@Override
	public void init(Map<String, Object> map) {
		this.total=Integer.parseInt(map.get("total").toString());
		this.driving=Integer.parseInt(map.get("driving").toString());
		this.rest=Integer.parseInt(map.get("rest").toString());
	}

	@Override
	public boolean match(Map<String, Object> map0x0200) {
		long ts=System.currentTimeMillis();
		Map<String,Object> map=map0x0200;
		String tid=map.get("tid").toString();
		float speed=Float.parseFloat(map.get("speed").toString());
		if(LAST_TIME_CACHE.getIfPresent(tid)==null) {//太久没有上报数据，第一包没办法处理
			LAST_TIME_CACHE.put(tid, ts);
			return false;
		}
		long seconds=(ts-LAST_TIME_CACHE.getIfPresent(tid))/1000;
		if(speed>0) {
			addToCache(TOTAL_CACHE,tid,seconds);
			addToCache(DRIVE_CACHE,tid,seconds);
			
		}else {
			addToCache(STOP_CACHE,tid,seconds);
			if(STOP_CACHE.getIfPresent(tid)>rest*60) {//休息越过20分钟，不算连续驾驶
				DRIVE_CACHE.put(tid, 0l);
			}
		}
		
		if(TOTAL_CACHE.getIfPresent(tid)==null)return false;
		if(TOTAL_CACHE.getIfPresent(tid)>total*3600) {//当天开车超过8小时，触发报警
			TOTAL_CACHE.invalidate(tid);//报警后重置
			DRIVE_CACHE.invalidate(tid);
			return true;
		}
		if(DRIVE_CACHE.getIfPresent(tid)==null)return false;
		if(DRIVE_CACHE.getIfPresent(tid)>driving*3600&&(STOP_CACHE.getIfPresent(tid)==null||STOP_CACHE.getIfPresent(tid)<rest*60)) {
			//连续超过4小时，触发报警
			DRIVE_CACHE.invalidate(tid);//报警后重置
			TOTAL_CACHE.invalidate(tid);
			return true;
		}
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}

	private void addToCache(Cache<String,Long> cache,String key,long value) {
		if(cache.getIfPresent(key)==null) {
			cache.put(key, value);
		}else {
			cache.put(key, cache.getIfPresent(key)+value);
		}
	}
}
