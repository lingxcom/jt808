package com.tracbds.core.bean;

public class AlarmBean {
	private int bit;
	private String name;
	private boolean audio;
	private boolean push;
	public int getBit() {
		return bit;
	}
	public void setBit(int bit) {
		this.bit = bit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAudio() {
		return audio;
	}
	public void setAudio(boolean audio) {
		this.audio = audio;
	}
	public boolean isPush() {
		return push;
	}
	public void setPush(boolean push) {
		this.push = push;
	}
	
}
