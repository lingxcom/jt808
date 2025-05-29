package com.tracbds.core.bean;

public class Cmd8301Bean {

	public Cmd8301Bean(int id,String event) {
		this.id=id;
		this.event=event;
	}
	private int id;
	private String event;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	
}
