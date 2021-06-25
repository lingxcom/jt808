package com.lingx.jt808.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指令下发时的返回值，由于有些地方需要带参数，所以创建此类
 * //0：成功/确认；1：失败；2：消息有误；3：不支持,99：离线
 * @author lingx.com
 *
 */
public class RetBean {

	public RetBean(int code) {
		this(code,new ArrayList<>());
	}
	public RetBean(int code,List<Map<String,Object>>  params) {
		this.code=code;
		this.params=params;
	}
	private int code;
	private List<Map<String,Object>> params;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<Map<String,Object>>  getParams() {
		return params;
	}
	public void setParams(List<Map<String,Object>>  params) {
		this.params = params;
	}
	
}
