package com.tracbds.database.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.lingx.utils.Utils;

@Component(value = "MileageJob")
public class MileageJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("#{configs['jt808.database.job.mileage.enabled']}")
	private String enabled = "true";//是否启用该功能

	@Value("#{configs['jt808.database.job.model']}")
	private String model="history";//mileage
	
	@PostConstruct
	public void init(){
		
	}
	
	@Scheduled(cron="0 0/10 * * * ?")//每10分钟计算
	public void checkMileage(){
		if(!"true".equals(this.enabled))return;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=new Date();
		String current=sdf.format(date);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id,mileage,gpstime from tgps_car where gpstime>?",current+"000000");
		
		date.setDate(date.getDate()-1);
		String predate=sdf.format(date),tid;
		for(Map<String,Object> map:list){
			tid=map.get("id").toString();
			double pm=0;//上一天的总里程
			//try {
			//	pm=this.jdbcTemplate.queryForObject("select lc from tgps_mileage where car_id=? and create_time<=? order by id desc limit 1",Double.class,map.get("id"),predate);
			//} catch (Exception e) {
				//e.printStackTrace();
			//}
			pm=this.getPreMileage(tid, predate);
			double lm=getCurrentMileage(tid);//Float.parseFloat(map.get("mileage").toString());//当前总里程
			if(lm==0||pm==0)continue;
			double dtlc=(lm-pm);
			if(dtlc<1)dtlc=0;
			IJT808Cache.set("dtlc_"+tid, String.valueOf(dtlc));
			if(this.jdbcTemplate.queryForObject("select count(*) from tgps_mileage where car_id=? and create_time=? ",Integer.class,map.get("id"),current)==0){
				this.jdbcTemplate.update("insert into tgps_mileage(car_id,lc,dtlc,create_time) values(?,?,?,?)",map.get("id"),lm,dtlc,current);
			}else{
				String id=this.jdbcTemplate.queryForObject("select id from tgps_mileage where car_id=? and create_time=? limit 1", String.class,map.get("id"),current);
						this.jdbcTemplate.update("update tgps_mileage set lc=?,dtlc=? where id=? and dtlc<?",lm,dtlc,id,dtlc);
			}
			
		}
		
		
	}

	private double getPreMileage(String tid,String predate) {
		if("mileage".equals(this.model)) {
			try {
				return this.jdbcTemplate.queryForObject("select lc from tgps_mileage where car_id=? and create_time<=? order by id desc limit 1",Double.class,tid,predate);
			} catch (Exception e) {
				//e.printStackTrace();
				return this.getHistoryMileage(tid);
			}
		}else {
			return this.getHistoryMileage(tid);
		}
	}
	public double getCurrentMileage(String tid) {
		String date=Utils.getTime().substring(0,8);
		String sql="select mileage from tgps_data_"+date+" where car_id=? and mileage>0 and gpstime>'"+date+"000000' order by gpstime desc limit 1";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql,tid);
		if(list.size()>0) {
			return Double.parseDouble(list.get(0).get("mileage").toString());
		}else return 0;
	}
	private double getHistoryMileage(String tid) {
		String date=Utils.getTime().substring(0,8);
		String sql="select mileage from tgps_data_"+date+" where car_id=? and mileage>0 and gpstime>'"+date+"000000' order by gpstime asc limit 1";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql,tid);
		if(list.size()>0) {
			return Double.parseDouble(list.get(0).get("mileage").toString());
		}else return 0;
	}
	
}
