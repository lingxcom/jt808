package com.tracbds.core.bean;

public class Cmd8500Bean {

	public Cmd8500Bean(int id,int param) {
		this.id=id;
		this.param=param;
	}
	private int id;
	private int param;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParam() {
		return param;
	}
	public void setParam(int param) {
		this.param = param;
	}
	
}
