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
public class Api1121 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupService groupService;
	@Override
	public int getApiCode() {
		return 1121;
	}
	@Override
	public String getApiName() {
		return "里程统计(天)";
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
		
		try {
			List<Map<String,Object>> list=new ArrayList<>();
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");
			Integer dtlc=0,sum,ccts,zts;//总天数
			for(String tid:sets) {
				sum=0;ccts=0;zts=0;
				Map<String,Object> map=new HashMap<>();
				map.putAll(this.jdbcTemplate.queryForMap("select tid,carno from tgps_car where id=?",tid));
				map.put("group", this.jdbcTemplate.queryForObject("select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=?)", String.class,tid));
				Date sdate=sdf2.parse(stime);
				Date edate=sdf2.parse(etime);
				String key;
				while(sdate.getTime()<=edate.getTime()) {
					zts++;
					key=sdf.format(sdate);
					dtlc=this.jdbcTemplate.queryForObject("select sum(dtlc) from tgps_mileage where car_id=? and create_time=?", Integer.class,tid,sdf2.format(sdate));
					if(dtlc==null)dtlc=0;
					map.put(key, dtlc);
					sum+=dtlc;
					if(dtlc>0)ccts++;
					
					if(map.get(key)==null)map.put(key, 0);
					sdate.setDate(sdate.getDate()+1);
					
				}
				map.put("sum", sum);
				map.put("ccts", ccts);
				map.put("ccl", String.format("%.2f", 1f*ccts/zts*100)+"%");
				list.add(map);
			}
			ret.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;

	}
	
	
}
