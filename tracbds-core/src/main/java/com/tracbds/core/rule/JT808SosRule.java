package com.tracbds.core.rule;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.tracbds.core.IJT808Rule;

public class JT808SosRule implements IJT808Rule{

	@Override
	public void init(Map<String, Object> map) {
		
	}

	@Override
	public boolean match(Map<String, Object> map0x0200) {
		if(map0x0200.containsKey("alarm")) {
			long alarm=Long.parseLong(map0x0200.get("alarm").toString());
			if((alarm&0b01)>0) {
				return true;//按JT808标准触发SOS报警
			}
		}
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		
	}

}
