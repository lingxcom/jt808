
package com.tracbds.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracbds.core.utils.Utils;
/**
 * 数据库配置服务，用于各个子项目，方便动态配置
 * @author lingx.com
 *
 */
@Component
public class DatabaseConfigService {
	public static final String LINGX_APPID="335ec1fc-1011-11e5-b7ab-74d02b6b5f61";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private  Map<String,String> configs=Collections.synchronizedMap(new HashMap<String,String>());
	
	public String getConfigValue(String key,String defaultValue) {
		if (configs.containsKey(key)) {
			return configs.get(key);
		} else if(this.jdbcTemplate.queryForObject("select count(*) from tlingx_config where config_key=?",Integer.class,key)==1) {
			String value=this.jdbcTemplate.queryForObject("select config_value from tlingx_config where config_key=?",String.class,key);
			configs.put(key, value);
			return value;
		} else {
			String time=Utils.getTime();
			if(this.jdbcTemplate.queryForObject("select count(*) from tlingx_config where config_key=?",Integer.class,key)==0){
				this.jdbcTemplate.update("insert into tlingx_config(id,name,config_key,config_value,status,create_time,modify_time,remark,app_id) values(?,?,?,?,?,?,?,?,?)"
						,UUID.randomUUID().toString(),"未设置",key,defaultValue,1,time,time,"自动生成",LINGX_APPID);
			}
			configs.put(key, defaultValue);
			return defaultValue;
		}
	}
	@Scheduled(cron="0 0/10 * * * ?")//10分钟处理一次
	public void reload() {
		for(String key:configs.keySet()) {
			String value=this.jdbcTemplate.queryForObject("select config_value from tlingx_config where config_key=?",String.class,key);
			configs.put(key, value);
		}
	}
}
