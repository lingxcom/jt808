package com.lingx.jt808.api.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1130 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getApiCode() {
		return 1130;
	}
	@Override
	public String getApiName() {
		return "获取参数配置列表";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select * from tgps_jt808_param order by id asc");
		ret.put("data", list);
		return ret;

	}
	public boolean isLog() {
		return false;
	}
}
