package com.tracbds.core.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.Constants;
import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808Command;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;

@Component("JT808CommandService")
public class JT808CommandService {
	private final static Logger log = LoggerFactory.getLogger(JT808CommandService.class);
	@Value("#{configs['gps.cmd.send.waittime']}")
	private String waitTime="5";//下发指令后的等待时间，秒
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String,Object> sendCommand(IJT808Command cmd,String carId,String userid,boolean isLog){
		Map<String,Object> ret=null;
		String tid=cmd.getTid();
		String content=cmd.toMessageHexstring();
		int msgId=cmd.getMsgId();
		int msgSn=cmd.getMsgSn();
		String json=IJT808Cache.get(carId);
		try {
			if(json!=null) {
				while (tid.charAt(0) == '0') {
					tid = tid.substring(1);
				}
					IJT808Cache.SEND_COMMAND_QUEUE.add( this.getJSONString(tid, content, msgId, msgSn));

					String key=JT808Utils.getResKey(tid, msgId, msgSn);//tid_msgSn
					String key2=JT808Utils.getResKey2(tid, msgId, msgSn);//tid_msgId
					for(int i=0;i<Integer.valueOf(waitTime);i++) {
						//System.out.println(i+"->key:"+key+",key2:"+key2);
						json=IJT808Cache.cache(key);
						if(Utils.isNull(json)) {
							json=IJT808Cache.cache(key2);
						}
						if(json!=null) {
							IJT808Cache.cacheDel(key);
							IJT808Cache.cacheDel(key2);
							ret=(Map<String,Object>)JSON.parse(json);
							if("9".equals(ret.get("code").toString()))continue;//如果是网关下发的不在线，可能是暂时离线，继续循环
							//ret.put("message", "SUCCESS");
							break;
						}
						Thread.currentThread().sleep(1000);
					}
					if(ret==null) {
						ret=new HashMap<String,Object>();
						ret.put("code", 8);
						ret.put("message", "终端设备无回复");
					}
					
					if(isLog)
					this.jdbcTemplate.update("insert into tgps_car_cmd(car_id,cmd_id,user_id,ts,cmd_str) values(?,?,?,?,?)",carId,content.substring(2, 6),userid,Utils.getTime(),content);
					
				
			}else {
				ret=new HashMap<String,Object>();
				ret.put("code", 9);
				ret.put("message", "终端设备不在线");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 向终端发送指令
	 * @param cmdId 指令ID
	 * @param tid 终端ID
	 * @param param 扩展参数
	 * @return
	 */
	public Map<String,Object> sendCommand(String cmdId,String carId,String tid,String userId,Map<String,Object> param){
		IJT808Command cmd=null;
		Map<String, Object> ret=null;
		boolean isVersion=false;
		String json=IJT808Cache.get(carId);
		if(json==null) {
			ret=new HashMap<String,Object>();
			ret.put("code", 9);
			ret.put("message", "终端设备不在线");
			return ret;
		}
		String clazzName=this.jdbcTemplate.queryForObject("select clazz_path from tgps_cmd where id=?", String.class,cmdId);
		try {
			Map<String,Object> last=(Map<String,Object>)JSON.parse(json);
			if(last.containsKey("isVersion")&&"true".equals(last.get("isVersion").toString()))isVersion=true;
			Class<?> clazz=Class.forName(clazzName);
			cmd=(IJT808Command)clazz.getConstructors()[0].newInstance(new Object[]{tid,param,isVersion});
			ret=this.sendCommand(cmd,carId, userId, true);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private String getJSONString(String tid,String cmd,int msgId,int msgSn) {
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("tid", tid);
		ret.put("cmd", cmd);
		ret.put("msgId", msgId);
		ret.put("msgSn", msgSn);
		return JSON.toJSONString(ret);
	}
	
}
