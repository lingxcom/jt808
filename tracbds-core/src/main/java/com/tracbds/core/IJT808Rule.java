package com.tracbds.core;

import java.util.Map;

import org.springframework.context.ApplicationContext;
/**
 * 规则抽象接口，比如是否超速、是否疲劳驾驶
 * @author lingx
 *
 */
public interface IJT808Rule {
	/**
	 * 初始化规则参数，比如超速阀值，不同的车辆可能需要不同的参数值
	 * @param map
	 */
	public void init(Map<String,Object> map);
	/**
	 * 是否符合规则，false是不符合，时速在阀值之下；true是符合超速规则,时速在阀值之上，触发动作Action接口
	 * @param map0x0200
	 * @return
	 */
	public boolean match(Map<String,Object> map0x0200);
	/**
	 * 设置Spring环境
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext);
	
}
