package com.lingx.jt808.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.jt808.IJT808Cache;
import com.lingx.jt808.server.netty.JT808Server;
import com.lingx.jt808.server.netty.Utils;

@Component
public class JT808DataService {
	@Resource
	private JT808Server jt808Server;
	@Resource
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select * from tgps_car");
		for(Map<String,Object> map:list) {
			IJT808Cache.CACHE_0x0200.put(map.get("id").toString(), map);
		}
		new Thread(jt808Server).start();
	}

	public int handler0704(String tid,Map<String, Object> map) {
		if(map.get("A01")==null)map.put("A01",0);
		if(map.get("A02")==null)map.put("A02",0);
		int status=Integer.parseInt(map.get("status").toString());
		
		this.jdbcTemplate.update("insert into tgps_data(car_id,acc,location,alarm,status,lat,lng,height,speed,direction,gpstime,systime,mileage) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				map.get("tid"),(status&0b01)>0?"1":"0",(status&0b10)>0?"1":"0",map.get("alarm"),map.get("status"),map.get("lat"),map.get("lng"),map.get("height"),map.get("speed"),map.get("direction"),map.get("gpstime"),Utils.getTime(),map.get("A01"));

		return 0;
	}

	public int handler0200(String tid,Map<String, Object> map) {
		IJT808Cache.CACHE_0x0200.put(tid, map);
		int status=Integer.parseInt(map.get("status").toString());
		this.jdbcTemplate.update("insert into tgps_data(car_id,acc,location,alarm,status,lat,lng,height,speed,direction,gpstime,systime,mileage) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				map.get("tid"),(status&0b01)>0?"1":"0",(status&0b10)>0?"1":"0",map.get("alarm"),map.get("status"),map.get("lat"),map.get("lng"),map.get("height"),map.get("speed"),map.get("direction"),map.get("gpstime"),Utils.getTime(),map.get("A01"));
		int c=this.jdbcTemplate.update("update tgps_car set acc=?,location=?,alarm=?,status=?,lat=?,lng=?,height=?,speed=?,direction=?,gpstime=?,systime=?,mileage=? where id=?",
				(status&0b01)>0?"1":"0",(status&0b10)>0?"1":"0",map.get("alarm"),map.get("status"),map.get("lat"),map.get("lng"),map.get("height"),map.get("speed"),map.get("direction"),map.get("gpstime"),Utils.getTime(),map.get("A01"),map.get("tid"));
		if(c==0)this.addCar(map);
		return 0;
	}

	/**
	 * 车辆注册
	 * 
	 * @param map
	 * @return 0：成功；1：车辆已被注册；2：数据库中无该车辆； 3：终端已被注册；4：数据库中无该终端
	 */
	public int handle0100(String tid,Map<String, Object> map) {
		try {
			if (this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id=?", Integer.class,
					tid) == 0) {
				this.addCar(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 下发鉴权码
	 * 
	 * @return
	 */
	public String get0100Code(String tid) {
		return "1234567890A";
	}

	/**
	 * 检查设备上报的鉴权码
	 * 
	 * @param code
	 * @return
	 */
	public boolean check0200Code(String tid,String code) {
		return true;
	}

	private void addCar(Map<String, Object> map) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sdate = new Date();
			Date edate = new Date();
			edate.setYear(edate.getYear() + 1);
			String tid = map.get("tid").toString();
			if (map.get("p7") == null || "".equals(map.get("p7").toString())) {
				map.put("p7", tid);
			}
			if (map.get("p5") == null) {map.put("p5",tid);}
			this.jdbcTemplate.update(
					"insert into tgps_car(id,carno,zdid,sim,stime,etime,create_time,modify_time,ter_id) values(?,?,?,?,?,?,?,?,?)",
					tid, map.get("p7"), map.get("p5"), tid, sdf.format(sdate), sdf.format(edate),
					map.get("systemtime"), map.get("systemtime"),0);
			if (this.jdbcTemplate.queryForObject("select count(*) from tgps_group_car where car_id=?", Integer.class,
					tid) == 0)
				this.jdbcTemplate.update("insert into tgps_group_car(group_id,car_id) values(?,?)", 147, tid);
			map = null;
			tid = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
