package com.tracbds.core.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracbds.core.IJT808Rule;
import com.tracbds.core.bean.FenceBean;
import com.tracbds.core.bean.Point;
import com.tracbds.core.utils.FenceUtils;

public class FenceRule implements IJT808Rule{
	private  Cache<String,Integer> LAST_NUM_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(15, TimeUnit.MINUTES).build();
	private ApplicationContext spring;
	private List<FenceBean> listFence;
	private int type=0;//触发方式,1为出围栏报警，2为进围栏报警，3为出入报警
	@Override
	public void init(Map<String, Object> map) {
		JdbcTemplate jdbc=this.spring.getBean(JdbcTemplate.class);
		if(map.containsKey("ids")) {
			List<Map<String,Object>>listTemp1=jdbc.queryForList("select t.* from tgps_pls t where t.id in("+map.get("ids").toString()+")");
			this.listFence=this.getFenceBeans(listTemp1);
		}
		if(map.containsKey("type"))this.type=Integer.parseInt(map.get("type").toString());
		
	}

	@Override
	public boolean match(Map<String, Object> map0x0200) {
		if(listFence==null)return false;
		if(type==0)return false;
		Map<String,Object> map=map0x0200;
		Point point=new Point(Double.parseDouble(map.get("lng").toString()),Double.parseDouble(map.get("lat").toString()));
		int inNum=0;
		for(FenceBean bean:this.listFence) {
			if(inFence(point,bean)) {
				inNum++;
			}
		}
		if(type==1) {//1为出围栏报警
			if(inNum==0)return true;//当坐标不在任何一个围栏里，就报警
		}else if(type==2){//2为进围栏报警
			if(inNum>0)return true;//当坐标在任何一个围栏里，就报警
		}else if(type==3){//3为进出围栏报警
			String key=map.get("car_id").toString();
			if(LAST_NUM_CACHE.getIfPresent(key)!=null) {
				int preNum=LAST_NUM_CACHE.getIfPresent(key);
				LAST_NUM_CACHE.put(key, inNum);
				if(preNum==0&&inNum>0)return true;//进围栏
				if(preNum>0&&inNum==0)return true;//出围栏
			}else {
				LAST_NUM_CACHE.put(key, inNum);
			}
			
		}
		
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}
	private List<FenceBean> getFenceBeans(List<Map<String,Object>> listTemp1){
		List<FenceBean> list=new ArrayList<>();
		for(Map<String,Object> temp1:listTemp1) {
			FenceBean bean=new FenceBean();
			if("3".equals(temp1.get("type").toString())) {
				List<Point> list1=new ArrayList<>();
				list1.add(FenceUtils.getPoint(temp1.get("latlngs").toString()));
				bean.setPoints(list1);
			}else {
				bean.setPoints(FenceUtils.getPoints(temp1.get("latlngs").toString()));
			}
			bean.setId(temp1.get("id").toString());
			bean.setName(temp1.get("name").toString());

			bean.setType(Integer.parseInt(temp1.get("type").toString()));
			if(!"".equals(temp1.get("radius").toString()))
			bean.setRadius(Integer.parseInt(temp1.get("radius").toString()));
			list.add(bean);
		}
		return list;
	}
	/**
	 * 是否在围栏内
	 * @param point
	 * @param bean
	 * @return
	 */
	public boolean inFence(Point point,FenceBean bean) {
		boolean b=false;
		switch(bean.getType()) {
		case 1://面
			b=FenceUtils.rayCasting(point, bean.getPoints());
			break;
		case 2://线
			b=FenceUtils.getShortestDistance(point, bean.getPoints())<=bean.getRadius();
			break;
		case 3://点
			b=FenceUtils.distance(bean.getPoints().get(0), point)<=bean.getRadius();
			break;
		}
		
		return b;
	}
}
