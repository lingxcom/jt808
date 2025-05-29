package com.tracbds.core.bean;

public class Cmd8103Bean {

	public Cmd8103Bean(int paramId,String type,String value) {
		this.paramId=paramId;
		this.type=type;
		this.value=value;
	}
	private int paramId;
	private String type;
	private String value;
	public int getParamId() {
		return paramId;
	}
	public void setParamId(int paramId) {
		this.paramId = paramId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
