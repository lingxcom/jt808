package com.lingx.jt808.server.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.rule.RuleManager;
import com.lingx.jt808.core.service.StatusParserService;
import com.lingx.jt808.server.netty.JT808Handler;

import redis.clients.jedis.JedisPubSub;
@Component
public class SystemCommandSubscribe extends JedisPubSub {
	@Autowired
	private JT808Handler jt808Handler;
	@Autowired
	private StatusParserService statusParserService;
	@Autowired
	private RuleManager ruleManager;
	@Override
    public void onMessage(String channel222, String message) {
		if("3001".equals(message)) {
			System.exit(-1);
		}if("3002".equals(message)) {
		}else if("3003".equals(message)) {
			JT808Handler.isHexstring=true;
		}else if("3004".equals(message)) {
			JT808Handler.isHexstring=false;
		}else if("3005".equals(message)) {
			jt808Handler.reloadWhitelist();
		}else if("3006".equals(message)) {
			statusParserService.reload();
		}else if("3007".equals(message)) {
			ruleManager.reload();
		}
	}
	
	
}
