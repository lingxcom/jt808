package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.service.LanguageService;
import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1108 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource 
	private LanguageService languageService;
	@Override
	public int getApiCode() {
		return 1108;
	}
	@Override
	public String getApiName() {
		return "获取可操作指令";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String tid=IApi.getParamString(params, "tid", "");
		String type=IApi.getParamString(params, "type", "1");
		String language=IApi.getParamString(params, "language", "zh-CN");
		String userid=TokenUtils.getTokenDataUserId(token);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select * from tgps_cmd where type=? and id in(select cmd_id from tgps_cmd_user where user_id=?) order by orderindex asc",type,userid);
		for(Map<String,Object> map:list) {
			map.put("name", this.languageService.text(map.get("name"), language));
			
			if(map.containsKey("params"))map.put("params", text(map.get("params").toString(), language));
		}
		ret.put("data", list);
		return ret;
	}
	private String text(String json,String language) {
		if(Utils.isNull(json))return "";
		List<Map<String,Object>> list=(List<Map<String,Object>>)JSON.parse(json);
		for(Map<String,Object> map:list) {
			map.put("name", this.languageService.text(map.get("name"), language));
			if(map.containsKey("optionitem")) {
				List<Map<String,Object>> optionitem=(List<Map<String,Object>>)map.get("optionitem");
				for(Map<String,Object> item:optionitem) {
					item.put("text", this.languageService.text(item.get("text"), language));
				}
			}
		}
		return JSON.toJSONString(list);
	}
}
