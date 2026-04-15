package com.lingx.jt808.core.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.utils.HttpUtils;
import com.lingx.utils.Utils;
import com.lingx.jt808.core.utils.LatLngUtils;

@Component
public class AddressService {
	private final static Logger log = LoggerFactory.getLogger(AddressService.class);
	private static Cache<String, String> ADDRESS_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(10, TimeUnit.MINUTES).build();
	private static final DecimalFormat df = new DecimalFormat("#.000");

	private static final String sql="select address from tgps_address where lat=? and lng=? limit 1";
	private static final String save_sql="insert into tgps_address(lat,lng,address,ts)values(?,?,?,?)";
	//境内解析
	private String type1="gaode";//gaode,baidu
	private String appkey1="";
	//境外解析
	private String type2="google";//photon，google
	private String appkey2="";
	@Autowired
	private DatabaseConfigService databaseConfigService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@PostConstruct
	public void init() {
		this.type1=this.databaseConfigService.getConfigValue("lingx.jt808.type1", "photon");
		this.appkey1=this.databaseConfigService.getConfigValue("lingx.jt808.appkey1", "-");
		this.type2=this.databaseConfigService.getConfigValue("lingx.jt808.type2", "photon");
		this.appkey2=this.databaseConfigService.getConfigValue("lingx.jt808.appkey2", "-");
		log.info("------------AddressService-------------");
		log.info("type1:{}",type1);
		log.info("appkey1:{}",appkey1);
		log.info("type2:{}",type2);
		log.info("appkey2:{}",appkey2);
	}
	public void reload() {
		this.init();
	}
	/**
	 * 传入wgs84坐标系统
	 * @param lat
	 * @param lng
	 * @return 地址信息
	 */
	public String getAddress(String lat,String lng) {
		return this.getAddress(Double.parseDouble(lat), Double.parseDouble(lng));
	}
	/**
	 * 传入wgs84坐标系统
	 * @param lat
	 * @param lng
	 * @return 地址信息
	 */
	public synchronized String getAddress(double lat,double lng) {
		if(!isAddress())return "";
		String key=df.format(lat)+","+df.format(lng);
		String address=ADDRESS_CACHE.getIfPresent(key);
		if(Utils.isNull(address))address=getAddressByDatabase(lat,lng);
		if(Utils.isNull(address)) {
					if(outOfChina(lat,lng)) {
						address=getAddressByOutOfChina(lat, lng);
					}else {
						address=getAddressByInOfChina(lat, lng);
					}
					if(Utils.isNotNull(address))
					ADDRESS_CACHE.put(key, address);
					else ADDRESS_CACHE.put(key, "-");//免得一直请求
				
		}
		if(Utils.isNotNull(address))
		ADDRESS_CACHE.put(key, address);
		return address;
	}

	private synchronized String getAddressByOutOfChina(double lat,double lng) {
		String key=df.format(lat)+","+df.format(lng);
		String address=ADDRESS_CACHE.getIfPresent(key);
		if(address!=null)return address;//防止并发，所以加锁，在这里检查下缓存
		if("google".equals(this.type2)) {
			address=this.getAddressByGoogle(lat, lng);
		}else{
			address=getAddressByPhoton(lat, lng);
		}
		try {
			if(address!=null)
			this.jdbcTemplate.update(save_sql,df.format(lat),df.format(lng),address,Utils.getTime());
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		}
		return address;
	}
	private synchronized String getAddressByInOfChina(double lat,double lng) {
		String key=df.format(lat)+","+df.format(lng);
		String address=ADDRESS_CACHE.getIfPresent(key);
		if(address!=null)return address;//防止并发，所以加锁，在这里检查下缓存
		if("gaode".equals(this.type1)) {
			address=getAddressByGaode(lat, lng);
		}else{
			address=getAddressByPhoton(lat, lng);
		}
		try {
			if(address!=null)
			this.jdbcTemplate.update(save_sql,df.format(lat),df.format(lng),address,Utils.getTime());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return address;
	}
	private static String getAddressByPhoton(double lat,double lng) {
		String url="https://photon.komoot.io/reverse?lat=%s&lon=%s";
		String json=HttpUtils.get(String.format(url, lat,lng));
		Map<String,Object> map=JSON.parseObject(json);
		List<Map<String,Object>> features=(List<Map<String,Object>>)map.get("features");
		StringBuilder sb=new StringBuilder();
		if(features.size()>0) {
			Map<String,Object> properties=(Map<String,Object>)features.get(0).get("properties");
			if(properties.containsKey("state"))sb.append(properties.get("state").toString());
			if(properties.containsKey("county"))sb.append(properties.get("county").toString());
			if(properties.containsKey("city"))sb.append(properties.get("city").toString());
			if(properties.containsKey("street"))sb.append(properties.get("street").toString());
		}
		return sb.toString();
	}
	
	private  String getAddressByGoogle(double lat,double lng) {
		String urlStr="https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s";
		URL url = null;
		try {
			url = new URL(String.format(urlStr, lat,lng,this.appkey2));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		BufferedReader r = null;
		StringBuilder sb=new StringBuilder();
		try {
			URLConnection con = url.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			r = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
			String temp=null;
			while((temp=r.readLine())!=null){
				sb.append(temp);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		Map<String,Object> map=(Map<String,Object>)JSON.parse(sb.toString());
		String address=getAddressByGoogleMap(map);
		return address;
	}
	private static String getAddressByGoogleMap(Map<String,Object> map){
		StringBuilder sb=new StringBuilder();
		List<Map<String,Object>> list=(List<Map<String,Object>>)map.get("results");
		if(list!=null&&list.size()>0){
			Map<String,Object> m=list.get(0);
			sb.append(m.get("formatted_address").toString());
		}
		return sb.toString();
	}
	private  String getAddressByGaode(double lat,double lng){
		String urlStr="http://restapi.amap.com/v3/geocode/regeo?location=%s,%s&key=%s&extensions=all";//e607415290b3792980c29664c6bd5b5b
		URL url = null;
		String temp1 = LatLngUtils.wgs84togcj02(lng,
				lat);
		String[] arr=temp1.split(",");
		try {
			url = new URL(String.format(urlStr, arr[0],arr[1],this.appkey1));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		BufferedReader r = null;
		StringBuilder sb=new StringBuilder();
		try {
			URLConnection con = url.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			r = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
			String temp=null;
			while((temp=r.readLine())!=null){
				sb.append(temp);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		//System.out.println(sb.toString());
		Map<String,Object> map=(Map<String,Object>)JSON.parse(sb.toString());
		String address=getAddressByGaodeMapNew(map);
		
		return address;
		
	}
	private String getAddressByGaodeMapNew(Map<String,Object> map){
		StringBuilder sb=new StringBuilder();
		
		Map<String,Object> result=(Map<String,Object>)map.get("regeocode");//formatted_address
		if(result==null||!result.containsKey("formatted_address"))return null;
		sb.append(result.get("formatted_address").toString());
		List<Map<String,Object>> list=(List<Map<String,Object>>)result.get("pois");
		if(list!=null&&list.size()>1){
			Map<String,Object> m=list.get(1);
			int num=Float.valueOf(m.get("distance").toString()).intValue();
			if(num!=0)
				sb.append(",距").append(m.get("name")).append(num).append("米");
		}
		return sb.toString();
	}
	
	/**
	 * 是否在境外
	 * @param lat
	 * @param lng
	 * @return
	 */
	public boolean outOfChina(double lat,double lng) {
		return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271);
	}
	public boolean isAddress() {
		return "true".equals(this.databaseConfigService.getConfigValue("lingx.jt808.address", "true"));
	}
	private String getAddressByDatabase(double lat, double lng) {
		try {
			Map<String,Object> map=this.jdbcTemplate.queryForMap(sql,df.format(lat),df.format(lng));
			return map.get("address").toString();
		} catch (Exception e) {
			return null;
		}
	}
}
