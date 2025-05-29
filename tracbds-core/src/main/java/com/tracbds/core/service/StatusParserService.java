package com.tracbds.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.tracbds.core.IJT808StatusParser;
import com.tracbds.core.bean.AttachedBean0x0200;
import com.tracbds.core.service.statusparser.LocatedStatusParser;
import com.tracbds.core.service.statusparser.StatusParseBean;
import com.tracbds.core.utils.JT808Utils;
/**
 * 状态自定义显示管理
 * @author lingx
 *
 */
@Component(value="StatusParserService")
public class StatusParserService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Map<String,IJT808StatusParser> cacheParserMap=new HashMap<>();
	private List<StatusParseBean> listParser=new ArrayList<>();
	private LocatedStatusParser locatedStatusParser =new LocatedStatusParser();
	@PostConstruct
	public void init() {
		String sql="select car_ids,status_ids from tgps_status_inst where status='1'";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		listParser.clear();
		for(Map<String,Object> map:list) {
			StatusParseBean bean=getStatusParseBean(map);
			listParser.add(bean);
		}
	}
	public void reload() {
		this.init();
	}
	
	public void parse(Map<String,Object> map0x0200,List<AttachedBean0x0200> listAttached) {
		try {
			String temp=locatedStatusParser.parse(map0x0200, listAttached);
			if(temp!=null) {
				JT808Utils.putMap("status_str", temp, map0x0200);
			}
			String car_id=map0x0200.get("car_id").toString();
			for(StatusParseBean bean:listParser) {
				if(bean.getCarIdSet().contains(car_id)) {
					for(IJT808StatusParser parser:bean.getParserSet()) {
						temp=parser.parse(map0x0200, listAttached);
						if(temp!=null) {
							JT808Utils.putMap("status_str", temp, map0x0200);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private StatusParseBean getStatusParseBean(Map<String,Object> map) {
		StatusParseBean bean=new StatusParseBean();
		String car_ids=map.get("car_ids").toString();
		bean.setCarIdSet(Sets.newHashSet(car_ids.split(",")));
		
		String status_ids=map.get("status_ids").toString();
		Set<String> set=Sets.newHashSet(status_ids.split(","));
		Set<IJT808StatusParser> parserSet=new HashSet<>();
		IJT808StatusParser parser=null;
		for(String id:set) {
			parser=getParser(id);
			if(parser!=null)parserSet.add(parser);
		}
		bean.setParserSet(parserSet);
		return bean;
	}
	
	private IJT808StatusParser getParser(String id) {
		if(cacheParserMap.containsKey(id))return cacheParserMap.get(id);
		IJT808StatusParser parser=null;
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select clazz from tgps_status where id=? and status='1'",id);
		if(list.size()>0) {
			try {
				String clazzName=list.get(0).get("clazz").toString();
				Class<?> clazz=Class.forName(clazzName.trim());
				parser=(IJT808StatusParser)clazz.getConstructors()[0].newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cacheParserMap.put(id, parser);
		return parser;
	}
}
