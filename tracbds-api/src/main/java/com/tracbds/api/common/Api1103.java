package com.tracbds.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1103 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
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
		List<String> checkedKeys=new ArrayList<>();
		List<Map<String,Object>> list;
		if(Utils.isNotNull(searchText)) {
			list=jdbcTemplate.queryForList("select id,name text from tgps_group where name like '%"+searchText+"%' order by orderindex asc");
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
			list=treeGroup(fid,userid,checkedKeys,this.jdbcTemplate);
		}
		
		ret.put("data", list);
		ret.put("checkedKeys", checkedKeys);
		return ret;
	}
	public List<Map<String,Object>> treeGroup(String fid,String userid,List<String> checkedKeys,JdbcTemplate jdbc){
		List<Map<String,Object>> list=jdbc.queryForList("select id,name text from tgps_group where fid=? order by orderindex asc",fid);
		boolean b=false;
		for(Map<String,Object> m:list){
			b= jdbc.queryForObject("select count(*) from tgps_group_user where user_id=? and group_id=?", Integer.class,userid,m.get("id"))>0;
			if(b)checkedKeys.add(m.get("id").toString());
			m.put("checked",b);
			if("0".equals(fid))
			m.put("state","open");
			List<Map<String,Object>> listsub=treeGroup(m.get("id").toString(),userid,checkedKeys,jdbc);
			if(listsub.size()>0){
				m.put("children",listsub);
				m.put("checked",false);
				m.put("leaf", false);
				if(b)checkedKeys.remove(m.get("id").toString());
			}else {
				m.put("leaf", true);
				
			}
		}
		return list;
	}

}
