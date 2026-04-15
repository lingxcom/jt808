package com.lingx.jt808.core.rule.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.jt808.core.IJT808Action;
import com.lingx.jt808.core.rule.RuleEvent;
import com.lingx.jt808.core.service.DatabaseConfigService;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.support.SendMail;
import com.lingx.utils.Utils;

public class SendEMailAction implements IJT808Action{
	private static Cache<String, Long> SEND_CACHE= CacheBuilder.newBuilder().maximumSize(1000000)
			.expireAfterWrite(5, TimeUnit.SECONDS).build();
	private final static Logger log = LoggerFactory.getLogger(SendEMailAction.class);
	private ApplicationContext spring;
	private JdbcTemplate jdbcTemplate;
	private JT808CommonService jt808CommonService;
	private DatabaseConfigService databaseConfigService;
	@Override
	public void init(Map<String, Object> map) {
		this.jdbcTemplate=this.spring.getBean(JdbcTemplate.class);
		this.jt808CommonService=this.spring.getBean(JT808CommonService.class);
		this.databaseConfigService=this.spring.getBean(DatabaseConfigService.class);
	}

	@Override
	public void exec(Map<String, Object> map0x0200, RuleEvent ruleEvent) {
		if(ruleEvent.getAction()!=1)return;//同个报警只处理一次
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				"select id,email from tlingx_user where id in(select user_id from tgps_group_user where group_id in(select group_id from tgps_car where id=?))",
				map0x0200.get("car_id"));
		
		String name=ruleEvent.getName();
		String id=map0x0200.get("car_id").toString();
		String carno=this.jt808CommonService.getCarnoById(id);
		String alarmInfo=String.format("%s -> %s", carno,name),email;
		
		String host=databaseConfigService.getConfigValue("lingx.email.stmp", ""),from=databaseConfigService.getConfigValue("lingx.email.name", ""),password=databaseConfigService.getConfigValue("lingx.email.password", "");
		if(host.equals("")||from.equals("")||password.equals("")&&!from.contains("@"))return;
		for(Map<String,Object> map:list) {
			if(map.containsKey("email")) {
				email=map.get("email").toString();
				if(Utils.isNotNull(email)&&email.contains("@")&&SEND_CACHE.getIfPresent(email)==null) {
					log.info("Send Mail {}:{}",email,alarmInfo);
					SendMail obj=new SendMail(email, alarmInfo,alarmInfo,host,from,password);
			    	obj.start();
			    	SEND_CACHE.put(email, System.currentTimeMillis());
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}

}
