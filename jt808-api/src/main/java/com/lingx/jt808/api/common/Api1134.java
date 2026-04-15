package com.lingx.jt808.api.common;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.cmd.Cmd8107;
import com.lingx.jt808.core.service.JT808CommandService;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;
@Component
public class Api1134 extends AbstractAuthApi implements IApi{
	private final static Logger log = LoggerFactory.getLogger(Api1134.class);
	@Autowired
	private JT808CommandService commandService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getApiCode() {
		return 1134;
	}
	@Override
	public String getApiName() {
		return "查询终端属性";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "deviceId",ret))return ret; 
		String carid=this.getParamString(params, "deviceId", "");
		String tid=this.commandService.getTidByCarid(carid);
		Cmd8107 cmd=new Cmd8107(tid);
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenData(token).getUserId();
		Map<String,Object> data=commandService.sendCommand(cmd,carid,userid,false);
		ret.put("data", data);
		return ret;

	}
}
