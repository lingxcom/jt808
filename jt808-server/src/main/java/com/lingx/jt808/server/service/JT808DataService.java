package com.lingx.jt808.server.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.utils.Utils;

@Component
public class JT808DataService {
	@Value("#{configs['jt808.server.0x0100.autoadd']}")
	private String autoadd="true";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 允许上报的设备ID
	 */
	//private Set<String> tids=Collections.synchronizedSet(new HashSet<>());
	/**
	 * 车辆注册 
	 * {"p0":"2013","p1":0,"p2":0,"p3":"3031313131","p4":"4C494E4758313131313131313131313131313131","p5":"31313131313131","p6":1,"p7":"闽D12345","tid":"013800138999"}
	 * @param map
	 * @return 0：成功；1：车辆已被注册；2：数据库中无该车辆；
		3：终端已被注册；4：数据库中无该终端
	 */
	public int handle0100(Map<String,Object> map) {
		//System.out.println(JSON.toJSONString(map));
		
		try {
			String tid=map.get("tid").toString();
			if(this.jdbcTemplate.queryForObject("select count(*) from tgps_device where sim=?",Integer.class, tid)==0) {
				String sql="insert into tgps_device(sim,id1,id2,zzcid,zdxh,zdid,zpys,clbs,version,create_time) values(?,?,?,?,?,?,?,?,?,?)";
				this.jdbcTemplate.update(sql,tid,map.get("p1"),map.get("p2"),map.get("p3"),map.get("p4"),map.get("p5"),map.get("p6"),map.get("p7"),map.get("p0"),Utils.getTime());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String tid=map.get("tid").toString();
		if(this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id=?", Integer.class,tid)==0) {
			this.addCar(map);
		}/**/
		return 0;
	}
	/**
	 * 终端注销
	 * @param tid
	 */
	public void handle0003(String tid) {
		
	}
	/**
	 * 下发鉴权码
	 * @return
	 */
	public String get0100Code() {
		return "1234567890";
	}
	/**
	 * 检查设备上报的鉴权码
	 * @param code
	 * @return
	 */
	public boolean check0102Code(String code) {
		return true ;
	}
	
	private void addCar(Map<String, Object> map){
		if(!"true".equals(this.autoadd))return;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sdate=new Date();
			Date edate=new Date();
			edate.setYear(edate.getYear()+1);
			String tid=map.get("tid").toString();
			if(map.get("p7")==null||"".equals(map.get("p7").toString())) {
				map.put("p7", tid);
			}
			String time=Utils.getTime();
			this.jdbcTemplate.update("insert into tgps_car(id,carno,color,sim,stime,etime,create_time,modify_time,online) values(?,?,?,?,?,?,?,?,'1')",tid,map.get("p7"),map.get("p6"),map.get("p5"),sdf.format(sdate),sdf.format(edate),time,time);
			if(this.jdbcTemplate.queryForObject("select count(*) from tgps_group_car where car_id=?", Integer.class,tid)==0)
			this.jdbcTemplate.update("insert into tgps_group_car(group_id,car_id) values(?,?)",147,tid);
			//this.jdbcTemplate.update("update tgps_car t set group_name=(select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=t.id)) where id=?",tid);
			map=null;
			tid=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void saveEvent(String tid, String event) {
		this.jdbcTemplate.update("insert into tgps_event(car_id,event,ts) values(?,?,?)",tid,event,Utils.getTime());
	}
	
	public void handl0900(String tid,int type,byte[] data) {
			String hexstring=Utils.bytesToHex(data);
		
		this.jdbcTemplate.update("insert into tgps_jt808_touchuan(tid,type,data,ascii_code,uord,ts,remark) values(?,?,?,?,?,?,?)",
				tid,String.format("0x%02X", type),hexstring,new String(data),"上行",Utils.getTime(),"");
	}
	
}
