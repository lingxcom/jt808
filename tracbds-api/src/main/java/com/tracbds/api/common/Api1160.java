package com.tracbds.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.tracbds.core.service.JT808CommonService;
@Component
public class Api1160 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
	@Override
	public int getApiCode() {
		return 1160;
	}
	@Override
	public String getApiName() {
		return "手机车队车辆列表";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String token = IApi.getParamString(params, "lingxtoken", "");
		String userid = TokenUtils.getTokenDataUserId(token);
		List<Map<String,Object>> removeList=new ArrayList<>();
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id,name from tgps_group where id in(select group_id from tgps_group_user where user_id=?) order by CONVERT(name USING gbk) asc",userid);
		for(Map<String,Object> groupMap:list) {
			int online=0;
			List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select id,carno text from tgps_car where id in(select car_id from tgps_group_car where group_id=?) order by online desc,carno asc",groupMap.get("id"));
			for(Map<String,Object> map:list2){
				Map<String,Object> cache=commonService.getLast0x0200Data(map.get("id").toString());//cacheService.getCar(map.get("id").toString());
				int zt=commonService.getJT808Status(cache);
				map.putAll(cache);
				map.put("status", zt);
				map.put("checked", false);
				if(map.containsKey("online")&&"1".equals(map.get("online").toString()))online++;
			}
			groupMap.put("checked", false);
			groupMap.put("cars", list2);
			groupMap.put("online", online);
			groupMap.put("total", list2.size());
			if(list2.size()==0)removeList.add(groupMap);
		}
		list.removeAll(removeList);
		ret.put("data", list);
		return ret;

	}

	
}
