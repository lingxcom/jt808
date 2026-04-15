package com.lingx.jt808.server.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.web.ILingxThread;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
@Component
public class SystemCommandThread implements ILingxThread{
	@Autowired
	private RedisService redisService;
	@Autowired
	private SystemCommandSubscribe systemCommandSubscribe;
	private Thread _this;
	@Override
	public String getName() {
		return "TracSeek-Server -> 系统指令监听";
	}

	@Override
	public void run() {
		this.redisService.subscribe(this.systemCommandSubscribe, Constants.TOPIC_SYSTEM_COMMAND);
	}

	@Override
	public void shutdown() {
		_this.interrupt();
	}

	@Override
	public void startup() {
		_this=new Thread(this,this.getName());
		_this.start();
		
	}

}
