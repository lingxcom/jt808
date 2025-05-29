package com.tracbds.core.bean;

public class Cmd8302Bean {

	public Cmd8302Bean(int id,String answer) {
		this.id=id;
		this.answer=answer;
	}
	private int id;
	private String answer;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
