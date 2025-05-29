package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.utils.Utils;
import com.lingx.service.LingxService;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

@Component
public class Api1163 extends AbstractAuthApi implements IApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private LingxService lingxService;

	@Override
	public int getApiCode() {
		return 1163;
	}

	@Override
	public String getApiName() {
		return "批量添加";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String, Object> ret = IApi.getRetMap(1, "添加成功");
		String type = this.getParamString(params, "type", "single");
		if("single".equals(type)) {
			
			if (this.checkIsNull(params, "tid", ret))
				return ret;
			if (this.checkIsNull(params, "groupId", ret))
				return ret;
			
			String tid = this.getParamString(params, "tid", "");
			String carno = this.getParamString(params, "carno", "");
			String groupId = this.getParamString(params, "groupId", "");
			String version = this.getParamString(params, "version", "");
			String sim = this.getParamString(params, "sim", "");
			String czxm = this.getParamString(params, "czxm", "");
			String tel = this.getParamString(params, "tel", "");
			String remark = this.getParamString(params, "remark", "");
			tid=tid.trim();
			while (tid.charAt(0) == '0') {
				tid = tid.substring(1);
			}
			int c=lingxService.queryForInt("select count(*) from tgps_car where tid=?",tid);
			if(c>0){
				ret.put("code", -1);
				ret.put("message", "添加失败，设备号已存在");
				return ret;
			}else{
			String ts=lingxService.ts();
			String ets=lingxService.getTime(1,1);
			jdbcTemplate.update("insert into tgps_car(tid,carno,sim,czxm,tel,remark,version,create_time,modify_time,stime,etime)values(?,?,?,?,?,?,?,?,?,?,?)",tid,carno,sim,czxm,tel,remark,version,ts,ts,ts.substring(0,8),ets.substring(0,8));

			int car_id=this.jdbcTemplate.queryForObject("select id from tgps_car where tid=?", Integer.class,tid);

			IJT808Cache.WHITE_LIST.put(tid, String.valueOf(car_id));
			jdbcTemplate.update("insert into tgps_group_car(group_id,car_id)values(?,?)",groupId,car_id);
			
			}
		}else {
			
		if (this.checkIsNull(params, "tids", ret))
			return ret;
		if (this.checkIsNull(params, "groupId", ret))
			return ret;
		
		String tids = this.getParamString(params, "tids", "");
		String groupId = this.getParamString(params, "groupId", "");
		List<String> channels = (List<String>) params.get("channels");// this.getParamString(params, "channels", "");
		String version = this.getParamString(params, "version", "");
		String remark = this.getParamString(params, "remark", "");

		String sql = "insert into tgps_car(tid,carno,sim,czxm,tel,remark,version,create_time,modify_time,stime,etime)values(?,?,?,?,?,?,?,?,?,?,?)";
		tids = tids.trim();
		tids = tids.replace("，", ",");
		tids = tids.replace("\n", ",");
		

		String array[] = tids.split(",");
		String time = Utils.getTime();
		String ets = this.lingxService.getTime(1, 1);
		for (String tid : array) {
			if(Utils.isNull(tid))continue;
			tid=tid.trim();
			while (tid.charAt(0) == '0') {
				tid = tid.substring(1);
			}
			if(this.jdbcTemplate.queryForObject("select count(*) from tgps_car where tid=?", Integer.class,tid)>0)continue;
			this.jdbcTemplate.update(sql, tid, tid, "","", "", remark, version, time, time,
					time.substring(0, 8), ets.substring(0, 8));
			int car_id=this.jdbcTemplate.queryForObject("select id from tgps_car where tid=?", Integer.class,tid);
			IJT808Cache.WHITE_LIST.put(tid, String.valueOf(car_id));
			this.jdbcTemplate.update("delete from tgps_group_car where car_id=?",car_id);
			this.jdbcTemplate.update("insert into tgps_group_car(group_id,car_id)values(?,?)", groupId, car_id);

		}
		
		}
		
		Api1101.CacheTotal.invalidateAll();
		return ret;

	}

}
