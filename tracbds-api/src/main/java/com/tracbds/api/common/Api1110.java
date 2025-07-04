package com.tracbds.api.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.utils.HttpUtils;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.cmd.Cmd9101;
import com.tracbds.core.cmd.Cmd9102;
import com.tracbds.core.service.DatabaseConfigService;
import com.tracbds.core.service.JT808CommandService;
import com.tracbds.core.support.JT808SendCommandThread;
@Component
public class Api1110 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommandService commandService;
	@Autowired
	private DatabaseConfigService databaseConfigService;
	
	@Override
	public int getApiCode() {
		return 1110;
	}
	@Override
	public String getApiName() {
		return "实时视频直播0x9101";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		try {//deviceId:this.deviceId,channel:this.channel,mltype:this.mltype
			String token=IApi.getParamString(params, "lingxtoken", "");
			String car_id=IApi.getParamString(params, "car_id", "");
			String tdh=IApi.getParamString(params, "channel", "");
			String mltype=IApi.getParamString(params, "mltype", "");
			String stop=IApi.getParamString(params, "stop", "");
			String userid=TokenUtils.getTokenDataUserId(token);
			String apiAddress=databaseConfigService.getConfigValue("jt1078.server.api.address", "http://127.0.0.1:6809/api");
			String apiSecret =databaseConfigService.getConfigValue("jt1078.server.api.secret", "43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae");
			String type="1";
			String pushAddress=databaseConfigService.getConfigValue("jt1078.server.push.address", "127.0.0.1:6802");
			String pullAddress=databaseConfigService.getConfigValue("jt1078.server.pull.address", "ws://127.0.0.1:6899/websocket");
			boolean isVersion=false;
			
			String json=IJT808Cache.get(car_id);
			if(json==null) {
				ret=new HashMap<String,Object>();
				ret.put("code", 9);
				ret.put("message", "终端设备不在线");
				return ret;
			}
				Map<String,Object> last=(Map<String,Object>)JSON.parse(json);
				if(last.containsKey("isVersion")&&"true".equals(last.get("isVersion").toString()))isVersion=true;
			String tid=last.get("tid").toString();
			if("1".equals(stop)) {//停止播放
				this.stop(tid, tdh, type, apiAddress, apiSecret);
				Cmd9102 cmd=new Cmd9102(tid,Integer.parseInt(tdh),0,isVersion);
				JT808SendCommandThread thread=new JT808SendCommandThread(cmd,car_id,userid,this.commandService);
				new Thread(thread).start();
				return ret;
			}
			
			//当不在直播时下发直播指令，有些设备如果重复下发播放指令会异常
			boolean b=this.isLive(tid, tdh, type, apiAddress, apiSecret);
			String send="no";
			if(!b) {
				String array[]=pushAddress.split(":");
				Cmd9101 cmd=new Cmd9101(tid,array[0],Integer.parseInt(array[1]),
						Integer.parseInt(tdh),0,Integer.parseInt(mltype),isVersion);
				JT808SendCommandThread thread=new JT808SendCommandThread(cmd,car_id,userid,this.commandService);
				new Thread(thread).start();
				send="yes";
			}
			ret.put("tid", tid);	
			ret.put("tdh", tdh);	
			ret.put("isLive", b);	
			ret.put("send9101", send);	
			ret.put("pull_address", addJT1078Params(pullAddress,tid,tdh,token));
			ret.put("pull_address_audio", addJT1078ParamsAudio(pullAddress,tid,tdh,token));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", -1);
			ret.put("message", "操作失败，参数无效不能执行！");
			return ret;
		}
	}
	private static String addJT1078Params(String address,String deviceId,String channel,String token) {
		deviceId="0000000000"+deviceId;
		String template="/realtime_video/%s/%s/%s/1/%s";
		if(deviceId.length()>12)deviceId=deviceId.substring(deviceId.length()-12);
		return address.replace("/websocket", String.format(template, deviceId,channel,1,token));
	}
	private static String addJT1078ParamsAudio(String address,String deviceId,String channel,String token) {
		deviceId="0000000000"+deviceId;
		String template="/realtime_video/%s/%s/%s/2/%s";
		if(deviceId.length()>12)deviceId=deviceId.substring(deviceId.length()-12);
		return address.replace("/websocket", String.format(template, deviceId,channel,1,token));
	}
	public boolean isLive(String tid,String tdh,String type,String apiAddress,String apiSecret) {
		Map<String,String> param =new HashMap<>();
		param.put("cmd", "1001");
		param.put("tid", tid);
		param.put("tdh", tdh);
		param.put("type", type);
		param.put("ticket",apiSecret);
		String apiUrl=apiAddress+"?json="+JSON.toJSONString(param);
		System.out.println(apiUrl);
		String jsonRet=HttpUtils.get(apiUrl);
		System.out.println(jsonRet);
		Map<String,Object> ret=(Map<String,Object>)JSON.parseObject(jsonRet);
		return "true".equals(ret.get("result").toString());
	}
	
	public void stop(String tid,String tdh,String type,String apiAddress,String apiSecret) {
		Map<String,String> param =new HashMap<>();
		param.put("cmd", "1004");
		param.put("tid", tid);
		param.put("tdh", tdh);
		param.put("type", type);
		param.put("ticket",apiSecret);
		String apiUrl=apiAddress+"?json="+JSON.toJSONString(param);
		System.out.println(apiUrl);
		String jsonRet=HttpUtils.get(apiUrl);
		System.out.println(jsonRet);
		//Map<String,Object> ret=(Map<String,Object>)JSON.parseObject(jsonRet);
		//return "true".equals(ret.get("result").toString());
	}
	
}
