package com.lingx.jt808.core.rule.action;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.lingx.jt808.core.IJT808Action;
import com.lingx.jt808.core.cmd.Cmd8801;
import com.lingx.jt808.core.rule.RuleEvent;
import com.lingx.jt808.core.service.JT808CommandService;

public class TakePhostsCommandAction  implements IJT808Action{

	private ApplicationContext spring;
	private JT808CommandService commandService;
	@Override
	public void init(Map<String, Object> map) {
		this.commandService=this.spring.getBean(JT808CommandService.class);
	}	

	@Override
	public void exec(Map<String, Object> map0x0200,RuleEvent ruleEvent) {
		if(ruleEvent.getAction()!=1)return;//同个报警只处理一次
		boolean isVersion=false;
		Map<String, Object> map=map0x0200;
		String tid=map.get("tid").toString();
		if(map.containsKey("isVersion")&&"true".equals(map.get("isVersion").toString()))isVersion=true;
		tid=this.commandService.getTidByVersion(tid, isVersion);
		Cmd8801 cmd=new Cmd8801(tid,1);
		this.commandService.sendCommand(cmd, map.get("car_id").toString(),"0", true);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}
}
