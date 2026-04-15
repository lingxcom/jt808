package com.lingx.jt808.api.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.service.GroupService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1171 extends AbstractAuthApi{
	@Autowired
	private GroupService groupService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getApiCode() {
		return 1171;
	}

	public boolean isLog() {
		return false;
	}
	@Override
	public String getApiName() {
		return "里程报表";
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
		String tids=this.getParamString(params, "tids", "[]");
		String gids=this.getParamString(params, "gids", "[]");
		String stime=this.getParamString(params, "stime", "");
		String etime=this.getParamString(params, "etime", "");
		List<Integer> listTids=(List<Integer>)JSON.parse(tids);
		
		List<Integer> listGids=(List<Integer>)JSON.parse(gids);
		Set<Integer> sets=this.groupService.getCarIdsByGroupIds(listGids.toArray());
		if(listTids!=null)
		sets.addAll(listTids);
		List<Map<String,Object>> list,listRet=null;
		for(int carid:sets) {
			listRet=this.jdbcTemplate.queryForList("select create_time,dtlc from tgps_mileage where car_id=? and create_time>=? and create_time<=? order by create_time desc",
					carid,stime,etime);
		} 
		ret.put("data", listRet);
		return ret;
	}
}
