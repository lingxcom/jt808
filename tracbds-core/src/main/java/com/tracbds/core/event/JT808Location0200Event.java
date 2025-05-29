package com.tracbds.core.event;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class JT808Location0200Event extends ApplicationContextEvent{

	private static final long serialVersionUID = 7226417288494604575L;
	private Map<String,Object> map;
	private byte data[];
	public JT808Location0200Event(ApplicationContext source,Map<String,Object> map,byte[] data) {
		super(source);
		this.map=map;
		this.data=data;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public byte[] getData() {
		return data;
	}

}
