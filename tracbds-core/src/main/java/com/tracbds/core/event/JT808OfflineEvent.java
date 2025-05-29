package com.tracbds.core.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class JT808OfflineEvent  extends ApplicationContextEvent {

	public JT808OfflineEvent(ApplicationContext source,String tid) {
		super(source);
		this.tid=tid;
	}

	private static final long serialVersionUID = 7168518950136274959L;

	private String tid;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	
	
}
