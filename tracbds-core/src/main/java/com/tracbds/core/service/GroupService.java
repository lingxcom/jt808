package com.tracbds.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
@Component(value="GroupService")
public class GroupService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String getGroupNameBy(String carid) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=?)",carid);
		if(list.size()==0)return "";
		return list.get(0).get("name").toString();
	}
	public Set<String> getCarIdsByGroupIds(Object... gids ){
		Set<String> sets=new HashSet<>();
		for(Object gid:gids) {
			this.addSetByGroupId(gid, sets);
		}
		return sets;
	}


	private void addSetByGroupId(Object groupId,Set<String> sets) {
	
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=?"
				,groupId);
		for(Map<String,Object> map:listGroups){
			this.addSetByGroupId(map.get("id").toString(), sets);
		}
		List<Map<String,Object>> listCar=this.jdbcTemplate.queryForList("select id from tgps_car where id in (select car_id from tgps_group_car where group_id=?)",groupId);
		for(Map<String,Object> map:listCar) {
			sets.add(map.get("id").toString());
		}
	}
}
