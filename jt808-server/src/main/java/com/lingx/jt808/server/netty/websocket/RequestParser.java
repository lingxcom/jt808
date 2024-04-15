package com.lingx.jt808.server.netty.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class RequestParser {
	private HttpRequest fullReq;

	/**
	 * 构造一个解析器
	 * 
	 * @param req
	 */
	public RequestParser(HttpRequest req) {
		this.fullReq = req;
	}

	/**
	 * 解析请求参数
	 * 
	 * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
	 *
	 * @throws BaseCheckedException
	 * @throws IOException
	 */
	public Map<String, String> parse() throws  IOException {
		HttpMethod method = fullReq.getMethod();

		Map<String, String> parmMap = new HashMap<String, String>();

		if (HttpMethod.GET == method) {
			// 是GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(fullReq.getUri());
			
			for(Map.Entry<String,List<String>> entry:decoder.parameters().entrySet()){
				parmMap.put(entry.getKey(), entry.getValue().get(0));
			}
			
		} else if (HttpMethod.POST == method) {
			// 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
			decoder.offer((FullHttpRequest)fullReq);

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

			for (InterfaceHttpData parm : parmList) {

				Attribute data = (Attribute) parm;
				parmMap.put(data.getName(), data.getValue());
			}

		} else {
			// 不支持其它方法
			//throw new MethodNotSupportedException(""); // 这是个自定义的异常, 可删掉这一行
		}

		return parmMap;
	}
}
