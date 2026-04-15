package com.lingx.jt808.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lingx.service.StartupService;

public class App {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"spring.xml" });
		 StartupService bean=context.getBean(StartupService.class);
		 bean.startup();
	}

}
