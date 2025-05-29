package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1154 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getApiCode() {
		return 1154;
	}
	@Override
	public String getApiName() {
		return "每日里程流水";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "stime",ret))return ret; 
		if(this.checkIsNull(params, "etime",ret))return ret; 
		if(this.checkIsNull(params, "tid",ret))return ret; 
		String tid=this.getParamString(params, "tid", "[]");
		String stime=this.getParamString(params, "stime", "");
		String etime=this.getParamString(params, "etime", "");
		if(stime.length()>8)stime=stime.substring(0,8);
		if(etime.length()>8)etime=etime.substring(0,8);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select create_time date,dtlc mileage,lc total  from tgps_mileage where car_id=? and create_time>=? and create_time<=?",
				tid,stime,etime);
		ret.put("data", list);
		return ret;

	}
	
	public boolean isLog() {
		return false;
	}
}
