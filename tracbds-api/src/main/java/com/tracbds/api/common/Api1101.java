package com.tracbds.api.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracbds.core.service.JT808CommonService;
import com.lingx.utils.SQLUtils;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
@Component
public class Api1101  extends AbstractAuthApi{
	public static Cache<String,Integer> CacheTotal=CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.HOURS).build();

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
	
	@Value("#{configs['gps.car.endtime.enabled']}")
	private String enabledEndTime="false";
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		List<String> expandedKeys=new ArrayList<>();
		String fid=IApi.getParamString(params,"fid", "");
		String node=fid;
		String text=IApi.getParamString(params,"searchText", "");
		String isVideo=IApi.getParamString(params,"isVideo", "");
		String isStatus=IApi.getParamString(params,"isStatus", "");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		String language=IApi.getParamString(params, "language", "zh-CN");
		
		text=SQLUtils.getValue(text);

		String groupWhere1="",groupWhere2="";
		String isVideoWhere="";
		String iconCar="";//
		if("true".equals(isVideo)) {
			isVideoWhere=" and channel_num>0 ";
		}

		groupWhere1=" and id in (select group_id from tgps_group_user where user_id='"+userid+"')";
		groupWhere2=" fid not in (select group_id from tgps_group_user where user_id='"+userid+"')";
		if(fid.indexOf("_")>0) {
			String tempTid=fid.substring(fid.indexOf("_")+1);
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select tdh,name from tgps_car_tdh where car_id=? order by tdh asc",tempTid);
			if(list.size()>0) {
				for(Map<String,Object> m:list) {
					m.put("iconCls", "el-icon-video-camera-solid");
					m.put("id",tempTid+"_"+m.get("tdh"));
					String temp="CH-"+m.get("tdh");
					if(Utils.isNotNull(m.get("name").toString())&&!temp.equals(m.get("name").toString())) {
						temp=temp+"["+m.get("name")+"]";
					}
					m.put("text",temp);
					m.put("value",m.get("tdh"));
					m.put("leaf",true);
					m.put("type","tdh");
				}

				ret.put("data", list);
				ret.put("expandedKeys", expandedKeys);
			}
			return ret;
		}//摄像头处理结束
		
		if(Utils.isNotNull(text)){
			text=text.trim();
			List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select id,carno text,id `value`,speed,etime from tgps_car where (carno like '%"+text+"%' or tid like '%"+text+"%' or czxm like '%"+text+"%') and id in(select car_id from tgps_group_car where group_id in (select group_id from tgps_group_user where user_id='"+userid+"')) "+isVideoWhere+" order by online desc,carno  asc");
			for(Map<String,Object> map:list2){
				Map<String,Object> cache=commonService.getLast0x0200Data(map.get("id").toString());//cacheService.getCar(map.get("id").toString());
				int zt=commonService.getJT808Status(cache);
				map.put("iconCls", iconCar+" carStatus_"+zt);//iconfont icon-icon_huabanfuben
				map.put("status", zt);
				map.put("id",0+"_"+map.get("id"));
				map.put("leaf",true);
				map.put("text", map.get("text").toString()+this.getStatusString(cache,isStatus,language));
				map.put("type","device");
				this.getTdh(map, isVideo,expandedKeys);
				this.handleEndTimeMap(map);

			}
			ret.put("data", list2);
		}else{
			List<Map<String,Object>> list=null;
			if("0".equals(node)){
				list=this.jdbcTemplate.queryForList("select id,name text,id `value`,icon_cls from tgps_group where "+groupWhere2+" "+groupWhere1+" order by orderindex asc,name  asc");
				
			}else{
				list=this.jdbcTemplate.queryForList("select id,name text,id `value`,icon_cls from tgps_group where fid=? "+groupWhere1+" order by orderindex asc, name  asc",node);
			}
			 for(Map<String,Object> map:list){
				 int total=getCarNumByGroup(map.get("id").toString(), userid,isVideoWhere),online=getOnlineCarNumByGroup(map.get("id").toString(), userid,isVideoWhere);
					map.put("text",map.get("text")+"("+online+"/"+total+")");
					map.put("type","group");
			} 
			 {//根据分组ID取车辆
				List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select id,carno text,id `value`,speed,online,etime from tgps_car where id in(select car_id from tgps_group_car where group_id=?) "+isVideoWhere+" order by online desc,carno asc limit 100",node);
				for(Map<String,Object> map:list2){
					Map<String,Object> cache=commonService.getLast0x0200Data(map.get("id").toString());//cacheService.getCar(map.get("id").toString());
					if(cache==null)continue;
					cache.put("online", map.get("online"));
					int zt=commonService.getJT808Status(cache);
					map.put("iconCls", iconCar+" carStatus_"+zt);
					map.put("status", zt);
					map.put("id",node+"_"+map.get("id"));
					map.put("leaf",true);
					map.put("text", map.get("text").toString()+this.getStatusString(cache,isStatus,language));
					map.put("type","device");
					this.getTdh(map, isVideo,expandedKeys);
					this.handleEndTimeMap(map);
				}
				list.addAll(list2);
			
			 }
				ret.put("data", list);
		}
		List<Map<String,Object>> list=(List<Map<String,Object>>)ret.get("data");
		if(expandedKeys.size()==0&&list.size()!=0&&"0".equals(fid)&&Utils.isNull(text)) {
			expandedKeys.add(list.get(0).get("id").toString());
		}
		ret.put("expandedKeys", expandedKeys);
		return ret;
	}
	public boolean handleEndTimeMap(Map<String,Object> map) {
		boolean b=false;
		if(map==null)return false;
		if(!"true".equals(this.enabledEndTime)) {
			map.put("expire", false);
			return b;
		}
		if(map.containsKey("etime")) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			try {
				long etime=sdf.parse(map.get("etime").toString()).getTime();
				if(System.currentTimeMillis()>etime) {
					map.put("expire", true);
					b=true;
				}else {
					map.put("expire", false);
				}
			} catch (Exception e) {
				map.put("expire", false);
			}
		}else {
			map.put("expire", false);
		}
		return b;
	}
	public String getStatusString(Map<String,Object> map,String isStatus,String language) {
		if(map==null||!"true".equals(isStatus))return "";
		this.commonService.addJT808Info(map,language);
		return " - "+map.get("status_leftcar").toString();
	}
	
	
	private void getTdh(Map<String,Object> map,String isVideo,List<String> expandedKeys) {
		if("true".equals(isVideo)) {
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select tdh from tgps_car_tdh where car_id=? order by tdh asc",map.get("value"));
			if(list.size()>0) {
				expandedKeys.add(map.get("id").toString());
				map.remove("leaf");
				map.put("children", list);
				map.put("type","tdh");
			}
		}
	}
	@Override
	public int getApiCode() {
		return 1101;
	}

	@Override
	public String getApiName() {
		return "树形分组车辆";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	

	public int getCarNumByGroup(String groupId, String userId,String isVideoWhere) {
		int count=0;
		
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=? and id in (select group_id from tgps_group_user where user_id=?)"
				,groupId,userId);
		for(Map<String,Object> map:listGroups){
			count+=this.getCarNumByGroup(map.get("id").toString(), userId,isVideoWhere);
		}
		
		String key=String.format("%s_%s_total", groupId,isVideoWhere);
		if(CacheTotal.getIfPresent(key)!=null)return CacheTotal.getIfPresent(key);
		count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id in (select car_id from tgps_group_car where group_id=?)"+isVideoWhere,Integer.class,groupId);
		CacheTotal.put(key, count);
		return count;
	}
	public int getOnlineCarNumByGroup(String groupId, String userId,String isVideoWhere) {
		int count=0;
		
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=? and id in (select group_id from tgps_group_user where user_id=?)"
				,groupId,userId);
		for(Map<String,Object> map:listGroups){
			count+=this.getOnlineCarNumByGroup(map.get("id").toString(), userId, isVideoWhere);
		}
		count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id in (select car_id from tgps_group_car where group_id=?) and online='1'"+isVideoWhere,Integer.class,groupId);
		return count;
	}
	public boolean isLog() {
		return false;
	}
}
