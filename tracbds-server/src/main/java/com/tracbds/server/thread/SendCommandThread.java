package com.tracbds.server.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;
import com.lingx.web.ILingxThread;
@Component
public class SendCommandThread  implements Runnable,ILingxThread {

	private final static Logger log = LoggerFactory.getLogger(SendCommandThread.class);
	private boolean isRun=true;
	@Override
	public void run() {
		String json=null;
		try {
		while(isRun) {
				json=IJT808Cache.SEND_COMMAND_QUEUE.poll();
				if(Utils.isNotNull(json)) {
					log.info(json);
					Map<String,Object> map=(Map<String,Object>)JSON.parse(json);
					String tid=map.get("tid").toString();
					while (tid.charAt(0) == '0') {
						tid = tid.substring(1);
					}
					String cmd=map.get("cmd").toString();
					String msgId=map.get("msgId").toString();
					String msgSn=map.get("msgSn").toString();
					if(IJT808Cache.SESSIONS.getIfPresent(tid)==null) {
						String key=JT808Utils.getResKey(tid, Integer.parseInt(msgId), Integer.parseInt(msgSn));
						IJT808Cache.cache(key, JT808Utils.getResBeanAndToString(9,"终端设备不在线",""), 10);
					}else {
						IJT808Cache.SESSIONS.getIfPresent(tid).writeAndFlush(Utils.hexToBytes(cmd));
						log.info(cmd);
						
					}
					map.clear();
					map=null;
				}else {
					Thread.currentThread().sleep(100);
				}
			
			}
		} catch (Exception e) {
			log.error("指令下发线程退出。。。");
			log.error(json);
			System.out.println(json);
			e.printStackTrace();
		}
	}
	@Override
	public String getName() {
		return "Tracbds-Server -> 指令下发线程";
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
