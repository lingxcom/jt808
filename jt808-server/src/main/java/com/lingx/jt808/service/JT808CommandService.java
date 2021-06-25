package com.lingx.jt808.service;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.IJT808Cache;
import com.lingx.jt808.IJT808Command;
import com.lingx.jt808.bean.RetBean;
import com.lingx.jt808.server.netty.JT808Utils;

@Component
public class JT808CommandService {

	public RetBean sendCmd(IJT808Command cmd) {
		RetBean ret = null;
		String tid = cmd.getTid();
		if (IJT808Cache.SESSIONS.getIfPresent(tid) != null) {
			IJT808Cache.SESSIONS.getIfPresent(tid).writeAndFlush(cmd.toMessageByteBuf());
			String key = JT808Utils.getResKey(tid, cmd.getMsgId(), cmd.getMsgSn());
			int i = 0;
			while (ret == null&&i<5) {
				//System.out.println("第"+i+"次");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ret = IJT808Cache.RESPONSES.getIfPresent(key);
				i++;
				
			}
		}
		return ret;
	}
}
