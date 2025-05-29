package com.tracbds.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.utils.SQLUtils;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;

@Component
public class Api1123 extends AbstractAuthApi implements IApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LingxService lingxService;

	@Override
	public int getApiCode() {
		return 1123;
	}

	@Override
	public String getApiName() {
		return "最新数据";
	}

	@Override
	public String getGroupName() {
		return "车载监控";
	}

	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String, Object> ret = IApi.getRetMap(1, "SUCCESS");
		String text = this.getParamString(params, "text", "");

		text=SQLUtils.getValue(text);
		String token = IApi.getParamString(params, "lingxtoken", "");
		String userid = TokenUtils.getTokenDataUserId(token);
		String ifwhere = "";
		if (Utils.isNotNull(text)) {
			ifwhere = " and (carno like '%" + text + "%' or id like '%" + text + "%')";
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				"select id,carno,speed,direction fx,status,lat,lng,location,acc,gpstime from tgps_car where 1=1 "
						+ ifwhere
						+ " and id in(select car_id from tgps_group_car where group_id in (select group_id from tgps_group_user where user_id='"
						+ userid + "')) order by gpstime desc limit 100");

		for (Map<String, Object> map : list) {
			map.put("group", this.jdbcTemplate.queryForObject("select name from tgps_group where id =(select max( group_id) from tgps_group_car where car_id=?)", String.class,map.get("id")));
		}
		ret.put("data", list);
		return ret;

	}

}
