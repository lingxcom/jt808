package com.tracbds.database.thread;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.service.JT808DataService;
import com.lingx.web.ILingxThread;
@Component
public class JT8080x0704Handler implements Runnable,ILingxThread {
	private boolean isRun=true;
	@Autowired
	private JT808DataService databaseService;
	@Override
	public String getName() {
		return "Tracbds-Database -> 0x0704处理服务[位置批量上报]";
	}
	@Override
	public void run() {
		try {
		while(isRun) {
				String json=IJT808Cache.GPS_HISTORY_DATA_QUEUE.poll();
				if(Utils.isNotNull(json)) {
					Map<String,Object> map=(Map<String,Object>)JSON.parse(json);
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
