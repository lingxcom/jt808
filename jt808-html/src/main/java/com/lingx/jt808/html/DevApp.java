package com.lingx.jt808.html;

import com.lingx.jt808.html.netty.HttpServer;

public class DevApp {

	public static void main(String args[]) {
		new Thread(new HttpServer(8800)).start();
	}
}
