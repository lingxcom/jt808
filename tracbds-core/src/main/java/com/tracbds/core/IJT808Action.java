package com.tracbds.core;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.tracbds.core.rule.RuleEvent;
/**
 * 系统动作接口，比如写入数据库、消息推送、下发拍照指令、下发TTS指令
 * @author lingx
 *
 */
public interface IJT808Action {
	/**
	 * 初始化参数，比如写入数据的报警名称叫什么、消息推送的内容模板、下发拍照的摄像头通道号、下发TTS的文本信息
	 * @param map
	 */
	public void init(Map<String,Object> map);
	/**
	 * 根据参数执行具体动作
	 * @param map0x0200
	 * @param count 连续触发几次，从1开始
	 */
	public void exec(Map<String,Object> map0x0200,RuleEvent ruleEvent);
	/**
	 * 设置Spring环境
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext);
}
