package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1104 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Override
	public int getApiCode() {
		return 1104;
	}
	@Override
	public String getApiName() {
		return "分组授权";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String userid=IApi.getParamString(params, "user_id", "");
		String groupid=IApi.getParamString(params, "group_id", "");
		String checked=IApi.getParamString(params, "checked", "");
		String checkedSub=IApi.getParamString(params, "checkedSub", "");
		if("true".equals(checked)&&"false".equals(checkedSub)) {//||"true".equals(checkedSub)
			if(lingxService.queryForInt("select count(*) from tgps_group_user where user_id=? and group_id=?", userid,groupid)==0)
			this.jdbcTemplate.update("insert into tgps_group_user(user_id,group_id)values(?,?)",userid,groupid);
			addSubAuth(groupid,userid);
			
		}else if("true".equals(checked)&&"true".equals(checkedSub)) {//||"true".equals(checkedSub)
			if(lingxService.queryForInt("select count(*) from tgps_group_user where user_id=? and group_id=?", userid,groupid)==0) {
			this.jdbcTemplate.update("insert into tgps_group_user(user_id,group_id)values(?,?)",userid,groupid);
			addSubAuth(groupid,userid);
			}
		}else if("false".equals(checked)&&"false".equals(checkedSub)) {
			if(lingxService.queryForInt("select count(*) from tgps_group_user where user_id=? and group_id=?", userid,groupid)>0) {
			this.jdbcTemplate.update("delete from tgps_group_user where user_id=? and group_id=?",userid,groupid);
			delSubAuth(groupid,userid);
			}
		}
		return ret;
	}
	
	private void addSubAuth(Object fid,String userid) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tgps_group where fid=?",fid);
		for(Map<String,Object> map:list) {
			if(lingxService.queryForInt("select count(*) from tgps_group_user where user_id=? and group_id=?", userid,map.get("id"))==0)
				this.jdbcTemplate.update("insert into tgps_group_user(user_id,group_id)values(?,?)",userid,map.get("id"));
			addSubAuth(map.get("id"),userid);
		}
	}

	private void delSubAuth(Object fid,String userid) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tgps_group where fid=?",fid);
		for(Map<String,Object> map:list) {
			this.jdbcTemplate.update("delete from tgps_group_user where user_id=? and group_id=?",userid,map.get("id"));
			delSubAuth(map.get("id"),userid);
		}
	}
}
