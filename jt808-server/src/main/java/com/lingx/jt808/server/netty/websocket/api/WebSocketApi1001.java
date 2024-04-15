package com.lingx.jt808.server.netty.websocket.api;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
/**
 * 指令下发
 * @author lingx.com
 *
 */
@Component
public class WebSocketApi1001 extends AbstractApi {
	public static final int WAIT_TIME=10;
	public WebSocketApi1001(){
		this.setCmd("1001");
	}
	/**
	 * {"cmd":"1001","tid":"088888888888","content":"123456","ticket":"123123"}
	 * 
	 */
	@Override
	public Map<String, Object> execute(Map<String, Object> param,ChannelHandlerContext ctx) {
		int code=-85;
		String msg="NO_RESPONSE";
		System.out.println(JSON.toJSONString(param));
		try {
			String cmdid="";
			String tid=param.get("tid").toString();
			String content=param.get("content").toString();
			
			System.out.println("response:"+tid);
			Thread.currentThread().sleep(1000);
			for(int i=0;i<WAIT_TIME;i++){
				Integer res=11;///Session.command_response.getIfPresent(tid);
				if(res!=null){
					switch(res){//0：成功/确认；1：失败；2：消息有误；3：不支持
					case 0:
						code=1;
						msg="SUCCESS";
						break;
					case 1:
						code=-80;
						msg="FAIL";
						break;
					case 2:
						code=-81;
						msg="MSG_ERROR";
						break;
					case 3:
						code=-82;
						msg="NO_SUPPORT";
						break;
					default:
						code=-83;
						msg="NONE";
					}
				}else{
					Thread.currentThread().sleep(i*500);
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return this.getRetMap(-98, "NO_ONLINE");
		}
		return this.getRetMap(code, msg);
	};
	
	public String getSubCmd(String hexstring){
		
		return "";
	}
	
	public  String decode(String str) {
		StringBuilder sb = new StringBuilder();
		sb.append("7E");
		for (int i = 2; i < str.length() - 2; i += 2) {
			// ////System.out.println(str.substring(i,i+4));
			switch (Integer.parseInt(str.substring(i, i + 4), 16)) {
			case 0x7D01:
				sb.append("7D");
				i += 2;
				break;
			case 0x7D02:
				sb.append("7E");
				i += 2;
				break;
			default:
				sb.append(str.substring(i, i + 2));
			}
		}
		sb.append(str.substring(str.length() - 2));

		return sb.toString();

	}

}
