package com.lingx.jt808.api.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.utils.TokenUtils;
import com.lingx.utils.Utils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.service.JT808CommonService;
import com.lingx.jt808.core.utils.SqlSafe;
@Component
public class Api1101  extends AbstractAuthApi{
	public static Cache<String,Integer> CacheTotal=CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(10, TimeUnit.SECONDS).build();

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService commonService;
	
	@Value("#{configs['gps.car.endtime.enabled']}")
	private String enabledEndTime="false";
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		List<Object> expandedKeys=new ArrayList<>();
		String fid=IApi.getParamString(params,"fid", "");
		String node=fid;
		String text=IApi.getParamString(params,"searchText", "");
		String isVideo=IApi.getParamString(params,"isVideo", "");
		String isStatus=IApi.getParamString(params,"isStatus", "");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		String language=IApi.getParamString(params, "language", "zh-CN");
		String type=IApi.getParamString(params,"type", "all");

		String groupWhere1="",groupWhere2="";
		String isVideoWhere="";
		String iconCar="";//
		if("true".equals(isVideo)) {
			isVideoWhere=" and channel_num>0 ";
		}

		groupWhere1=" and id in (select group_id from tgps_group_user where user_id=?)";
		groupWhere2=" fid not in (select group_id from tgps_group_user where user_id=?)";
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
					m.put("deviceId",tempTid);
					
				}

				ret.put("data", list);
				ret.put("expandedKeys", expandedKeys);
			}
			return ret;
		}//摄像头处理结束
		
		if(Utils.isNotNull(text)){
			text=text.trim();
			String like = "%" + SqlSafe.escapeLike(text) + "%";
			List<Map<String,Object>> list2=this.jdbcTemplate.queryForList("select id,carno text,id `value`,speed,etime from tgps_car where (carno like ? or tid like ? or czxm like ? or group_id in(select id from tgps_group where name like ?)) and group_id in (select group_id from tgps_group_user where user_id=?) "+isVideoWhere+" order by online desc,gpstime desc",
					like, like, like,like, userid);
			for(Map<String,Object> map:list2){
				Map<String,Object> cache=commonService.getLast0x0200Data(map.get("id").toString());//cacheService.getCar(map.get("id").toString());
				int zt=commonService.getJT808Status(cache);
				map.put("iconCls", iconCar+" carStatus_"+zt);//iconfont icon-icon_huabanfuben
				map.put("status", zt);
				map.put("deviceId",map.get("id"));
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
				list=this.jdbcTemplate.queryForList("select id,name text,id `value`,icon_cls,total,online from tgps_group where "+groupWhere2+" "+groupWhere1+" order by orderindex asc,name  asc", userid, userid);
				
			}else{
				list=this.jdbcTemplate.queryForList("select id,name text,id `value`,icon_cls,total,online from tgps_group where fid=? "+groupWhere1+" order by orderindex asc, name  asc",node, userid);
			}
			 for(Map<String,Object> map:list){
				
				 int total=getCarNumByGroup(map.get("id").toString(), userid,isVideoWhere),online=getOnlineCarNumByGroup(map.get("id").toString(), userid,isVideoWhere);
				 if("true".equals(isVideo)) {
					 
					map.put("text",map.get("text")+"("+online+"/"+total+")");
				 } else if("all".equals(type)) {
					map.put("text",map.get("text")+"("+online+"/"+total+")");
				}else if("online".equals(type)) {
					map.put("text",map.get("text")+"("+online+")");
				}else if("offline".equals(type)) {
					map.put("text",map.get("text")+"("+(total-online)+")");
				}else if("follow".equals(type)) {
					int follow=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id in(select car_id from tgps_car_follow where user_id=?) and group_id=?", Integer.class,userid,map.get("id"));
					map.put("text",map.get("text")+"("+follow+")");
				}
					
					map.put("type","group");
			} 
			 {//根据分组ID取车辆
				 if("online".equals(type)) {
					 isVideoWhere+=" and online='1' ";
				 }else if("offline".equals(type)){
					 isVideoWhere+=" and online<>'1' ";
				 }else if("follow".equals(type)){
					 isVideoWhere+=" and id in (select car_id from tgps_car_follow where user_id=?) ";
				 }
				 
				List<Map<String,Object>> list2;
				if("follow".equals(type)) {
					list2=this.jdbcTemplate.queryForList("select id,carno text,id `value`,speed,online,etime from tgps_car where group_id=? "+isVideoWhere+" order by gpstime desc limit 100",node, userid);
				} else {
					list2=this.jdbcTemplate.queryForList("select id,carno text,id `value`,speed,online,etime from tgps_car where group_id=? "+isVideoWhere+" order by gpstime desc limit 100",node);
				}
				for(Map<String,Object> map:list2){
					Map<String,Object> cache=commonService.getLast0x0200Data(map.get("id").toString());//cacheService.getCar(map.get("id").toString());
					if(cache==null)continue;
					cache.put("online", map.get("online"));
					int zt=commonService.getJT808Status(cache);
					map.put("iconCls", iconCar+" carStatus_"+zt);
					map.put("status", zt);
					map.put("deviceId",map.get("id"));
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
			expandedKeys.add(Integer.parseInt(list.get(0).get("id").toString()));
		}
		ret.put("expandedKeys", expandedKeys);
		
		ret.put("data2", getData2(userid));
		return ret;
	}
	
	private Map<String,Object> getData2(String userid){
		Map<String,Object> data2=new HashMap<>();
		int total=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where group_id in(select group_id from tgps_group_user where user_id=?)", Integer.class,userid);

		int online=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where online='1' and group_id in(select group_id from tgps_group_user where user_id=?)", Integer.class,userid);
		int offline=total-online;
		int follow=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where id in(select car_id from tgps_car_follow where user_id=?)",Integer.class,userid);
		data2.put("total", total);
		data2.put("online", online);
		data2.put("offline", offline);
		data2.put("follow", follow);
		return data2;
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
	private String getStatusString(Map<String,Object> map,String isStatus,String language) {
		if(map==null||!"true".equals(isStatus))return "";
		this.commonService.addJT808Info(map,language,false);
		return " - "+map.get("status_leftcar").toString();
	}
	
	public int getCarNumByGroup(String groupId, String userId,String isVideoWhere) {
		int count=0;

		String key=String.format("%s_%s_total", groupId,isVideoWhere);
		if(CacheTotal.getIfPresent(key)!=null)return CacheTotal.getIfPresent(key);
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=? and id in (select group_id from tgps_group_user where user_id=?)"
				,groupId,userId);
		for(Map<String,Object> map:listGroups){
			count+=this.getCarNumByGroup(map.get("id").toString(), userId,isVideoWhere);
		}
		if(isVideoWhere.contains("tgps_car_follow")) {
			count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where  group_id=? "+isVideoWhere,Integer.class,groupId, userId);
		} else {
			count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where  group_id=? "+isVideoWhere,Integer.class,groupId);
		}
		CacheTotal.put(key, count);
		return count;
	}
	public int getOnlineCarNumByGroup(String groupId, String userId,String isVideoWhere) {
		int count=0;

		String key=String.format("%s_%s_online", groupId,isVideoWhere);
		if(CacheTotal.getIfPresent(key)!=null)return CacheTotal.getIfPresent(key);
		List<Map<String,Object>> listGroups=this.jdbcTemplate.queryForList("select id from tgps_group where fid=? and id in (select group_id from tgps_group_user where user_id=?)"
				,groupId,userId);
		for(Map<String,Object> map:listGroups){
			count+=this.getOnlineCarNumByGroup(map.get("id").toString(), userId, isVideoWhere);
		}
		if(isVideoWhere.contains("tgps_car_follow")) {
			count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where group_id=? and online='1'"+isVideoWhere,Integer.class,groupId, userId);
		} else {
			count+=this.jdbcTemplate.queryForObject("select count(*) from tgps_car where group_id=? and online='1'"+isVideoWhere,Integer.class,groupId);
		}
		CacheTotal.put(key, count);
		return count;
	}
	
	private void getTdh(Map<String,Object> map,String isVideo,List<Object> expandedKeys) {
		if("true".equals(isVideo)) {
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select tdh from tgps_car_tdh where car_id=? order by tdh asc",map.get("value"));
			if(list.size()>0) {
				expandedKeys.add(map.get("id").toString());
				map.remove("leaf");
				//map.put("children", list);
				//map.put("type","tdh");
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
	

	public boolean isLog() {
		return false;
	}
}
