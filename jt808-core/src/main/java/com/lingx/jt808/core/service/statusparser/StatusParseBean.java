package com.lingx.jt808.core.service.statusparser;

import java.util.Set;

import com.lingx.jt808.core.IJT808StatusParser;

public class StatusParseBean {
	private Set<String> carIdSet;
	private Set<IJT808StatusParser> parserSet;
	public Set<String> getCarIdSet() {
		return carIdSet;
	}
	public void setCarIdSet(Set<String> carIdSet) {
		this.carIdSet = carIdSet;
	}
	public Set<IJT808StatusParser> getParserSet() {
		return parserSet;
	}
	public void setParserSet(Set<IJT808StatusParser> parserSet) {
		this.parserSet = parserSet;
	}
	
}
