package com.tracbds.core.rule.action;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.tracbds.core.IJT808Action;
import com.tracbds.core.cmd.Cmd8801;
import com.tracbds.core.rule.RuleEvent;
import com.tracbds.core.service.JT808CommandService;

public class TakePhostsCommandAction  implements IJT808Action{

	private ApplicationContext spring;
	private JT808CommandService jt808CommandService;
	@Override
	public void init(Map<String, Object> map) {
		this.jt808CommandService=this.spring.getBean(JT808CommandService.class);
	}	

	@Override
	public void exec(Map<String, Object> map0x0200,RuleEvent ruleEvent) {
		if(ruleEvent.getAction()!=1)return;//同个报警只处理一次
		boolean isVersion=false;
		Map<String, Object> map=map0x0200;
		String tid=map.get("tid").toString();
		if(map.containsKey("isVersion")&&"true".equals(map.get("isVersion").toString()))isVersion=true;
		Cmd8801 cmd=new Cmd8801(tid,1,isVersion);
		this.jt808CommandService.sendCommand(cmd, map.get("car_id").toString(),"0", true);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}
}
