package com.lingx.jt808.api.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.service.JT808CommandService;
@Component
public class Api1109 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommandService commandService;
	@Override
	public int getApiCode() {
		return 1109;
	}
	@Override
	public String getApiName() {
		return "下发终端指令";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		
		try {
			String token=IApi.getParamString(params, "lingxtoken", "");
			String car_id=IApi.getParamString(params, "car_id", "");
			String cmdId=IApi.getParamString(params, "cmdId", "");
			String cmdParams=IApi.getParamString(params, "cmdParams", "");
			String userid=TokenUtils.getTokenDataUserId(token);
			Map<String,Object> cmdParamsMap=new HashMap<>();
			if(Utils.isNotNull(cmdParams)) {
				List<Map<String,Object>> list=(List<Map<String, Object>>)JSON.parse(cmdParams);
				for(Map<String,Object> m:list) {
					cmdParamsMap.put(m.get("code").toString(), m.get("value"));
				}
			}
			ret=this.commandService.sendCommand(cmdId,car_id,  userid,cmdParamsMap );
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("message", e.getLocalizedMessage());
			return ret;
		}
	}
	
}
