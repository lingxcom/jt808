package com.tracbds.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tracbds.server.service.JT808ServerConfigService;
import com.lingx.service.ConfigService;
import com.lingx.service.StartupService;

public class App {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"application.xml" });
		 StartupService bean=context.getBean(StartupService.class);
		 bean.startup();
	}

}
