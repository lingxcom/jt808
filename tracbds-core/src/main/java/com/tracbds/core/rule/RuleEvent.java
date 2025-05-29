package com.tracbds.core.rule;

public class RuleEvent {
	
	public RuleEvent(String name,long total, long duration, long action) {
		super();
		this.name = name;
		this.total = total;
		this.duration = duration;
		this.action = action;
	}
	/**
	 * 报警名称，tgps_rule_inst.name
	 */
	private String name;
	/**
	 * 事件触发的总次数
	 */
	private long total;
	/**
	 * 持续时长阀值
	 * 事件触发的总时长(秒)
	 */
	private long duration;
	/**
	 * 该次事件执行动作的次数累计，从1开始
	 */
	private long action;
	
	public String getName() {
		return name;
	}
	public long getTotal() {
		return total;
	}
	public long getDuration() {
		return duration;
	}
	public long getAction() {
		return action;
	}
	
	
}
