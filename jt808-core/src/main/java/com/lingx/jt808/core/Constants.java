package com.lingx.jt808.core;

public interface Constants {
	/**
	 * 通过redis的订阅发布来做为应用间的通知，订阅名称为SystemCommandByRedis
	 * 3001 退出进程
	 * 3002 重载JT808转发信息
	 * 3003 打开实时报文开关
	 * 3004 关闭实时报文开关
	 * 3005 刷新JT808网关白名单
	 * 3006 刷新StatusParser
	 * 3007 刷新Rule
	 */
	public static final String TOPIC_SYSTEM_COMMAND="TOPIC_SYSTEM_COMMAND";
	/**
	 * 订阅发布，实时位置
	 */
	public static final String TOPIC_GPS_DATA="TOPIC_GPS_DATA";
	/**
	 * 实时报文
	 */
	public static final String TOPIC_HEXSTRING_DATA="TOPIC_HEXSTRING_DATA";
	
	public static final String JT808_0200_DATA="JT808_0200_DATA";
	
	public static final String JT808_0704_DATA="JT808_0704_DATA";
	
	/**
	 * 升级结果通知
	 */
	public static final String JT808_0108_DATA="JT808_0108_DATA";

	/**
	 * 电子运单数据上传
	 */
	public static final String JT808_0701_DATA="JT808_0701_DATA";
	/**
	 * 驾驶员身份信息采集上报
	 */
	public static final String JT808_0702_DATA="JT808_0702_DATA";
	/**
	 * CAN数据上报
	 */
	public static final String JT808_0705_DATA="JT808_0705_DATA";
	/**
	 * 多媒体事件信息上传
	 */
	public static final String JT808_0800_DATA="JT808_0800_DATA";
	/**
	 * 媒体文件上报
	 */
	public static final String JT808_0801_DATA="JT808_0801_DATA";
	
	/**
	 * 指令下发通道的服务ID
	 */
	public static final String JT808_SERVER_ID="JT808ServerId";
	
	
	
	
}
