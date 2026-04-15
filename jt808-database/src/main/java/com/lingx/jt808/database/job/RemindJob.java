package com.lingx.jt808.database.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.service.JT808CommonService;

@Component
public class RemindJob {

	@Value("#{configs['jt808.job.remind']}")
	private String enabled="true";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService jt808CommonService;
	@PostConstruct
	public void init(){
		//System.out.println("====MileageJob====");
		//this.enabled="true";
		//this.execute();
	}
	@Scheduled(cron="10 0 0 * * ?")//每天计算1次
	public void execute() {
		if(!"true".equals(enabled))return;
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
		String date10,date20,date30;
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 10);
		date10=df.format(c.getTime());

		c.add(Calendar.DATE, 10);
		date20=df.format(c.getTime());

		c.add(Calendar.DATE, 10);
		date30=df.format(c.getTime());
		
		String array[]=new String[] {date10,date20,date30};
		List<Map<String,Object>> list;
		int day=0;
		for(String temp:array) {
			day=day+10;
			list=this.jdbcTemplate.queryForList("select * from toa_insure where edate<? and is_remind='0' and day=?",temp,day);
			remindInsure(list);
			
			list=this.jdbcTemplate.queryForList("select * from toa_sim where edate<? and is_remind='0' and day=?",temp,day);
			remindSim(list);
			
			list=this.jdbcTemplate.queryForList("select * from toa_mot where edate<? and is_remind='0' and day=?",temp,day);
			remindMot(list);
			list=this.jdbcTemplate.queryForList("select id,carno,etime from tgps_car where etime=?",temp);
			remindCar(list);
		}
		
	}
	private void remindCar(List<Map<String,Object>> list) {
		if(list.size()==0)return;
		for(Map<String,Object> map:list) {
			List<Map<String,Object>> listUser=this.getUserList(map.get("id"));
			for(Map<String,Object> user:listUser) {
				this.jt808CommonService.pushMessageToUserId(user.get("id").toString(),"服务即将到期："+map.get("carno").toString()+"["+map.get("id").toString()+"]，到期日期为："+formatDate(map.get("etime").toString())+"。");
				
			}
		}
		
	}
	private List<Map<String,Object>> getUserList(Object tid){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_user where id in(select user_id from tgps_group_user where group_id in(select group_id from tgps_car where id=?))",tid);
		
		return list;
	}
	private void remindInsure(List<Map<String,Object>> list) {
		if(list.size()==0)return;
		for(Map<String,Object> map:list) {
			this.jt808CommonService.pushMessageToUserId(map.get("user_id").toString(),"保险即将到期："+map.get("carno").toString()+"，到期日期为："+formatDate(map.get("edate").toString())+"。");
			this.jdbcTemplate.update("update toa_insure set is_remind='1' where id=?",map.get("id"));
		}
		
	}
	private void remindSim(List<Map<String,Object>> list) {
		if(list.size()==0)return;
		for(Map<String,Object> map:list) {
			this.jt808CommonService.pushMessageToUserId(map.get("user_id").toString(),"SIM卡即将到期:"+map.get("sim").toString()+"，到期日期为："+formatDate(map.get("edate").toString())+"。");
			this.jdbcTemplate.update("update toa_sim set is_remind='1' where id=?",map.get("id"));
		}
	}
	private void remindMot(List<Map<String,Object>> list) {
		if(list.size()==0)return;
		for(Map<String,Object> map:list) {
			this.jt808CommonService.pushMessageToUserId(map.get("user_id").toString(),"年检即将到期:"+map.get("carno").toString()+"，到期日期为："+formatDate(map.get("edate").toString())+"。");
			this.jdbcTemplate.update("update toa_mot set is_remind='1' where id=?",map.get("id"));
		}
	}

	private String formatDate(String date) {
		return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	}
}
