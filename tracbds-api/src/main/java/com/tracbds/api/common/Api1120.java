package com.tracbds.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.model.impl.DefaultEntity;
import com.lingx.service.LanguageService;
import com.lingx.service.ModelService;
import com.lingx.utils.SQLUtils;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1120 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ModelService modelService;
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
			ret.put("data",  language(getList(IApi.getParamString(params, "p1", ""),etype,id),params));
			break;
		case "3":
			list=this.jdbcTemplate.queryForList("select params from tgps_action where id=? ",id);
			if(list.size()>0) {
				ret.put("data",  language(JSON.parse(list.get(0).get("params").toString()),params));
			}
			break;
		case "4":
			ret.put("data",  language(getList(IApi.getParamString(params, "p1", ""),etype,id),params));
			break;
		}
		return ret;
	}
	
	private Object language(Object obj,Map<String, Object> params) {
		String language=IApi.getParamString(params, "language", "zh-CN");
		List<Map<String,Object>> list=(List<Map<String,Object>>)obj;
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
	private List<Map<String,Object>> getList(String p1,String etype0,String id){
		String sql = "select `%s`,`%s` from %s where `%s` in(%s) %s";
		etype0=SQLUtils.getValue(etype0);
		List<Map<String,Object>> list=(List<Map<String,Object>>)JSON.parse(p1);
		List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select params from "+etype0+" where id=? ",id);
		if(list2.size()>0) {
			List<Map<String,Object>> list3= (List<Map<String,Object>>)JSON.parse(list2.get(0).get("params").toString());
			for(Map<String,Object> map1:list) {
				for(Map<String,Object> map2:list3) {
					if(map1.get("code").toString().equals(map2.get("code").toString())) {
						map2.put("value", map1.get("value"));
					}
				}
			}
			list=list3;
		}
		
		
		for(Map<String,Object> map:list) {
			if(map.containsKey("refEntity")) {
				String etype=map.get("refEntity").toString();
				DefaultEntity subEntity = this.modelService.getEntity(etype);
				if (subEntity == null)
					continue;
			String	ename=subEntity.getName();
			String tableName = subEntity.getTableName();
			String vField = modelService.getValueField(subEntity);
			List<String> listField = modelService.getTextField(subEntity);
			List<String>  paramField= modelService.getParamField(subEntity);

			List<String> allList=new ArrayList<>();
			allList.addAll(listField);
			allList.addAll(paramField);
			List<Map<String,Object>> listMap =this.jdbcTemplate.queryForList(String.format(sql,this.modelService.getStringByList(allList).replaceAll(",", "`,`"),vField, tableName, vField,map.get("value"), "").trim());
			map.put("value", formatList(listMap,listField, vField, etype, ename));
			}
		}
		return list;
	}
	
	private List<Map<String,Object>> formatList( List<Map<String,Object>> listMap,List<String> listField,String vField,String etype,String ename) {
		for(Map<String,Object> map:listMap){
			StringBuilder sb = new StringBuilder();
			for (String s : listField) {
				sb.append(map.get(s.toUpperCase())).append("-");
			}
			sb.deleteCharAt(sb.length() - 1);
			map.put("text", sb.toString());
			map.put("value", map.get(vField.toUpperCase()));
			map.put("etype", etype);
			map.put("ecode", etype);
			map.put("ename", ename);

			map.put("islink", false);
			
		}
		return listMap;
	}
	
}
