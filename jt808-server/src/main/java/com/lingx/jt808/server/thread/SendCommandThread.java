package com.lingx.jt808.server.thread;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.AbstractJT808ThreadService;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.IJT808ThreadService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.jt808.server.service.JT808ServerConfigService;
@Component
public class SendCommandThread extends AbstractJT808ThreadService implements Runnable,IJT808ThreadService {
	
	public static boolean IS_RUN=true;

	@Autowired
	private JT808ServerConfigService configService;
	@Autowired
	private RedisService redisService;

	@Override
	public void run() {
		long sleep=Long.parseLong(this.configService.getJt808CommandTreadSleepMS());
		while(IS_RUN) {
			try {
				String json=this.redisService.pop(this.configService.getJt808ServerId());
				if(Utils.isNotNull(json)) {
					Map<String,Object> map=(Map<String,Object>)JSON.parse(json);
					String tid=map.get("tid").toString();
					String cmd=map.get("cmd").toString();
					String msgId=map.get("msgId").toString();
					String msgSn=map.get("msgSn").toString();
					if(IJT808Cache.SESSIONS.getIfPresent(tid)==null) {
						String key=JT808Utils.getResKey(tid, Integer.parseInt(msgId), Integer.parseInt(msgSn));
						this.redisService.cache(key, JT808Utils.getResBeanAndToString(9,"终端设备不在线",""), 10);
					}else {
						IJT808Cache.SESSIONS.getIfPresent(tid).writeAndFlush(Utils.hexToBytes(cmd));
					}
				}else {
					Thread.currentThread().sleep(sleep);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "JT808-Server -> 指令下发监听服务:"+this.configService.getJt808ServerId();
	}

}
