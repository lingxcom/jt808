package com.lingx.jt808.api.support;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.service.LingxService;
import com.lingx.web.ILingxThread;
@Component
public class RedisApiQueueThread  implements ILingxThread{
	private final static Logger log = LoggerFactory.getLogger(RedisApiQueueThread.class);
	@Autowired
	private RedisService redisService;
	@Autowired
	private LingxService lingxService;
	private boolean isRun=true;
	private Thread _this;
	@Override
	public void run() {
		Map<String,Object> map;
		String json=null;
		try {
			while(isRun) {
				json=redisService.pop("JT808_API_QUEUE");
				if(json!=null) {
					log.info(json);
					map=JSON.parseObject(json);
					this.lingxService.api().a(map, null);
				}else {
					Thread.currentThread().sleep(1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "TracSeek-Api -> Redis队列API监听";
	}

	@Override
	public void shutdown() {
		isRun=false;
		_this.interrupt();
	}

	@Override
	public void startup() {
		_this=new Thread(this,this.getName());
		_this.start();
		
	}

}
