package com.lingx.jt808.api.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.web.ILingxThread;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
@Component
public class RealtimeGpsDataThread implements ILingxThread{
	@Autowired
	private RedisService redisService;
	@Autowired
	private RealtimeGpsDataSubscribe realtimeGpsDataSubscribe;
	private Thread _this;
	@Override
	public String getName() {
		return "TracSeek-Api -> 实时定位数据监听";
	}

	@Override
	public void run() {
		this.redisService.subscribe(this.realtimeGpsDataSubscribe, Constants.TOPIC_GPS_DATA);
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
