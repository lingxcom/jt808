package com.lingx.jt808.api.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.service.RedisService;
@Component
public class Api1115 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RedisService redisService;
	@Override
	public int getApiCode() {
		return 1115;
	}
	@Override
	public String getApiName() {
		return "关注/取关车辆";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "deviceId",ret))return ret; 
		String type=IApi.getParamString(params, "type", "add");
		String deviceId=IApi.getParamString(params, "deviceId", "");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		Map<String,Object> map=JSON.parseObject(this.redisService.get(deviceId));
		if("add".equals(type)) {
			this.jdbcTemplate.update("insert into tgps_car_follow(car_id,user_id) values(?,?)",deviceId,userid);
			map.put("follow", 1);
		}else {
			this.jdbcTemplate.update("delete from tgps_car_follow where car_id=? and user_id=?",deviceId,userid);
			map.put("follow", 0);
		}
		String json=JSON.toJSONString(map);
		this.redisService.publish(Constants.TOPIC_GPS_DATA, json);
		this.redisService.set(deviceId, json);
		return ret;
	}

	public boolean isLog() {
		return false;
	}

}
