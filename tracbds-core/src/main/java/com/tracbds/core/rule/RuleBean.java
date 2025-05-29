package com.tracbds.core.rule;

import java.util.List;
import java.util.Set;

import com.tracbds.core.IJT808Action;
import com.tracbds.core.IJT808Rule;

public class RuleBean {
	private String id;
	private String name;
	private int duration;//持续时长阀值，达到这个时间才触发
	private String stime,etime;//0000，2359
	private Set<String> carIdSet;
	private IJT808Rule ruleInstance;
	private List<IJT808Action> listAction;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public Set<String> getCarIdSet() {
		return carIdSet;
	}
	public void setCarIdSet(Set<String> carIdSet) {
		this.carIdSet = carIdSet;
	}
	public IJT808Rule getRuleInstance() {
		return ruleInstance;
	}
	public void setRuleInstance(IJT808Rule ruleInstance) {
		this.ruleInstance = ruleInstance;
	}
	public List<IJT808Action> getListAction() {
		return listAction;
	}
	public void setListAction(List<IJT808Action> listAction) {
		this.listAction = listAction;
	}
}
