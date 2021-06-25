package com.lingx.jt808;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lingx.jt808.server.netty.JT808Server;

public class App {

	public static ApplicationContext SPRINGCONTEXT=null;
	
	public static void main(String[] args) {
		String configFile="spring.xml" ;
		if(args!=null&&args.length>0){
			configFile=args[0];
		}
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {configFile });
		SPRINGCONTEXT=context;
	}

}
