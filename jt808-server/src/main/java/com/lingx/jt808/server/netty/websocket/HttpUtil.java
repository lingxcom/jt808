package com.lingx.jt808.server.netty.websocket;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class HttpUtil {

	public static void setContentLength(FullHttpResponse res,int len){}
	
	public static boolean isKeepAlive(FullHttpRequest req){return false;}
}
