package com.lingx.jt808.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lingx.service.StartupService;

public class DevApp {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring_dev.xml" });
		StartupService bean = context.getBean(StartupService.class);
		bean.startup();
		
	}

}
