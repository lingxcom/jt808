package com.tracbds.core.rule.action;

import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.utils.Utils;
import com.tracbds.core.IJT808Action;
import com.tracbds.core.rule.RuleEvent;
import com.tracbds.core.service.JT808CommonService;

public class PushWebMessageAction  implements IJT808Action {

	private ApplicationContext spring;
	private JdbcTemplate jdbcTemplate;
	private JT808CommonService jt808CommonService;
	@Override
	public void init(Map<String, Object> map) {
		this.jdbcTemplate=this.spring.getBean(JdbcTemplate.class);
		this.jt808CommonService=this.spring.getBean(JT808CommonService.class);
	}

	@Override
	public void exec(Map<String, Object> map0x0200,RuleEvent ruleEvent) {
		if(ruleEvent.getAction()!=1)return;//同个报警只处理一次
		String id=map0x0200.get("car_id").toString();
		String carno=this.jt808CommonService.getCarnoById(id);
		String name=ruleEvent.getName();
		Set<String> userids=this.jt808CommonService.getUserIdsById(id);
		String alarmInfo=String.format("%s -> %s", carno,name);
		String time=Utils.getTime();
		for(String userid:userids) {
			this.jdbcTemplate.update(
					"insert into tlingx_message(content,to_user_id,from_user_id,type,status,is_push,is_audio,route_path,create_time,read_time) values(?,?,?,?,?,?,?,?,?,?)",
					alarmInfo, userid, "", 1, 1, 0,0, "", time, time);
		}		
	}
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}

}
