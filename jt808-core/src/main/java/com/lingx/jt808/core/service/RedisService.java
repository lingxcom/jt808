package com.lingx.jt808.core.service;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.Constants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Component("RedisService")
public class RedisService {

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 推进队列
	 * 
	 * @param key
	 * @param values
	 */
	public void push(String key, String... values) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, values);
			
		}catch(Exception e) {
			e.printStackTrace();;
		}finally {
			returnResource(jedis);
		}
		
	}

	/**
	 * 取出队列
	 * 
	 * @param key
	 * @return
	 */
	public String pop(String key) {
		String value = null;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			value=jedis.lpop(key);;
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return value;
	}

	public long size(String key) {
		long value = 0;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			value=jedis.llen(key);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return value;
	}
	/**
	 * 缓存一定时间
	 * 
	 * @param key
	 * @param value
	 * @param time  秒
	 */
	public void cache(String key, String value, int time) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key, time, value);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		
	}

	/**
	 * 永久存储的
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key,  value);
			//jedis.setex(key, 36000, value);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	/**
	 * 长时间存储的，1个小时是3600
	 * 
	 * @param key
	 * @param value
	 */
	public void setex(String key,int time, String value) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			//jedis.set(key,  value);
			jedis.setex(key, time, value);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	public String get(String key) {
		String value = null;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			value=jedis.get(key);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		
		return value;
	}
	public void del(String key) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	public void addSet(String key, String... value) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.sadd(key, value);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}

	public Set<String> getSet(String key) {
		Set<String> ret=null;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.smembers(key);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return ret;
	}

	public void publish(String id,String message) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.publish(id, message);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	public void subscribe(JedisPubSub sub,String... ids) {
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			jedis.subscribe(sub, ids);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	public int pubsub(String id) {
		int num=0;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			Map<String,String> map=jedis.pubsubNumSub(id);
			num=Integer.parseInt(map.get(id));
			//System.out.println(JSON.toJSONString(map));
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return num;
	}

	public String info() {
		String info=null;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			info=jedis.info();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return info;
	}
	public static void returnResource( Jedis jedis) {  
	    if (jedis != null) {  
	    	jedis.close();
	    }  
	}  
	/**
	 * 通过redis的订阅发布来做为应用间的通知，订阅名称为SystemCommandByRedis
	 * @param code 
	 * 3001 重启
	 */
	public void publishSystemCommand(int code) {
		this.publish(Constants.TOPIC_SYSTEM_COMMAND, String.valueOf(code));
	}
	/**
	 * 获取队列长度
	 * @param key
	 * @return
	 */
	public long llength(String key) {
		long info=0l;
		Jedis jedis=null;
		try {
			jedis = jedisPool.getResource();
			info=jedis.llen(key);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return info;
	}
	public static void main(String args[]) {
		System.out.println(1);
	}

}
