package com.lingx.jt808.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component(value="GroupService")
public class GroupService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String getGroupNameBy(String carid) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name from tgps_group where id =(select group_id  from tgps_car where id=?)",carid);
		if(list.size()==0)return "";
		return list.get(0).get("name").toString();
	}
	public Set<Integer> getCarIdsByGroupIds(Object... gids ){
		Set<Integer> sets=new HashSet<>();
		for(Object gid:gids) {
			this.addSetByGroupId(gid, sets);
		}
		return sets;
	}

	public Set<String> getCarIdsByGroupIds2(String groupId,String type,String userid){
		Set<String> sets=new HashSet<>();
		this.addSetByGroupId(groupId, sets,type,userid);
		return sets;
	}

	private void addSetByGroupId(Object groupId,Set<String> sets,String type,String userid) {
		String vif="";
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=?"
				,groupId);
		for(Map<String,Object> map:listGroups){
			this.addSetByGroupId(map.get("id").toString(), sets, type, userid);
		}
		switch(type) {
		case "all":break;
		case "online":
			vif=" and online='1'";
			break;
		case "offline":
			vif=" and online<>'1'";
			break;
		case "follow":
			vif=" and id in(select car_id from tgps_car_follow where user_id=?)";
			break;
		}
		List<Map<String,Object>> listCar;
		if("follow".equals(type)) {
			listCar=this.jdbcTemplate.queryForList("select id from tgps_car where group_id=? "+vif,groupId,userid);
		} else {
			listCar=this.jdbcTemplate.queryForList("select id from tgps_car where group_id=? "+vif,groupId);
		}
		for(Map<String,Object> map:listCar) {
			sets.add(map.get("id").toString());
		}
	}

	private void addSetByGroupId(Object groupId,Set<Integer> sets) {
	
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=?"
				,groupId);
		for(Map<String,Object> map:listGroups){
			this.addSetByGroupId(map.get("id").toString(), sets);
		}
		List<Map<String,Object>> listCar=this.jdbcTemplate.queryForList("select id from tgps_car where group_id=?",groupId);
		for(Map<String,Object> map:listCar) {
			sets.add(Integer.parseInt(map.get("id").toString()));
		}
	}
}
