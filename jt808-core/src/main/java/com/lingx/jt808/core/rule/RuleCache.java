package com.lingx.jt808.core.rule;

import java.util.concurrent.atomic.AtomicInteger;

public class RuleCache {
	/**
	 * 报警触发总次数
	 */
	private AtomicInteger total;
	/**
	 * 执行动作的次数，因为有最小持续时间的存在，触发次数不等于执行动作次数
	 */
	private AtomicInteger action;
	/**
	 * 报警触发时间
	 */
	private long stime;
	
	public RuleCache() {
		super();
		this.total = new AtomicInteger();
		this.action = new AtomicInteger();
		this.stime = System.currentTimeMillis();
	}
	public AtomicInteger getTotal() {
		return total;
	}
	public AtomicInteger getAction() {
		return action;
	}
	public long getStime() {
		return stime;
	}
	
}
