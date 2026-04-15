package com.lingx.jt808.core.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.IJT808Command;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;

@Component("JT808CommandService")
public class JT808CommandService {
	private final static Logger log = LoggerFactory.getLogger(JT808CommandService.class);
	@Value("#{configs['gps.cmd.send.waittime']}")
	private String waitTime="5";//下发指令后的等待时间，秒
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RedisService redisService;

	public Map<String,Object> sendCommand(IJT808Command cmd,String carId,String userid,boolean isLog){
		Map<String,Object> ret=null;
		String tid=cmd.getTid();
		String content=cmd.toMessageHexstring();
		int msgId=cmd.getMsgId();
		int msgSn=cmd.getMsgSn();
		String json=this.redisService.get(carId);//IJT808Cache.get(carId);
		if(json==null) {
			System.out.println("JT808CommandService下发指令时carId传值可能有误(应该是int):"+carId);
		}
		try {
			if(json!=null) {
				Map<String,Object> last=(Map<String,Object>)JSON.parse(json);
				tid=Utils.parseTid(tid);
				if(!last.containsKey(Constants.JT808_SERVER_ID)) {
					ret=new HashMap<String,Object>();
					ret.put("code", 9);
					ret.put("message", "终端设备不在线");
					return ret;
				}
				String serverId=last.get(Constants.JT808_SERVER_ID).toString();
				this.redisService.push(serverId, this.getJSONString(tid, content, msgId, msgSn));
				//	IJT808Cache.SEND_COMMAND_QUEUE.add( this.getJSONString(tid, content, msgId, msgSn));

					String key=JT808Utils.getResKey(tid, msgId, msgSn);//tid_msgSn
					String key2=JT808Utils.getResKey2(tid, msgId, msgSn);//tid_msgId
					for(int i=0;i<Integer.valueOf(waitTime);i++) {
						//System.out.println(i+"->key:"+key+",key2:"+key2);
						json=this.redisService.get(key);
						if(Utils.isNull(json)) {
							json=this.redisService.get(key2);
						}
						if(json!=null) {
							this.redisService.del(key);
							this.redisService.del(key2);
							ret=(Map<String,Object>)JSON.parse(json);
							if(ret.get("code")!=null&&"9".equals(ret.get("code").toString()))continue;//如果是网关下发的不在线，可能是暂时离线，继续循环
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
	public Map<String,Object> sendCommand(String cmdId,String carId,String userId,Map<String,Object> param){
		IJT808Command cmd=null;
		Map<String, Object> ret=null;
		String json=this.redisService.get(carId);
		if(json==null) {
			ret=new HashMap<String,Object>();
			ret.put("code", 9);
			ret.put("message", "终端设备不在线");
			return ret;
		}
		String clazzName=this.jdbcTemplate.queryForObject("select clazz_path from tgps_cmd where id=?", String.class,cmdId);
		try {
			Map<String,Object> last=(Map<String,Object>)JSON.parse(json);
			Class<?> clazz=Class.forName(clazzName);
			String tid=this.getTidByCarid(carId);
			cmd=(IJT808Command)clazz.getConstructors()[0].newInstance(new Object[]{tid,param});
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
	/**
	 * 获取12位或20位的设备号
	 * @param tid
	 * @return
	 */
	
	public String getTidByCarid(String carid) {
		String json=redisService.get(carid);
		if(json==null) {
			return null;
		}
		Map<String,Object> last=(Map<String,Object>)JSON.parse(json);
		boolean isVersion=false;
		String tid="";
		if(last.containsKey("isVersion")&&"true".equals(last.get("isVersion").toString()))isVersion=true;
		if(last.containsKey("tid"))tid=last.get("tid").toString();
		if(isVersion) {
			tid=Utils.leftAdd0(tid, 20);
		}else {
			tid=Utils.leftAdd0(tid, 12);
		}
		return tid;
	}
	
	public String getTidByVersion(String tid,boolean isVersion) {
		
		return JT808Utils.getTidByVersion(tid, isVersion);
	}
	
}
