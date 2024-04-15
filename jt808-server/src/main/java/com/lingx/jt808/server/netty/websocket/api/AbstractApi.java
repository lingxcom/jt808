package com.lingx.jt808.server.netty.websocket.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.server.netty.websocket.IApi;

public abstract class AbstractApi implements IApi{
	@Autowired
	protected RedisService redisService;
	private String cmd;

	public boolean matching(Map<String,Object> param){
		return this.getCmd().equals(param.get("cmd").toString());
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public Map<String,Object> getRetMap(int code,String message){
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("cmd", "2000");//通用应答
		ret.put("code", code);
		ret.put("message", message);
		return ret;
	}

	public Map<String,Object> getRetMap(int code,String message,Object data){
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("cmd", "2000");//通用应答
		ret.put("code", code);
		ret.put("message", message);
		ret.put("data", data);
		return ret;
	}
	
	public String getUserIDByToken(String token) {
		return token;
	}

	protected String getString(String key,Map<String,Object> param,String dvalue) {
		String value=dvalue;
		if(param.containsKey(key)) {
			value=param.get(key).toString();
		}
		return value;
	}
	
}
