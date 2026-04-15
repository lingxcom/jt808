package com.lingx.jt808.database.thread;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.web.ILingxThread;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.JT808DataService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.Utils;
@Component
public class JT8080x0704Handler implements Runnable,ILingxThread {
	private boolean isRun=true;
	@Autowired
	private JT808DataService databaseService;
	@Autowired
	private RedisService redisService;
	@Override
	public String getName() {
		return "TracSeek-Database -> 0x0704处理服务[位置批量上报]";
	}
	@Override
	public void run() {
		Map<String,Object> map;
		String json;
		try {
		while(isRun) {
			json=redisService.pop(Constants.JT808_0704_DATA);
				if(Utils.isNotNull(json)) {
					map=(Map<String,Object>)JSON.parse(json);
					this.databaseService.save0200(map);
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
