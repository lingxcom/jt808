package com.tracbds.core.rule.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.tracbds.core.IJT808Action;
import com.tracbds.core.rule.RuleEvent;

public class WriteLogAction  implements IJT808Action{
	private final static Logger log = LoggerFactory.getLogger(WriteLogAction.class);
	private String template="{}-{}";
	@Override
	public void init(Map<String, Object> map) {
		if(map.containsKey("template"))
		this.template=map.get("template").toString();
	}

	@Override
	public void exec(Map<String, Object> map0x0200,RuleEvent ruleEvent) {
		if(ruleEvent.getAction()!=1)return;
		log.info(template,map0x0200.get("tid").toString(),ruleEvent.getName());
	}

	public void setApplicationContext(ApplicationContext applicationContext) {}
}
