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
public class Api1198 extends AbstractAuthApi implements IApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private LingxService lingxService;

	@Override
	public int getApiCode() {
		return 1198;
	}

	@Override
	public String getApiName() {
		return "处理报警信息";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String, Object> ret = IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "car_id",ret))return ret; 
		if(this.checkIsNull(params, "id",ret))return ret; 
		if(this.checkIsNull(params, "name",ret))return ret; 
		if(this.checkIsNull(params, "type",ret))return ret; 
		String car_id=IApi.getParamString(params, "car_id", "");
		String id=IApi.getParamString(params, "id", "");
		String name=IApi.getParamString(params, "name", "");
		String type=IApi.getParamString(params, "type", "");
		String comment=IApi.getParamString(params, "comment", "");
		if("1".equals(type)) {//同设备全部处理
			this.jdbcTemplate.update("update tgps_car_alarm set status='2',comment=? where car_id=? and status='1'",comment,car_id);
		}else if("2".equals(type)) {//同设备同名称全部处理
			this.jdbcTemplate.update("update tgps_car_alarm set status='2',comment=? where car_id=? and name=? and status='1'",comment,car_id,name);
		}else if("3".equals(type)) {//单个处理
			this.jdbcTemplate.update("update tgps_car_alarm set status='2',comment=? where id=?",comment,id);
		}
		return ret;

	}

}
