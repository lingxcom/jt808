package com.tracbds.database.thread;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.web.ILingxThread;
import com.tracbds.api.common.WebSocketApi1005;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.service.JT808CommonService;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.service.JT808DataService;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
@Component
public class JT8080x0200Handler implements Runnable,ILingxThread {

	private final static Logger log = LoggerFactory.getLogger(JT8080x0200Handler.class);
	private boolean isRun=true;
	@Value("#{configs['jt808.database.jt808x0200.threads']}")
	private String threads = "5";//线程数
	
	@Autowired
	private JT808DataService databaseService;
	//@Autowired
	//private JT808AlarmService alarmService;
	@Autowired
	private JT808CommonService commonService;
	@Override
	public String getName() {
		return "Tracbds-Database -> 0x0200处理服务[位置信息上报]";
	}
		@Override
	public void run() {
			Map<String,Object> map;
		try {
		while(isRun) {
			map=IJT808Cache.GPS_DATA_QUEUE.poll();
				if(map!=null) {
					if(map.get("car_id")==null) {
						log.error("JT8080x0200Handler中car_id为空");
						continue;
					}
					String car_id=map.get("car_id").toString();
					//经纬度为0时，取上一个经纬度点
					double lat=Double.parseDouble(map.get("lat").toString());
					double lng=Double.parseDouble(map.get("lng").toString());
					if(lat>999||lng>999||lat<-999||lng<-999) {
						continue;
					}
					if(lat==0&&Utils.isNotNull(IJT808Cache.get(car_id))) {
						String tempJSON=IJT808Cache.get(car_id);
						Map<String,Object> tempMap=(Map<String,Object>)JSON.parse(tempJSON);
						if(tempMap!=null&&tempMap.get("lat")!=null) {
						map.put("lat", tempMap.get("lat"));
						map.put("lng", tempMap.get("lng"));
						tempMap.clear();
						tempMap=null;
						}else {
							//设置默认经纬度
							map.put("lat", 39.916385);
							map.put("lng", 116.396621);
						}
					}
					//System.out.println(map);
					
					String lastJson=IJT808Cache.get(car_id);
					
					//this.alarmService.saveData(car_id, Long.parseLong(map.get("alarm").toString()), map);
					Map<String,Object> lastMap=null;
					if(lastJson!=null)lastMap=(Map<String,Object>)JSON.parse(lastJson);
					else {
						lastMap=this.commonService.getLast0x0200Data(car_id);
					}
					if(lastMap==null)lastMap=map;
					if(lastMap.get("speed")==null)lastMap.put("speed", 0);
					String lastSpeed=lastMap.get("speed").toString(),cspeed=map.get("speed").toString();
					//System.out.println(lastSpeed +" vs "+cspeed);
					if(0==Float.parseFloat(cspeed)&&0==Float.parseFloat(lastSpeed)||0!=Float.parseFloat(cspeed)&&0!=Float.parseFloat(lastSpeed)) {
						//同停止或同行驶，不记录时间
					}else {
						//行驶或停止变动时间
						IJT808Cache.set(car_id+"_speed_change", Utils.getTime());
					}
					this.databaseService.save0200(map);//(JSON.parseObject(JSON.toJSONString(map)));//
					if(lastMap!=map) {//由于缓存与推送到前端需要部分数据库字段，所以要覆盖下
						lastMap.remove("status_str");//移除旧的状态与报警
						lastMap.remove("alarm_str");//移除旧的状态与报警
						lastMap.putAll(map);
						map=lastMap;
					}
					this.pushWebSocket(car_id,map);//JSON.toJSONString(map);
					IJT808Cache.set(car_id, JSON.toJSONString(map));//设置最新数据，供业务系统调取
					//map.clear();//不清空，其他地方还有引用
					map=null;
				}else {
					Thread.currentThread().sleep(10);
				}
			
			}
		} catch (Exception e) {
			log.error("0x0200入库线程退出。。。");
			e.printStackTrace();
		}
	}
	private void pushWebSocket(String tid,Map<String,Object> map0x0200) {
		//System.out.println("发布定位数据:"+tid);
		Map<String,Set<String>> map=IJT808Cache.REALTIME_TIDS.asMap();
		for(String key:map.keySet()) {
			if(map.get(key).contains(tid)) {
				
				Channel channel=WebSocketApi1005.WEBSOCKET_SESSIONS.getIfPresent(key);
				if(channel!=null) {
					String language=channel.attr(WebSocketApi1005.channel_langugae_key).get();
					this.commonService.addJT808Info(map0x0200,language);
					channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(map0x0200)));
				}
				//IJT808Cache.publish("PUBLISH_"+key, temp);
			}
		}
		
	}
	@Override
	public void startup() {
		int num=Integer.parseInt(this.threads);
		for(int i=0;i<num;i++) {
			new Thread(this,this.getName()).start();
		}
	}
	@Override
	public void shutdown() {
		this.isRun=false;
	}

	

}
