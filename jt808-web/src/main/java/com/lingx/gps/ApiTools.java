package com.lingx.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ApiTools implements Runnable{
	public static boolean isRun=true;
	private static String apiurl="https://www.gb35658.com/lingx3api";
	private static String apitoken = "a4475031-d2af-4695-a722-8f6128d90581";
	private static String account="openapi";
	private static String password="63474798a035919d470a81536897b2b0";
	private static String token="";
	@PostConstruct
	public void init() {
		System.out.println("API初始化");
		getToken();
		new Thread(new ApiTools()).start();
	}
	public static String callApi(Map<String,Object> param) {
		param.put("lingxtoken", token);
		String res=post(apiurl,JSON.toJSONString(param));
		//Map<String,Object> map=(Map<String,Object>)JSON.parse(res);
		//return map;
		return res;
	}
	private static void getToken() {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("apicode", 1200);
		param.put("account", account);
		param.put("password", password);
		param.put("apitoken", apitoken);
		String res=post(apiurl,JSON.toJSONString(param));
		Map<String,Object> map=(Map<String,Object>)JSON.parse(res);
		if("1".equals(map.get("code").toString())) {
			token=map.get("token").toString();
		}
		
	}
	private static void refreshToken() {
		System.out.println("刷新TOKEN,旧TOKEN:"+token);
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("apicode", 1210);
		param.put("lingxtoken", token);
		String res=post(apiurl,JSON.toJSONString(param));
		Map<String,Object> map=(Map<String,Object>)JSON.parse(res);
		if("1".equals(map.get("code").toString())) {
			token=map.get("token").toString();
			System.out.println("刷新成功,新TOKEN:"+token);
		}
	}
	public static String post(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestMethod("POST");// 设置URL请求方法

			conn.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	@Override
	public void run() {
		if(isRun) {
			try {
				Thread.currentThread().sleep(10*60*60*1000l);
				refreshToken();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
