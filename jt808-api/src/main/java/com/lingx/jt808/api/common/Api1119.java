package com.lingx.jt808.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.service.HttpService;
import com.lingx.jt808.core.service.JT808CommandService;
import com.lingx.jt808.core.utils.LatLngUtils;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1119 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Override
	public int getApiCode() {
		return 1119;
	}
	public boolean isLog() {
		return false;
	}
	@Override
	public String getApiName() {
		return "获取点线面地图标志";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenData(token).getUserId();
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name,type,num,latlngs,color,radius from tgps_pls where is_map='1'  and user_id=? order by id desc", userid);
		ret.put("rows", list);

		return ret;

	}
	
	
}
