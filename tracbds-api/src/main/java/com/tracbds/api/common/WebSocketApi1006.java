package com.tracbds.api.common;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.web.api.IApiChannelHandlerContext;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.tracbds.core.IJT808Cache;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi1006 extends AbstractAuthApi  implements IApiChannelHandlerContext{
	private ChannelHandlerContext ctx;
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		String car_id=params.get("car_id").toString();
		String array[]=car_id.split(",");
		String cid=ctx.channel().id().asLongText();
		if(IJT808Cache.REALTIME_TIDS.getIfPresent(cid)!=null){
			for(String id:array) {
				IJT808Cache.REALTIME_TIDS.getIfPresent(cid).remove(id);
			}
			
		}
		
		return this.getRetMap(1, "SUCCESS");
	}
	@Override
	public int getApiCode() {
		return 1006;
	}
	@Override
	public String getApiName() {
		return "WebSocketApi1006";
	}
	@Override
	public void setChannelHandlerContext(ChannelHandlerContext arg0) {
		this.ctx=arg0;
	}


}
