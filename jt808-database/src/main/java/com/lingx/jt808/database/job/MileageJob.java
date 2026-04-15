package com.lingx.jt808.database.job;

import java.text.SimpleDateFormat;
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

import com.lingx.utils.Utils;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.utils.SqlSafe;
import com.lingx.jt808.core.service.RedisService;

@Component(value = "MileageJob")
public class MileageJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RedisService redisService;
	@Value("#{configs['jt808.database.job.mileage.enabled']}")
	private String enabled = "true";//是否启用该功能

	@Value("#{configs['jt808.database.job.model']}")
	private String model="history";//mileage
	
	@PostConstruct
	public void init(){
		
	}
	
	/** 每天 0:30 检查近三天 tgps_mileage.dtlc，与按 lc 推算结果不一致或明显异常时回写 */
	@Scheduled(cron = "0 30 0 * * ?")
	public void fixRecentDtlc() {
		if (!"true".equals(this.enabled)) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		for (int i = 1; i < 3; i++) {
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_MONTH, -i);
			String dayStr = sdf.format(cal.getTime());
			fixDtlcForDay(dayStr);
		}
	}

	private void fixDtlcForDay(String dayYyyyMMdd) {
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(
				"select id,car_id,lc,dtlc from tgps_mileage where create_time=?", dayYyyyMMdd);
		for (Map<String, Object> row : rows) {
			Object idObj = row.get("id");
			Object carObj = row.get("car_id");
			if (idObj == null || carObj == null) {
				continue;
			}
			String id = idObj.toString();
			String tid = carObj.toString();
			double lm = toDouble(row.get("lc"));
			double storedDtlc = toDouble(row.get("dtlc"));
			double pm = 0;
			try {
				Double p = this.jdbcTemplate.queryForObject(
						"select lc from tgps_mileage where car_id=? and create_time<? order by create_time desc, id desc limit 1",
						Double.class, tid, dayYyyyMMdd);
				if (p != null) {
					pm = p.doubleValue();
				}
			} catch (Exception e) {
				pm = 0;
			}
			double newDtlc = recomputeDtlcForRepair(lm, pm);
			if (!isDtlcProblematic(storedDtlc, newDtlc)) {
				continue;
			}
			this.jdbcTemplate.update("update tgps_mileage set dtlc=? where id=?", newDtlc, id);
		}
	}

	/** 与主任务意图一致：无昨日里程时小车按 lm 计日里程，否则日增量做上下限裁剪 */
	private static double recomputeDtlcForRepair(double lm, double pm) {
		if (lm == 0) {
			return 0;
		}
		if (pm == 0) {
			if (lm < 300) {
				return lm;
			}
			return 0;
		}
		double dtlc = lm - pm;
		if (dtlc < 1) {
			dtlc = 0;
		}
		if (dtlc > 2000) {
			dtlc = 0;
		}
		return dtlc;
	}

	private static boolean isDtlcProblematic(double stored, double expected) {
		if (stored < 0 || stored > 2000) {
			return true;
		}
		return Math.abs(stored - expected) >= 0.5;
	}

	private static double toDouble(Object v) {
		if (v == null) {
			return 0;
		}
		if (v instanceof Number) {
			return ((Number) v).doubleValue();
		}
		try {
			return Double.parseDouble(v.toString());
		} catch (Exception e) {
			return 0;
		}
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
			if(pm==0&&dtlc>1000)dtlc=0;//大于1000是考虑到，其他车子上换下来的
			if(pm==0&&lm<300)dtlc=lm;
			if(dtlc>2000)dtlc=0;
			redisService.set("dtlc_"+tid, String.valueOf(dtlc));
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
		String tbl = SqlSafe.sanitizeTableDate8FromGpstime(date + "000000");
		if (tbl == null) {
			return 0;
		}
		String sql="select mileage from tgps_data_"+tbl+" where car_id=? and gpstime>? and x0704='0' and mileage>0 order by gpstime desc limit 1";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql,tid, tbl+"000000");
		if(list.size()>0) {
			return Double.parseDouble(list.get(0).get("mileage").toString());
		}else return 0;
	}
	private double getHistoryMileage(String tid) {
		String date=Utils.getTime().substring(0,8);
		String tbl = SqlSafe.sanitizeTableDate8FromGpstime(date + "000000");
		if (tbl == null) {
			return 0;
		}
		String sql="select mileage from tgps_data_"+tbl+" where car_id=? and gpstime>? and x0704='0' and mileage>0 order by gpstime asc limit 1";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql,tid, tbl+"000000");
		if(list.size()>0) {
			return Double.parseDouble(list.get(0).get("mileage").toString());
		}else return 0;
	}
	
}
