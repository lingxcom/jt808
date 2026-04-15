package com.lingx.jt808.api.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.utils.SqlSafe;
import com.lingx.jt808.core.utils.Utils;
@Component
public class Api1103 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public static final String vif=" and ( id in(select group_id from tgps_group_user where user_id=?) or '6e0362e8-100e-11e5-b7ab-74d02b6b5f61' in (select role_id from tlingx_user_role where user_id=?))";
	@Override
	public int getApiCode() {
		return 1103;
	}
	@Override
	public String getApiName() {
		return "分组授权-获取分组树";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}
	
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String fid=IApi.getParamString(params, "fid", "");
		String userid=IApi.getParamString(params, "user_id", "");
		String searchText=IApi.getParamString(params, "searchText", "");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String cuserid=TokenUtils.getTokenDataUserId(token);
		List<String> checkedKeys=new ArrayList<>();
		List<Map<String,Object>> list;
		if(Utils.isNotNull(searchText)) {
			list=jdbcTemplate.queryForList("select id,name text from tgps_group where name like ? escape '\\' "+vif+" order by orderindex asc","%"+SqlSafe.escapeLike(searchText)+"%",cuserid,cuserid);
			boolean b=false;
			for(Map<String,Object> m:list){
				b= jdbcTemplate.queryForObject("select count(*) from tgps_group_user where user_id=? and group_id=?", Integer.class,userid,m.get("id"))>0;
				if(b)checkedKeys.add(m.get("id").toString());
				m.put("checked",b);
				if("0".equals(fid))
				m.put("state","open");
				m.put("leaf", true);
			}
		}else {
			list=treeGroup(fid,userid,checkedKeys,this.jdbcTemplate,cuserid);
		}
		
		ret.put("data", list);
		ret.put("checkedKeys", checkedKeys);
		return ret;
	}
	public List<Map<String,Object>> treeGroup(String fid,String userid,List<String> checkedKeys,JdbcTemplate jdbc,String cuserid){
		List<Map<String,Object>> list=jdbc.queryForList("select id,name text from tgps_group where fid=? "+vif+" order by orderindex asc",fid,cuserid,cuserid);
		boolean b=false;
		if("0".equals(fid)&&list.size()==0) {
			list=jdbc.queryForList("select id,name text,fid from tgps_group where 1=1 "+vif+" order by orderindex asc",cuserid,cuserid);
			//System.out.println(list.size());
			list=removeChiren(list);
			for(Map<String,Object> map:list) {
				//map.put("state", "open");
				checkedKeys.add(map.get("id").toString());
			}
			//System.out.println(list.size());
		}
		for(Map<String,Object> m:list){
			b= jdbc.queryForObject("select count(*) from tgps_group_user where user_id=? and group_id=?", Integer.class,userid,m.get("id"))>0;
			//System.out.println(userid+" + "+m.get("id").toString()+" = "+b);
			if(b)checkedKeys.add(m.get("id").toString());
			m.put("checked",b);
			if("0".equals(fid))
			m.put("state","open");
			List<Map<String,Object>> listsub=treeGroup(m.get("id").toString(),userid,checkedKeys,jdbc,cuserid);
			if(listsub.size()>0){
				m.put("children",listsub);
				//m.put("checked",false);
				m.put("leaf", false);
				//if(b)checkedKeys.remove(m.get("id").toString());
			}else {
				m.put("leaf", true);
				
			}
		}
		return list;
	}
	

	public static List<Map<String,Object>> removeChiren(List<Map<String,Object>> list){
		Set<String> ids=new HashSet<String>();
		List<Map<String,Object>> newList=new ArrayList<>();
		for(Map<String,Object> map:list) {
			ids.add(map.get("id").toString());
		}
		for(Map<String,Object> map:list) {
			if(!ids.contains(map.get("fid").toString())) {
				newList.add(map);
			}
		}
		return newList;
	}

	public boolean isLog() {
		return false;
	}
}
