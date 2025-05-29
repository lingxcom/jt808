package com.tracbds.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.service.GroupService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1136 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupService groupService;
	@Override
	public int getApiCode() {
		return 1136;
	}
	@Override
	public String getApiName() {
		return "根据分组ID获取所有车辆ID";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, ret, "groupId"))return ret;
		String groupId=IApi.getParamString(params, "groupId", "");
		Set<String> sets=this.groupService.getCarIdsByGroupIds(groupId);
		List<Map<String,Object>> list=new ArrayList<>();
		StringBuilder tids=new StringBuilder();
		for(String id:sets) {
			Map<String,Object> map=this.jdbcTemplate.queryForMap("select id `value`,carno text from tgps_car where id=?",id);
			list.add(map);
			tids.append(id).append(",");
		}
		if(tids.length()>0)tids.deleteCharAt(tids.length()-1);
		ret.put("data", list);
		ret.put("deviceIds", sets);
		ret.put("deviceIdString", tids.toString());
		return ret;

	}

	public boolean isLog() {
		return false;
	}
}
