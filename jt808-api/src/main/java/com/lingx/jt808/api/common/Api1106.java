package com.lingx.jt808.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1106 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String vif=" and ( id in(select cmd_id from tgps_cmd_user where user_id=?) or '6e0362e8-100e-11e5-b7ab-74d02b6b5f61' in (select role_id from tlingx_user_role where user_id=?))";
	
	@Override
	public int getApiCode() {
		return 1106;
	}
	@Override
	public String getApiName() {
		return "指令授权-获取指令树";
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
		String token=IApi.getParamString(params, "lingxtoken", "");
		String cuserid=TokenUtils.getTokenDataUserId(token);
		List<String> checkedKeys=new ArrayList<>();
		List<Map<String,Object>> list=treeCmd(fid,userid,checkedKeys,this.jdbcTemplate,cuserid);
		ret.put("data", list);
		ret.put("checkedKeys", checkedKeys);
		return ret;
	}
	public List<Map<String,Object>> treeCmd(String fid,String userid,List<String> checkedKeys,JdbcTemplate jdbc,String cuserid){
		List<Map<String,Object>> list=jdbc.queryForList("select id,name text from tgps_cmd where enabled='1' "+vif+" order by orderindex asc",cuserid,cuserid);
		boolean b=false;
		for(Map<String,Object> m:list){
			b= jdbc.queryForObject("select count(*) from tgps_cmd_user where user_id=? and cmd_id=?", Integer.class,userid,m.get("id"))>0;
			if(b)checkedKeys.add(m.get("id").toString());
			m.put("checked",b);
			if("0".equals(fid))
			m.put("state","open");
			m.put("leaf", true);
		}
		return list;
	}

}
