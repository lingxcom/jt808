package com.tracbds.core.rule;

import java.util.Map;

import com.tracbds.core.IJT808Action;

public class RuleActionExecuteThread implements Runnable{
	private Map<String,Object> map0x0200;
	private RuleBean ruleBean;
	private RuleEvent ruleEvent;
	public RuleActionExecuteThread(Map<String,Object> map0x0200,RuleBean ruleBean,RuleEvent ruleEvent) {
		this.map0x0200=map0x0200;
		this.ruleBean=ruleBean;
		this.ruleEvent=ruleEvent;
	}
	@Override
	public void run() {
		for(IJT808Action action:this.ruleBean.getListAction()) {
			action.exec(this.map0x0200,this.ruleEvent);
		}
	}

}
