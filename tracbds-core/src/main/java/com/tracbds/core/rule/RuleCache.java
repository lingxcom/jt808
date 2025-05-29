package com.tracbds.core.rule;

import java.util.concurrent.atomic.AtomicInteger;

public class RuleCache {

	private AtomicInteger total;
	private AtomicInteger action;
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
