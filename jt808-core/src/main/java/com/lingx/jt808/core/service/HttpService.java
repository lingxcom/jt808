package com.lingx.jt808.core.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;
@Component
public class HttpService {

	public  String get(String httpUrl){
		StringBuilder sb=new StringBuilder();
		HttpURLConnection conn=null;
		InputStream in=null;
		try{
			URL url = new URL(httpUrl);  
	         conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
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

	
	 public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestMethod("POST");// 设置URL请求方法   
	    	
	        	conn.setRequestProperty("Charset", "UTF-8"); 
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setReadTimeout(30000);;
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    

		public static void main(String args[]) {
			System.out.println(1);
		}
}
