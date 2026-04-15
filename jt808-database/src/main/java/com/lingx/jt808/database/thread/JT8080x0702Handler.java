package com.lingx.jt808.database.thread;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.JT808DataService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.web.ILingxThread;
@Component
public class JT8080x0702Handler implements Runnable,ILingxThread {
	private boolean isRun=true;
	@Autowired
	private RedisService redisService;
	@Autowired
	private JT808DataService databaseService;
	@Override
	public String getName() {
		return "TracSeek-Database -> 0x0702处理服务[驾驶员身份信息采集上报]";
	}
	@Override
	public void run() {
		try {
		while(isRun) {
				String json=this.redisService.pop(Constants.JT808_0702_DATA);
				if(Utils.isNotNull(json)) {
					Map<String,Object> map=(Map<String,Object>)JSON.parse(json);
					this.databaseService.save0702(map);
				}else {
					Thread.currentThread().sleep(1000);
				}
			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startup() {
		new Thread(this,this.getName()).start();
	}
	@Override
	public void shutdown() {
		this.isRun=false;
	}
}
