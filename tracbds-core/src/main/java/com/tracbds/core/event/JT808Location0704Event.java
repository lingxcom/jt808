package com.tracbds.core.event;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class JT808Location0704Event extends ApplicationContextEvent{

	private static final long serialVersionUID = 72217288494604575L;
	private List<Map<String,Object>> list;
	private byte data[];
	public JT808Location0704Event(ApplicationContext source,List<Map<String,Object>> list,byte[] data) {
		super(source);
		this.list=list;
		this.data=data;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public byte[] getData() {
		return data;
	}

}
