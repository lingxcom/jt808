package com.lingx.jt808.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.service.GroupService;
import com.lingx.jt808.core.utils.DateUtils;
import com.lingx.jt808.core.utils.SqlSafe;

@Component
public class Api1124 extends AbstractAuthApi implements IApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;
	@Autowired
	private GroupService groupService;


	@Override
	public int getApiCode() {
		return 1124;
	}

	@Override
	public String getApiName() {
		return "离线统计";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String, Object> ret = IApi.getRetMap(1, "SUCCESS");
		String date8 = this.getParamString(params, "date8", "");
		String token = IApi.getParamString(params, "lingxtoken", "");
		String userid = TokenUtils.getTokenDataUserId(token);
		String ifwhere = "";
		String gpstimeEnd = null;
		if (Utils.isNotNull(date8)) {
			String d = SqlSafe.sanitizeDigitTime(date8);
			if (d == null || d.length() != 8) {
				return ret;
			}
			gpstimeEnd = d + "235959";
			ifwhere = " and gpstime<?";
		}else {
			return ret;
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				"select id,tid,carno,speed,direction fx,status,lat,lng,location,acc,gpstime from tgps_car where 1=1 "
						+ ifwhere
						+ " and group_id in (select group_id from tgps_group_user where user_id=?)"
						+ " order by gpstime desc limit 1000",
				gpstimeEnd, userid);
		String time=Utils.getTime();
		for (Map<String, Object> map : list) {
			map.put("group", this.groupService.getGroupNameBy(map.get("id").toString()));
			if(map.get("gpstime")!=null)
			map.put("time1", DateUtils.getTime(time,map.get("gpstime").toString()));
			
		
		}
		ret.put("data", list);
		return ret;

	}

	public boolean isLog() {
		return false;
	}
}
