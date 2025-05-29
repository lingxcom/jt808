package com.tracbds.database.thread;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.service.JT808CommonService;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.service.JT808DataService;
import com.lingx.web.ILingxThread;
@Component
public class GpsDataQueueSaveThread implements Runnable,ILingxThread {

	private final static Logger log = LoggerFactory.getLogger(GpsDataQueueSaveThread.class);
	private boolean isRun=true;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService jt808CommonService;
	final List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	@Override
	public void run() {
		long ts=0;
		while(isRun){
			try {
				if(!JT808DataService.LocatoinData.isEmpty()){
					ts=System.currentTimeMillis();
					int tmp=0;
					while(!JT808DataService.LocatoinData.isEmpty()&&tmp<10000){
						Map<String,Object> map=JT808DataService.LocatoinData.poll();
						if(map==null)continue;
						list.add(map);
						tmp++;
					}
					
					jdbcTemplate.batchUpdate(String.format("insert into %s(car_id,acc,location,alarm,status,lat,lng,height,speed,direction,gpstime,systime,mileage,oil,txxh,wxxh,status_str,alarm_str) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", this.jt808CommonService.get0x0200TableName()), 
							new BatchPreparedStatementSetter(){

						@Override
						public int getBatchSize() {
							return list.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int index) throws SQLException {
							Map<String,Object> map=list.get(index);
							if(map.get("speed")==null){
								map.put("speed", "0");
							}
							if(map.get("direction")==null){
								map.put("direction", "0");
							}
							if(map.get("alarm")==null){
								map.put("alarm", "0");
							}
							if(map.get("status")==null){
								map.put("status", "0");
							}
							if(map.get("height")==null){
								map.put("height", "0");
							}
							if(map.get("A01")==null){
								map.put("A01", "0");
							}
							if(map.get("A02")==null){
								map.put("A02", "0");
							}
							if(map.get("A30")==null){
								map.put("A30", "0");
							}
							if(map.get("A31")==null){
								map.put("A31", "0");
							}
							if(map.get("status_str")==null){
								map.put("status_str", "");
							}
							if(map.get("alarm_str")==null){
								map.put("alarm_str", "");
							}
							if(map.get("lat")==null){
								map.put("lat", "0");
							}
							if(map.get("lng")==null){
								map.put("lng", "0");
							}
							long status=Long.parseLong(map.get("status").toString());

							ps.setLong(1,Long.parseLong(map.get("car_id").toString()));
							ps.setString(2, String.valueOf((status&0b01)>0?"1":"0"));
							ps.setString(3, String.valueOf((status&0b10)>0?"1":"0"));
							ps.setLong(4, Long.parseLong(map.get("alarm").toString()));
							ps.setLong(5, Long.parseLong(map.get("status").toString()));
							ps.setDouble(6, Double.parseDouble(map.get("lat").toString()));
							ps.setDouble(7, Double.parseDouble(map.get("lng").toString()));
							ps.setInt(8, new Double(map.get("height").toString()).intValue());
							ps.setDouble(9, Double.parseDouble(map.get("speed").toString()));
							ps.setDouble(10, Double.parseDouble(map.get("direction").toString())%360);
							ps.setString(11, map.get("gpstime").toString());
							ps.setString(12, Utils.getTime());
							ps.setDouble(13, Double.parseDouble(map.get("A01").toString()));
							ps.setDouble(14, Double.parseDouble(map.get("A02").toString()));
							ps.setInt(15, Integer.parseInt(map.get("A30").toString()));
							ps.setInt(16, Integer.parseInt(map.get("A31").toString()));
							ps.setString(17, map.get("status_str").toString());
							ps.setString(18, map.get("alarm_str").toString());
							
						}
						
					});
					if((System.currentTimeMillis()-ts)>1000) {
						log.error("定位数据批量写入过慢:{}ms",System.currentTimeMillis()-ts);
					}
					
				}else {
					Thread.currentThread().sleep(100);
				}
				
			} catch (Exception e) {
				//System.out.println(JSON.toJSONString(list));
				log.error("定位数据批量写入异常:{}",e.getLocalizedMessage());
				e.printStackTrace();
			}finally {
			
				list.clear();
			}
		}
	}
	
	
	@Override
	public String getName() {
		return "Tracbds-Database -> 数据入库线程";
	}


	@Override
	public void startup() {
		new Thread(this,this.getName()).start();
	}


	@Override
	public void shutdown() {
		this.isRun=false;		
	}
}
