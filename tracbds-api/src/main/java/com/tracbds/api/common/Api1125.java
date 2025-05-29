package com.tracbds.api.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.tracbds.core.service.GroupService;
@Component
public class Api1125 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupService groupService;
	@Override
	public int getApiCode() {
		return 1125;
	}
	@Override
	public String getApiName() {
		return "报警统计";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "stime",ret))return ret; 
		if(this.checkIsNull(params, "etime",ret))return ret; 
		String tids=this.getParamString(params, "tids", "[]");
		String gids=this.getParamString(params, "gids", "[]");
		String stime=this.getParamString(params, "stime", "");
		String etime=this.getParamString(params, "etime", "");
		//System.out.println(tids);
		stime=stime+"000000";
		etime=etime+"235959";
		List<String> listTids=(List<String>)JSON.parse(tids);
		List<Integer> listGids=(List<Integer>)JSON.parse(gids);
		Set<String> sets=this.groupService.getCarIdsByGroupIds(listGids.toArray());
		if(listTids!=null)
		sets.addAll(listTids);
		List<String> listTypes=getTypes(sets,stime,etime);
		List<Map<String,Object>> list=new ArrayList<>();
		try {
			for(String tid:sets) {
				Map<String,Object> map=new HashMap<>();
				map.putAll(this.jdbcTemplate.queryForMap("select tid,carno from tgps_car where id=?",tid));
				map.put("group", this.jdbcTemplate.queryForObject("select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=?)", String.class,tid));
				
				for(String type:listTypes) {
					map.put(type, this.jdbcTemplate.queryForObject("select count(*) from tgps_car_alarm where car_id=? and name = ? and gpstime>? and gpstime<?", Integer.class,tid,type,stime,etime));
					if(map.get(type)==null)map.put(type, 0);
					
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("data", list);
		ret.put("types", listTypes);
		return ret;

	}
	
	private List<String> getTypes(Set<String> sets,String stime,String etime){
		String sql="select name,count(name) total from tgps_car_alarm where car_id in(:ids) and gpstime>:stime and gpstime<:etime  GROUP BY name ORDER BY total desc";
		NamedParameterJdbcTemplate jdbc=new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<String,Object> param=new HashMap<>();
		param.put("ids", sets);
		param.put("stime", stime);
		param.put("etime", etime);
		List<Map<String,Object>> list=jdbc.queryForList(sql, param);
		List<String> listRet=new ArrayList<>();
		for(Map<String,Object> map:list) {
			listRet.add(map.get("name").toString());
		}
		return listRet;
	}
	
}
