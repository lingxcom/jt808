package com.tracbds.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.event.JT808OnlineEvent;
@Component
public class OnlineListener implements ApplicationListener<JT808OnlineEvent> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public void onApplicationEvent(JT808OnlineEvent event) {
		this.jdbcTemplate.update("update tgps_car set online='1' where id=?",event.getTid());
	}

}
