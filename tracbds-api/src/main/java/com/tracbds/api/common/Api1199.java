package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.utils.Utils;
import com.lingx.service.LingxService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

@Component
public class Api1199 extends AbstractAuthApi implements IApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private LingxService lingxService;

	@Override
	public int getApiCode() {
		return 1199;
	}

	@Override
	public String getApiName() {
		return "获取设备报警信息";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String, Object> ret = IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "car_id",ret))return ret; 
		if(this.checkIsNull(params, "stime",ret))return ret; 
		if(this.checkIsNull(params, "etime",ret))return ret; 
		String car_id=IApi.getParamString(params, "car_id", "");
		String stime=IApi.getParamString(params, "stime", "");
		String etime=IApi.getParamString(params, "etime", "");
		
		String sql="select * from tgps_car_alarm where car_id=? and gpstime>=? and gpstime <=? order by gpstime desc";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql,car_id,stime,etime);
		ret.put("data", list);
		return ret;

	}

}
