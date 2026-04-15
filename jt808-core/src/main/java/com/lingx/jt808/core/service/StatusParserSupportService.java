package com.lingx.jt808.core.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
/**
 * 状态自定义显示管理
 * @author lingx
 *
 */
@Component(value="StatusParserSupportService")
public class StatusParserSupportService {
	public static Cache<String, List<Map<String,Object>>> OIL_CACHE=CacheBuilder.newBuilder().maximumSize(200000).expireAfterAccess(10, TimeUnit.MINUTES).build();
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String getOil(String car_id,String oil) {
		
		float ret=0;
		if(OIL_CACHE.getIfPresent(car_id)==null) {
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select key1,value1 from tgps_oiltype_item where oiltype_id in (select oiltype_id from tgps_car where id=? ) order by key1 asc",car_id);
			OIL_CACHE.put(car_id, list);
		}
		if(OIL_CACHE.getIfPresent(car_id)!=null) {
			 List<Map<String,Object>> list=OIL_CACHE.getIfPresent(car_id);
			 if(list.size()==0)return oil;
			 int index=-1;
			 for(int i=0;i<list.size();i++) {
				 Map<String,Object> m=list.get(i);
				 if(Float.parseFloat(oil)>=Float.parseFloat(m.get("key1").toString())) {
					 index=i;
				 }
			 }
			 if(index>=0) {
				 ret=Float.parseFloat(list.get(index).get("value1").toString());
				 if(index<list.size()-1) {
					 float temp=Float.parseFloat(oil);
					 float temp1=Float.parseFloat(list.get(index).get("key1").toString());
					 float temp2=Float.parseFloat(list.get(index+1).get("key1").toString());
					 float percentage=(temp-temp1)/(temp2-temp1);

					 temp1=Float.parseFloat(list.get(index).get("value1").toString());
					 temp2=Float.parseFloat(list.get(index+1).get("value1").toString());
					 ret=ret+(temp2-temp1)*percentage;
				 }
			 }
		}
		return String.format("%.2f", ret);
	}
}
