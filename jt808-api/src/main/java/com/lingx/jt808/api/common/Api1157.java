package com.lingx.jt808.api.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.service.AddressService;
@Component
public class Api1157 extends AbstractAuthApi implements IApi{
	@Autowired
	private AddressService addressService;
	@Override
	public int getApiCode() {
		return 1157;
	}
	@Override
	public String getApiName() {
		return "根据地心坐标获取地址信息";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(this.checkIsNull(params, "lat",ret))return ret; 
		if(this.checkIsNull(params, "lng",ret))return ret; 
		String lat=this.getParamString(params, "lat", "");
		String lng=this.getParamString(params, "lng", "");
		String ext=this.getParamString(params, "ext", "");//附加数据，例如ID，下标，返回前端方便正确展示
		ret.put("data", this.addressService.getAddress(lat, lng));
		ret.put("ext", ext);
		return ret;

	}
	public boolean isLog() {
		return false;
	}
	
}
