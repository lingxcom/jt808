package com.tracbds.core.bean;

public class Cmd8401Bean {

	public Cmd8401Bean(int mark,String tel,String name) {
		this.mark=mark;
		this.tel=tel;
		this.name=name;
	}
	private int mark;
	private String tel;
	private String name;
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
