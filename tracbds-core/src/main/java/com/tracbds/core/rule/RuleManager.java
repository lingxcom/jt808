package com.tracbds.core.rule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracbds.core.IJT808Action;
import com.tracbds.core.IJT808Rule;
import com.tracbds.core.event.JT808Location0200Event;
import com.tracbds.core.service.ThreadPoolExecutorService;
@Component(value="RuleManager")
public class RuleManager implements ApplicationListener<JT808Location0200Event>{
	private final static Logger log = LoggerFactory.getLogger(RuleManager.class);
	private static Cache<String, RuleCache> RULE_CACHE=CacheBuilder.newBuilder().maximumSize(100000)
			.expireAfterAccess(10, TimeUnit.MINUTES).build();
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ApplicationContext spring;
	@Autowired
	private ThreadPoolExecutorService threadPoolExecutorService;
	private List<RuleBean> listRule=new CopyOnWriteArrayList<>();
	
	@Override
	public void onApplicationEvent(JT808Location0200Event event) {
		//System.out.println(this.listRule.size());
		String key="";
		long total=0,duration,action;
		String car_id=event.getMap().get("car_id").toString();
		for(RuleBean rule:this.listRule) {
			//System.out.println(rule.getName());
			//System.out.println(isInTimeRange(rule.getStime(),rule.getEtime()));
			//System.out.println(rule.getCarIdSet().contains(car_id));
			if(!isInTimeRange(rule.getStime(),rule.getEtime()))continue;//不在时间段内不处理
			key=String.format("KEY_%s_%s", car_id,rule.getId());
			if(rule.getCarIdSet().contains(car_id)) {
				boolean b=rule.getRuleInstance().match(event.getMap());
				if(b) {
					RuleCache ruleCache=RULE_CACHE.getIfPresent(key);
					if(ruleCache==null) {
						ruleCache=new RuleCache();
						RULE_CACHE.put(key, ruleCache);
					}
					total=ruleCache.getTotal().addAndGet(1);
					duration=(System.currentTimeMillis()-ruleCache.getStime())/1000;
					if(duration>=rule.getDuration()) {
						action=ruleCache.getAction().addAndGet(1);
						RuleActionExecuteThread thread=new RuleActionExecuteThread(event.getMap(),rule,new RuleEvent(rule.getName(),total,duration,action));
						this.threadPoolExecutorService.submit(thread);
					}
				}else {
					RuleCache ruleCache=RULE_CACHE.getIfPresent(key);
					if(ruleCache!=null) {
						RULE_CACHE.invalidate(key);
					}
				}
			}
		}
	}

	@PostConstruct
	private void init() {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select t.*,a.clazz from tgps_rule_inst t,tgps_rule a where t.rule_id=a.id and t.status='1'");

		listRule.clear();
		for(Map<String,Object> map:list) {
			RuleBean bean=new RuleBean();
			bean.setId(map.get("id").toString());
			bean.setName(map.get("name").toString());
			bean.setStime(map.get("stime").toString());
			bean.setEtime(map.get("etime").toString());
			bean.setDuration(Integer.parseInt(map.get("min_duration").toString()));
			bean.setCarIdSet(this.getCarIds(map));
			IJT808Rule rule=this.getRule(map);
			if(rule==null) {
				log.info("{}规则实现类初始化失败",bean.getName());
				continue;
			}
			bean.setRuleInstance(rule);
			List<IJT808Action> listAction=this.getActions(map);
			if(listAction.size()==0) {
				log.info("{}动作实现类初始化失败",bean.getName());
				continue;
			}
			bean.setListAction(listAction);
			this.listRule.add(bean);
		}
	}
	public void reload() {
		this.init();
	}
	private List<IJT808Action> getActions(Map<String,Object> map){
		List<IJT808Action> listAction=new ArrayList<>();
		String action_inst_ids=map.get("action_inst_ids").toString();
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select t.action_config,a.clazz from tgps_action_inst t,tgps_action a where t.action_id=a.id and a.status='1' and t.id in("+action_inst_ids+")");
		try {
			for(Map<String,Object> inst:list) {
				String clazzName=inst.get("clazz").toString();
				Class<?> clazz=Class.forName(clazzName.trim());
				IJT808Action action=(IJT808Action)clazz.getConstructors()[0].newInstance();
				action.setApplicationContext(spring);
				List<Map<String,Object>> listConfig=(List<Map<String,Object>>)JSON.parse(inst.get("action_config").toString());
				Map<String,Object> params=new HashMap<>();
				for(Map<String,Object> m:listConfig) {
					params.put(m.get("code").toString(), m.get("value").toString());
				}
				action.init(params);
				listAction.add(action);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error(JSON.toJSONString(map));
		}
		return listAction;
	}
	private IJT808Rule getRule(Map<String,Object> map) {
		IJT808Rule rule=null;
		String clazzName=map.get("clazz").toString();
		try {
			Class<?> clazz=Class.forName(clazzName.trim());
			rule=(IJT808Rule)clazz.getConstructors()[0].newInstance();
			rule.setApplicationContext(spring);
			List<Map<String,Object>> list=(List<Map<String,Object>>)JSON.parse(map.get("rule_config").toString());
			Map<String,Object> params=new HashMap<>();
			for(Map<String,Object> m:list) {
				params.put(m.get("code").toString(), m.get("value").toString());
			}
			rule.init(params);
			}catch(Exception e) {
				log.error(e.getLocalizedMessage());
				log.error(JSON.toJSONString(map));
			}
		return rule;
	}
	private Set<String> getCarIds(Map<String,Object> map){
		Set<String> set=new HashSet<>();
		String car_ids=map.get("car_ids").toString();
		String array[]=car_ids.split(",");
		for(String str:array) {
			set.add(str);
		}
		return set;
	}
	
	 public static boolean isInTimeRange(String stime,String etime) {
	        LocalTime currentTime = LocalTime.now();
	        LocalTime start1 = LocalTime.of(Integer.parseInt(stime.substring(0,2)), Integer.parseInt(stime.substring(2,4)));
	        LocalTime end1 = LocalTime.of(Integer.parseInt(etime.substring(0,2)), Integer.parseInt(etime.substring(2,4)));
	        if(currentTime.equals(start1)||currentTime.equals(end1))return true;
	        if(start1.isBefore(end1)) {
		        boolean inFirstRange = !currentTime.isBefore(start1) && !currentTime.isAfter(end1);
		        return inFirstRange ;
	        }else {
		        boolean inSecondRange = !currentTime.isBefore(start1) || !currentTime.isAfter(end1);
		        return inSecondRange ;
	        }
	        
	}
	 
}
