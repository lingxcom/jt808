package com.lingx.jt808.core.service.statusparser;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.jt808.core.service.StatusParserSupportService;

public abstract class AbstractStatusParser {

	protected JdbcTemplate jdbcTemplate;
	protected StatusParserSupportService statusParserSupportService;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setStatusParserSupportService(StatusParserSupportService statusParserSupportService) {
		this.statusParserSupportService = statusParserSupportService;
	}
	
	
}
