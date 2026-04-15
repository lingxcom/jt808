package com.lingx.jt808.api.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.model.IContext;
import com.lingx.model.IExtExecutor;
import com.lingx.model.IPerformer;
import com.lingx.model.IRuleCondition;
import com.lingx.service.LingxService;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
@Component
public class HistoryDataExecutor implements IExtExecutor {

	public static Cache<String, String> DATE_CACHE=CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(12, TimeUnit.HOURS).build();
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IRuleCondition ruleCondition;
	@Resource
	private LingxService lingxService;
//{"apicode":8002,"e":"tsh_order","m":"grid","limit":50,"page":1,"orderField":"id","orderType":"desc","lingxtoken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsYXN0TG9naW5UaW1lIjoxNzEwMTYzNzI3MjIxLCJyb2xlSWRzIjoiNmUwMzYyZTgtMTAwZS0xMWU1LWI3YWItNzRkMDJiNmI1ZjYxIiwib3JnSWRzIjoiYzhhODA3ZjItOWNlZS00Mzg0LTlhNDEtZGFmMTcwNzNiOTk3IiwiZXhwIjoxNzEwMTY2NzUxLCJ1c2VySWQiOiI0M2IyZjg0Yi02ZTJkLTRjNGEtODdkNi1hYjY3ZThjODgzYWUifQ.pH46UuVr8AwPQT3-ZJjdtjiNUhgixPl2oq1XzrJNfBU"}: 
	@Override
	public Object execute(Map<String, Object> params,IContext context, IPerformer performer) throws Exception {
		Map<String,Object> ret=IApi.getRetMap();
		String limit=IApi.getParamString(params, "limit", "20");
		String page=IApi.getParamString(params, "page", "1");
		String orderField=IApi.getParamString(params, "orderField", "id");
		String orderType=IApi.getParamString(params, "orderType", "desc");
		String cx_date=IApi.getParamString(params, "cx_date", "");
		String cx_carid=IApi.getParamString(params, "cx_carid", "");
		String lingxtoken=IApi.getParamString(params, "lingxtoken", "");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date=sdf.format(new Date());
		String vif="";
		if(Utils.isNotNull(cx_date)) {
			date=cx_date;
		}
		if(!StringUtils.isNumber(date))return ret;
		if(!StringUtils.isNumber(limit))return ret;
		if(!StringUtils.isNumber(page))return ret;
		DATE_CACHE.put(lingxtoken, cx_date);
		String rule=this.ruleCondition.getCondition(ret, context, performer);
		String sql="select * from tgps_data_"+date+" where car_id=? "+rule+vif+" order by "+orderField+" "+orderType+" limit "+(Integer.parseInt(limit)*(Integer.parseInt(page)-1))+","+limit;
		sql=Utils.sqlInjection(sql);
		//logger.info(sql);
		

		this.lingxService.putSqlToSession( sql,context.getEntity().getCode(), context.getUserBean().getId());
		ret.put("rows", this.jdbcTemplate.queryForList(sql,cx_carid));
		ret.put("total", this.jdbcTemplate.queryForObject(Utils.sqlInjection("select count(*) from tgps_data_"+date+" where car_id=? "+rule+vif), Integer.class,cx_carid));
		
		return ret;
	}

	@Override
	public String ecode() {
		return "tgps_data_20260408";
	}

	@Override
	public Map<String, Object> getData(Object id,Map<String, Object> params) {
		String lingxtoken=IApi.getParamString(params, "lingxtoken", "");
		String cx_date=DATE_CACHE.getIfPresent(lingxtoken);
		if(Utils.isNull(cx_date)) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			cx_date=sdf.format(new Date());
		}
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(String.format("select * from tgps_data_%s where id=?",cx_date),id);
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
