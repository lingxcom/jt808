package com.lingx.jt808.api.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.service.LingxService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1118 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Override
	public int getApiCode() {
		return 1118;
	}
	@Override
	public String getApiName() {
		return "授权管理";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		String type=IApi.getParamString(params, "type", "1");
		String appkey=IApi.getParamString(params, "appkey", "");
		if("2".equals(type)) {
			if(!this.lingxService.getAuthCheck(appkey)) {
				ret.put("code", -1);
				ret.put("message", "无效的授权码");
				return ret;
			}
			try {
				File file=new File(System.getProperty("user.dir")+"/conf/appkey.key");
				FileWriter fw=new FileWriter(file);
				fw.write(appkey);
				fw.flush();
				fw.close();
				this.lingxService.getAuthReload();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("code", this.lingxService.getAuthCode());
		data.put("etime", this.lingxService.getAuthMap().get("etime"));
		data.put("deviceCount", this.lingxService.getAuthMap().get("deviceCount"));
		data.put("videoCount", this.lingxService.getAuthMap().get("videoCount"));
		ret.put("data", data);
		return ret;
	}
	

}
