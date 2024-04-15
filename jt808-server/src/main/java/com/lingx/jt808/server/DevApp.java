package com.lingx.jt808.server;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lingx.jt808.core.IJT808ThreadService;

public class DevApp {
	public static ApplicationContext SPRINGCONTEXT = null;

	public static void main(String[] args) {
		String configFile = "spring_server_dev.xml";
		if (args != null && args.length > 0) {
			configFile = args[0];
		}
		ApplicationContext spring = new ClassPathXmlApplicationContext(new String[] { configFile });
		SPRINGCONTEXT = spring;

		Map<String, IJT808ThreadService> services = spring.getBeansOfType(IJT808ThreadService.class);
		for(IJT808ThreadService service :services.values()) {
			System.out.println("正在启动【"+service.getName()+"】...");
			new Thread((Runnable)service).start();;
		}
		
	}

}
