package com.lingx.jt808.html.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlUtils {

	public static String getStringByTag(String content,String tag) {
		String start="<"+tag+">";
		String end="</"+tag+">";
		int sindex=content.indexOf(start),eindex=content.indexOf(end);
		return new String(content.substring(sindex+start.length(),eindex));
	}
	
	public static String getStringByMate(String content,String tag) {
		String start=tag;
		String end=">";
		int sindex=content.indexOf(start);
		String temp=content.substring(sindex);
		int eindex=temp.indexOf(end);
		//System.out.println(temp);
		temp=temp.substring(0,eindex);
		//System.out.println(temp);
		String array[]=temp.split("\"");
		String ret=array[array.length-1];
		array=null;
		return ret;
	}

	public static String get(String httpUrl){
		StringBuilder sb=new StringBuilder();
		HttpURLConnection conn=null;
		InputStream in=null;
		try{
			URL url = new URL(httpUrl);  
	         conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			HttpURLConnection.setFollowRedirects(true);
			// 请求方式
			conn.setRequestMethod("GET");
	       // conn.setRequestProperty("Connection", "Keep-Alive");  
			//conn.setDoOutput(true);
			//conn.setDoInput(true);
			int len;
			byte[] buff=new byte[4096];
	        in=conn.getInputStream();
	        while((len=in.read(buff))!=-1){//System.out.println(len);
	        	sb.append(new String(buff,0,len));
	        }
	        in.close();
	        conn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
		        try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	public static void main(String args[]) {
		System.out.println(get("http://jmjsb.cn"));
	}
}
