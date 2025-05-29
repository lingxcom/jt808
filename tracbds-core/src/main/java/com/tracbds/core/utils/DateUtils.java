package com.tracbds.core.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static void main1(String[] args)
	  {
	    System.out.println(time14XiangJian(Utils.getTime(), "20141208101855"));
	  }
	  public static long fromDateStringToLong(String inVal) {
	    if ("".equals(inVal)) return 0L;
	    java.util.Date date = null;
	    SimpleDateFormat inputFormat = new SimpleDateFormat(
	      "yyyyMMddHHmmss");
	    try {
	      date = inputFormat.parse(inVal);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return date.getTime();
	  }

	  public static String fromDateToString(java.util.Date date) {
	    SimpleDateFormat inputFormat = new SimpleDateFormat(
	      "yyyyMMddHHmmss");

	    return inputFormat.format(date);
	  }
	  public static String getTime(long time) {
	    String dateTime = MessageFormat.format("{0,date,yyyyMMddHHmmss}", 
	      new Object[] { new java.sql.Date(time) });
	    return dateTime;
	  }
	  public static String getTimeBy(String stime, String etime) {
	    long start = 0L; long end = 0L;
	    try {
	      start = fromDateStringToLong(stime);
	      end = fromDateStringToLong(etime);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    long time = end - start;

	    long mint = time / 1000L;

	    long s = mint % 60L;
	    long m = mint / 60L % 60L;
	    long h = mint / 3600L;
	    String temp1 = h + "小时" + m + "分钟";
	    if(h==0){
	    	temp1 = m + "分钟";
	    }
	    return temp1;
	  }
	  /**
	   * 秒
	   * @param time
	   * @return
	   */
	  public static String getTimeBy(long time) {
	    

	    long mint = time ;

	    long s = mint % 60L;
	    long m = mint / 60L % 60L;
	    long h = mint / 3600L;
	    String temp1 = h + "小时" + m + "分钟";
	    if(h==0){
	    	temp1 = m + "分钟";
	    }
	    return temp1;
	  }
	  public static int time14XiangJian(String time1, String time2) {
	    int h = 0; int m = 0; int s = 0;

	    int y = Integer.parseInt(time1.substring(0, 4)) - Integer.parseInt(time2.substring(0, 4));
	    int M = Integer.parseInt(time1.substring(4, 6)) - Integer.parseInt(time2.substring(4, 6));
	    int d = Integer.parseInt(time1.substring(6, 8)) - Integer.parseInt(time2.substring(6, 8));
	    h = Integer.parseInt(time1.substring(8, 10)) - Integer.parseInt(time2.substring(8, 10));
	    m = Integer.parseInt(time1.substring(10, 12)) - Integer.parseInt(time2.substring(10, 12));
	    s = Integer.parseInt(time1.substring(12, 14)) - Integer.parseInt(time2.substring(12, 14));
	    return y * 365 * 24 * 60 * 60 + M * 30 * 24 * 60 * 60 + d * 24 * 60 * 60 + h * 60 * 60 + m * 60 + s;
	  }

	  public static String getTime()
	  {
	    String dateTime = MessageFormat.format("{0,date,yyyyMMddHHmmss}", 
	      new Object[] { new java.sql.Date(System.currentTimeMillis()) });
	    return dateTime;
	  }

	  public static String getDate()
	  {
	    String dateTime = MessageFormat.format("{0,date,yyyy-MM-dd}", 
	      new Object[] { new java.sql.Date(System.currentTimeMillis()) });
	    return dateTime;
	  }

	  public static long getTime(String stime, String etime) {
	    long time = -1L;
	    try {
	      SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
	      java.util.Date sdate = dateFm.parse(stime);
	      java.util.Date edate = dateFm.parse(etime);
	      time = edate.getTime() - sdate.getTime();
	    }
	    catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return Math.abs(time);
	  }

	  public static String formatDataTime(String dateTime)
	  {
	    String temp = "";
	    try {
	      SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      java.util.Date date = dateFm.parse(dateTime);
	      SimpleDateFormat dateFm2 = new SimpleDateFormat("yyyyMMddHHmmss");
	      temp = dateFm2.format(date);
	    }
	    catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return temp;
	  }

	  public static String formatDataTime2(String dateTime)
	  {
	    String temp = "";
	    try {
	      SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
	      java.util.Date date = dateFm.parse(dateTime);
	      SimpleDateFormat dateFm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      temp = dateFm2.format(date);
	    }
	    catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return temp;
	  }

	  public static String formatDataTime3(String dateTime)
	  {
	    String temp = "";
	    try {
	      SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
	      java.util.Date date = dateFm.parse(dateTime);
	      SimpleDateFormat dateFm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      temp = dateFm2.format(date);
	    }
	    catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return temp;
	  }

	  public static String formatDataTime2_1(String dateTime)
	  {
	    String temp = "";
	    try {
	      SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
	      java.util.Date date = dateFm.parse(dateTime);
	      SimpleDateFormat dateFm2 = new SimpleDateFormat("yyyy-MM-dd");
	      temp = dateFm2.format(date);
	    }
	    catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return temp;
	  }
	  
	  public static String getDistanceTime(String str1, String str2) {  
		  SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
	        Date one;  
	        Date two;  
	        long day = 0;  
	        long hour = 0;  
	        long min = 0;  
	        long sec = 0;  
	        try {  
	            one = df.parse(str1);  
	            two = df.parse(str2);  
	            long time1 = one.getTime();  
	            long time2 = two.getTime();  
	            long diff ;  
	            if(time1<time2) {  
	                diff = time2 - time1;  
	            } else {  
	                diff = time1 - time2;  
	            }  
	            day = diff / (24 * 60 * 60 * 1000);  
	            hour = (diff / (60 * 60 * 1000) - day * 24);  
	            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        }  
	        StringBuilder sb=new StringBuilder();
	        if(day>0){
	        	sb.append(day).append("天");
	        }
	        if(hour>0){
	        	sb.append(hour).append("小时");
	        }
	        if(min>0&&day<=0){
	        	sb.append(min).append("分");
	        }
	        if(sec>0&&day<=0&&hour<=0){
	        	sb.append(sec).append("秒");
	        }
	        if(sb.length()==0){
	        	sb.append("1秒");
	        }
	        //return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
	        return sb.toString();
	    }  
	  
	  public static void main(String args[]){
		  System.out.println(getDistanceTime("20170606114530",Utils.getTime()));
	  }
}
