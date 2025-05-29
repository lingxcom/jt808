package com.tracbds.core.bean;
/**
 * 指令下发时的返回值，由于有些地方需要带参数，所以创建此类
 * //0：成功/确认；1：失败；2：消息有误；3：不支持,99：离线
 * @author lingx.com
 *
 */
public class RetBean {

	public RetBean(int code) {
		this(code,"");
	}
	public RetBean(int code,String params) {
		this.code=code;
		this.params=params;
		this.message="操作成功";
	}	
	public RetBean(int code,String message,String params) {
		this.code=code;
		this.params=params;
		this.message=message;
	}
	private int code;
	private String message;
	private String params;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
