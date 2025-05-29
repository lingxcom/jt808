package com.tracbds.api.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.tracbds.core.service.GroupService;
@Component
public class Api1122 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupService groupService;
	@Override
	public int getApiCode() {
		return 1122;
	}
	@Override
	public String getApiName() {
		return "里程统计(月)";
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
		List<String> listTids=(List<String>)JSON.parse(tids);
		List<Integer> listGids=(List<Integer>)JSON.parse(gids);
		Set<String> sets=this.groupService.getCarIdsByGroupIds(listGids.toArray());
		if(listTids!=null)
		sets.addAll(listTids);
		List<Map<String,Object>> list=new ArrayList<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf3=new SimpleDateFormat("yyyyMMdd");
		Integer sum=0,dylc;
		try {
			for(String tid:sets) {
				sum=0;
				Map<String,Object> map=new HashMap<>();
				map.putAll(this.jdbcTemplate.queryForMap("select tid,carno from tgps_car where id=?",tid));
				map.put("group", this.jdbcTemplate.queryForObject("select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=?)", String.class,tid));
				Date sdate=sdf3.parse(stime); //new Date(Long.parseLong(stime));
				Date edate=sdf3.parse(etime); //new Date(Long.parseLong(etime));
				String key,month;
				while(sdate.getTime()<=edate.getTime()) {
					key=sdf.format(sdate);
					month=sdf2.format(sdate);
					dylc=this.jdbcTemplate.queryForObject("select sum(dtlc) from tgps_mileage where car_id=? and create_time>=? and create_time<=?", Integer.class,tid,month+"01",month+"31");
					if(dylc!=null) {
						map.put(key, dylc);
						sum+=dylc;
					}else {
						map.put(key, 0);
					}
					if(map.get(key)==null)map.put(key, 0);
					sdate.setMonth(sdate.getMonth()+1);
					
				}
				map.put("sum", sum);
				list.add(map);
			}
			ret.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;

	}
	
	
}
