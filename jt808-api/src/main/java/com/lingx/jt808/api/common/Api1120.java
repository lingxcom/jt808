package com.lingx.jt808.api.common;

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
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1120 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Resource 
	private LanguageService languageService;
	@Override
	public int getApiCode() {
		return 1120;
	}
	@Override
	public String getApiName() {
		return "获取表单参数";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String type=IApi.getParamString(params, "type", "1");
		String id=IApi.getParamString(params, "id", "");
		String etype=IApi.getParamString(params, "etype", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		List<Map<String,Object>> list;
		switch(type) {
		case "1":
			list=this.jdbcTemplate.queryForList("select params from tgps_rule where id=? ",id);
			if(list.size()>0) {
				ret.put("data", language(JSON.parse(list.get(0).get("params").toString()),params));
			}
			break;
		case "2":
			ret.put("data",  language(lingxService.getList(IApi.getParamString(params, "p1", ""),etype,id),params));
			break;
		case "3":
			list=this.jdbcTemplate.queryForList("select params from tgps_action where id=? ",id);
			if(list.size()>0) {
				ret.put("data",  language(JSON.parse(list.get(0).get("params").toString()),params));
			}
			break;
		case "4":
			ret.put("data",  language(lingxService.getList(IApi.getParamString(params, "p1", ""),etype,id),params));
			break;
		}
		return ret;
	}
	
	private Object language(Object obj,Map<String, Object> params) {
		String language=IApi.getParamString(params, "language", "zh-CN");
		List<Map<String,Object>> list=(List<Map<String,Object>>)obj;
		if(list==null)return null;
		for(Map<String,Object> map:list) {
			if(map.containsKey("name"))map.put("name", this.languageService.text(map.get("name"), language));
			if(map.containsKey("optionitem")) {
				String json=map.get("optionitem").toString();
				List<Map<String,Object>> listOption=(List<Map<String,Object>>)JSON.parse(json);
				for(Map<String,Object>  item:listOption) {
					item.put("text", this.languageService.text(item.get("text"), language));
				}
				map.put("optionitem", listOption);
			}
		}
		return list;
	}
	
	
}
