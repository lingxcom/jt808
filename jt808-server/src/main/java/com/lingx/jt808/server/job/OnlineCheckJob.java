
package com.lingx.jt808.server.job;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.Utils;

@Component
public class OnlineCheckJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	protected RedisService redisService;
	@PostConstruct
	public void init() {
		check();
	}
	@Scheduled(cron="0 0/5 * * * ?")
	public void check() {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id,online from tgps_car");
		String tid,online;
		Set<String> tids=IJT808Cache.SESSIONS.asMap().keySet();
		for(Map<String,Object> map:list) {
			tid=map.get("id").toString();
			online=map.get("online").toString();
			if("1".equals(online)&&!tids.contains(tid)) {//离线
				this.jdbcTemplate.update("update tgps_car set online='0' where id=?",tid);
				
				String json=this.redisService.get(tid);
				if(Utils.isNotNull(json)) {
					Map<String,Object> m=(Map<String,Object>)JSON.parse(json);
					if(m==null)continue;
					m.put("online", "0");
					this.redisService.set(tid,JSON.toJSONString(m));
				}
				
			}
			if("0".equals(online)&&tids.contains(tid)) {//在线
				this.jdbcTemplate.update("update tgps_car set online='1' where id=?",tid);
				
				String json=this.redisService.get(tid);
				if(Utils.isNotNull(json)) {
					Map<String,Object> m=(Map<String,Object>)JSON.parse(json);
					if(m==null)continue;
					m.put("online", "1");
					this.redisService.set(tid,JSON.toJSONString(m));
				}
			}
		}
	
	}
	
}
