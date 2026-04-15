package com.lingx.jt808.core.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.jt808.core.IJT808Rule;
import com.lingx.jt808.core.utils.SqlSafe;
import com.lingx.jt808.core.bean.FenceBean;
import com.lingx.jt808.core.bean.Point;

public class FenceOverspeedRule implements IJT808Rule{
	private ApplicationContext spring;
	private List<FenceBean> listFence;
	private int maxSpeed=120;//限速阀值
	@Override
	public void init(Map<String, Object> map) {
		JdbcTemplate jdbc=this.spring.getBean(JdbcTemplate.class);
		if(map.containsKey("ids")) {
			List<Long> ids = SqlSafe.parseLongIds(map.get("ids").toString());
			List<Map<String,Object>> listTemp1;
			if (ids.isEmpty()) {
				listTemp1 = new ArrayList<>();
			} else {
				String sql = "select t.* from tgps_pls t where t.id in (" + SqlSafe.placeholders(ids.size()) + ")";
				listTemp1 = jdbc.queryForList(sql, ids.toArray());
			}
			this.listFence=FenceRule.getFenceBeans(listTemp1);
		}
		if(map.containsKey("speed"))this.maxSpeed=Integer.parseInt(map.get("speed").toString());
		
	}

	@Override
	public boolean match(Map<String, Object> map0x0200) {
		if(listFence==null)return false;
		if(maxSpeed<=0)return false;
		Map<String,Object> map=map0x0200;
		Point point=new Point(Double.parseDouble(map.get("lng").toString()),Double.parseDouble(map.get("lat").toString()));
		int inNum=0;
		for(FenceBean bean:this.listFence) {
			if(FenceRule.inFence(point,bean)) {
				inNum++;
			}
		}
		if(map0x0200.containsKey("speed")) {
			double speed=Double.parseDouble(map0x0200.get("speed").toString());
			if(inNum>0&&speed>this.maxSpeed)return true;
		}
		
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.spring=applicationContext;
	}
}
