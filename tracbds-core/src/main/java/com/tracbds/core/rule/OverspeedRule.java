package com.tracbds.core.rule;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.tracbds.core.IJT808Rule;

public class OverspeedRule implements IJT808Rule{
	private int maxSpeed=120;
	@Override
	public void init(Map<String, Object> map) {
		this.maxSpeed=Integer.parseInt(map.get("maxSpeed").toString());
	}

	@Override
	public boolean match(Map<String, Object> map0x0200) {
		if(map0x0200.containsKey("speed")) {
			double speed=Double.parseDouble(map0x0200.get("speed").toString());
			if(speed>this.maxSpeed)return true;
		}
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		
	}

}
