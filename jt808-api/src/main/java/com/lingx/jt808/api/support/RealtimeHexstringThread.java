package com.lingx.jt808.api.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.web.ILingxThread;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
@Component
public class RealtimeHexstringThread  implements ILingxThread{
	@Autowired
	private RedisService redisService;
	@Autowired
	private RealtimeHexstringSubscribe realtimeHexstringSubscribe;
	private Thread _this;
	@Override
	public void run() {
		this.redisService.subscribe(this.realtimeHexstringSubscribe, Constants.TOPIC_HEXSTRING_DATA);		
	}

	@Override
	public String getName() {
		return "TracSeek-Api -> 终端实时报文监听";
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
