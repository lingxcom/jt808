package com.tracbds.api.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1107 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Override
	public int getApiCode() {
		return 1107;
	}
	@Override
	public String getApiName() {
		return "指令授权";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String userid=IApi.getParamString(params, "user_id", "");
		String cmdid=IApi.getParamString(params, "cmd_id", "");
		String checked=IApi.getParamString(params, "checked", "");
		String checkedSub=IApi.getParamString(params, "checkedSub", "");
		//if("true".equals(checkedSub))checkedSub=String.valueOf(this.lingxService.queryForInt("select count(*) from tlingx_menu where fid=?", cmdid)>0);
		if(Utils.isNull(cmdid))return ret;
		if("true".equals(checked)&&"false".equals(checkedSub)) {//||"true".equals(checkedSub)
			if(lingxService.queryForInt("select count(*) from tgps_cmd_user where user_id=? and cmd_id=?", userid,cmdid)==0)
			this.jdbcTemplate.update("insert into tgps_cmd_user(user_id,cmd_id)values(?,?)",userid,cmdid);
			
		}else if("false".equals(checked)&&"true".equals(checkedSub)) {//||"true".equals(checkedSub)
			if(lingxService.queryForInt("select count(*) from tgps_cmd_user where user_id=? and cmd_id=?", userid,cmdid)==0) {
			this.jdbcTemplate.update("insert into tgps_cmd_user(user_id,cmd_id)values(?,?)",userid,cmdid);
			}
		}else if("false".equals(checked)&&"false".equals(checkedSub)) {
			if(lingxService.queryForInt("select count(*) from tgps_cmd_user where user_id=? and cmd_id=?", userid,cmdid)>0) {
			this.jdbcTemplate.update("delete from tgps_cmd_user where user_id=? and cmd_id=?",userid,cmdid);
			}
		}
		return ret;
	}
	
}
