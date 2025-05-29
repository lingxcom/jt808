package com.tracbds.core.utils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;



public class Utils {
	public static void main(String args[]){
		//System.out.println(Utils.hexToGBK("000004D2393939393939000036302E3139312E31362E39320000000000000000000000000000000000000000157D"));
	}
	public static final NumberFormat famatter = NumberFormat.getNumberInstance(java.util.Locale.CHINA);  
	public static final String NUMBER_STRING="1234567890";
	public static final SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");   
	/**
     * int到byte[]
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {   
          byte[] result = new byte[4];  
          //由高位到低位
          result[0] = (byte)((i >> 24) & 0xFF);
          result[1] = (byte)((i >> 16) & 0xFF);
          result[2] = (byte)((i >> 8) & 0xFF);
          result[3] = (byte)(i & 0xFF);
          return result;
    }
 
        /**
         * byte[]转int
         * @param bytes
         * @return
         */
        public static int byteArrayToInt(byte[] bytes) {
               int value=0;
               //由高位到低位
               int len=bytes.length;
               for(int i = 0; i < len; i++) {
                   int shift= (len-1-i) * 8;
                   value +=(bytes[i] & 0x000000FF) << shift;//往高位游
               }
               return value;
         }

	public static String formatDate14(String str){
		StringBuilder sb=new StringBuilder();
		sb.append(str.substring(0,4));
		sb.append("-");
		sb.append(str.substring(4,6));
		sb.append("-");
		sb.append(str.substring(6,8));
		sb.append(" ");
		sb.append(str.substring(8,10));
		sb.append(":");
		sb.append(str.substring(10,12));
		sb.append(":");
		sb.append(str.substring(12,14));
		return sb.toString();
	}
	public static boolean isNull(String temp){
		return temp==null||"".equals(temp);
	}
	public static boolean isNotNull(String temp){
		return !isNull(temp);
	}
	public static String hexToBinary(String hex){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<hex.length();i++){
			sb.append(Utils.leftAdd0(Integer.toBinaryString(Integer.parseInt(String.valueOf(hex.charAt(i)),16)), 4));
		}
		return sb.toString();
	}
	/**
	 * 时间 yyyyMMddHHmmss
	 * @return
	 */
	public static String getTime() {
		String dateTime = MessageFormat.format("{0,date,yyyyMMddHHmmss}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}
	public static String getTimeFormat() {
		String dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}
	/**
	 * 当前时间减5分钟
	 * @return
	 */
	public static String getTime_5MIN(){
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(new Date()); 
		gc.add(Calendar.MINUTE,-5); 
		return df.format(gc.getTime());
	}
	/**
	 * 时间yyMMddHHmmss
	 * @return
	 */
	public static String getTime2() {
		String dateTime = MessageFormat.format("{0,date,yyMMddHHmmss}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}
	public static String formatHexNumber(String hex,int length){
		StringBuilder sb=new StringBuilder();
		sb.append(hex);
		while(sb.length()<length){
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	public static boolean isNumber(String temp){
		boolean b=true;
		if(temp==null)return false;
		for(int i=0;i<temp.length();i++){
			if(NUMBER_STRING.indexOf(temp.charAt(i))==-1){
				b=false;
				break;
			}
		}
		return b;
	}
	public static String bytesToHex(byte[] b, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(toHex(b[i]));
		}
		return sb.toString();
	}
	public static String bytesToHex(byte[] b) {
		return bytesToHex(b,b.length);
	}

	public static final byte[] hexToBytes(String s) {
		String temp = "0123456789ABCDEF";
		byte array[] = s.toUpperCase().getBytes();
		byte[] newArr = new byte[array.length / 2];
		int index = 0;
		for (int i = 0; i < array.length; i += 2) {
			Integer t1 = temp.indexOf((char) array[i]);
			t1 = t1 << 4;
			t1 += temp.indexOf((char) array[i + 1]);
			newArr[index++] = t1.byteValue();
		}
		array = null;
		return newArr;
	}

	public static final String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
				.charAt(b & 0xf));
	}
	/**
	 * 将终端的经纬度转为经纬坐�?
	 * @param temp
	 * @param bit
	 * @return
	 */
	public static float latlng(String temp,int bit) {
		
		String d = temp.substring(bit);
		float f = Float.parseFloat(d)  / 60;//1000
		int i = Integer.parseInt(temp.substring(0,bit));
		return f + i;
	}
	/**
	 * 将服务器的经纬坐标转为经纬度
	 * @return
	 */
	public static String formatLatOrLng(String temp){
		int index=temp.indexOf(".");
		float sufix=Float.parseFloat("0."+temp.substring(index+1));
		sufix=sufix*60;
		famatter.setMaximumFractionDigits(3);
		return temp.substring(0,index)+famatter.format(sufix);
		
	} 
	/**
	 * 直线斜率公式
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double xielv(double x1,double y1,double x2,double y2){
		//System.out.println(String.format("%s,%s,%s,%s ",x1,y1,x2,y2));
		double ret=Math.atan2(y2-y1,x2-x1);
		ret=Math.toDegrees(ret);
		return 180-ret;
	}
	/**
	 * 左补�?，当s的字符个数小于bit，向左补0
	 * @param s
	 * @param bit
	 * @return
	 */
	public static String leftAdd0(String s,int bit){
		StringBuilder sb=new StringBuilder();
		
		for(int i=s.length();i<bit;i++){
			sb.append("0");
		}
		sb.append(s);
		return sb.toString();
	}
	public static String rightAdd0(String s,int bit){
		StringBuilder sb=new StringBuilder();

		sb.append(s);
		for(int i=s.length();i<bit;i++){
			sb.append("0");
		}
		return sb.toString();
	}
	/**
	 * 格式化IP:221.18.79.110转为221018079110
	 * @param ip
	 * @return
	 */
	public static String formatIP(String ip){
		StringBuilder sb=new StringBuilder();
		String[] array=ip.split("[.]");
		for(String s:array){
			sb.append(leftAdd0(s,3));
		}
		return sb.toString();
	}
	
	public static void testYh(){
		String temp="0102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435";
		
		System.out.println(Integer.toHexString(temp.length()/2));
	}
	public static void test(){
		String max="35";
		int temp=0;String hex="";
		for(int i=0;i<Integer.parseInt(max,16);i++){
			temp=i+1;
			hex=Integer.toHexString(temp);
			System.out.print(hex.length()==1?"0"+hex:hex);
		}
		System.out.println();
	}
	private static long inc=0l;

	static String getLsh(){
		String hex=Long.toHexString(( inc++)%255);
		 return hex.length()==1?"0"+hex:hex;

	}
	/**
	 * 异或
	 * @param temp
	 * @return
	 */
	public static String yh(String temp){
		//System.out.println(temp);
		byte xor=0;
		byte array[]=Utils.hexToBytes(temp);
		for(byte b :array)xor=xor ^= b;
		//System.out.println(Utils.bytesToHex(new byte[]{xor}, 1));
		return Utils.bytesToHex(new byte[]{xor}, 1);
	}
	
	public static String hexToUnicode(String temp){
		String str="";
		byte[] bs = new byte[temp.length()/2];
		  for (int i = 0; i < bs.length; i++) {
		   bs[i] = (byte) Integer.parseInt(temp.substring(i*2, i*2+2), 16);
		  }

		  try {
			str=(new String(bs, "utf-16"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return str; // 同unicode
	}
	public static void main1(String arsg[]){
		System.out.println(hexToUnicode("95FD0041003100320033003400350036"));
	}
	public static String stringToAscii(String value)  
	{  
	    StringBuilder sbu = new StringBuilder();  
	    char[] chars = value.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	    	 sbu.append(Integer.toHexString((int)chars[i]));  
	    }  
	    return sbu.toString();  
	}  
	public static String toASCIIandHex(String temp){
		StringBuilder sb=new StringBuilder();

		for(int i=0;i<temp.length();i++){
			sb.append(Integer.toHexString((int)temp.charAt(i)));
		}
		return sb.toString();
	}
	
	public static String asciiToHex(String temp){
		return toASCIIandHex(temp);
	}
	//2B38
	public static String hexToASCII(String temp){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<temp.length();i+=2){
			sb.append((char)Integer.parseInt(temp.substring(i,i+2),16));
		}
		return sb.toString();
	}

	public static String unicodeToHex(String temp){
		StringBuilder sb=new StringBuilder();
		try {
			byte buff[]=temp.getBytes("UTF-16");
			for(byte b:buff){
				sb.append(toHex(b));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString().substring(4);
	}
	
	public static String hexToGBK(String temp){
		byte array[]=Utils.hexToBytes(temp);
		try {
			return new String(array,"GBK");
		} catch (Exception e) {
			return temp;
		}
	}
	
	public static String gbkToHex(String temp){
		StringBuilder sb=new StringBuilder();
		try {
			byte buff[]=temp.getBytes("GBK");
			for(byte b:buff){
				sb.append(toHex(b));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	/*public static String hexToUnicode(String temp){
		String str="";
		byte[] bs = new byte[temp.length()/2];
		  for (int i = 0; i < bs.length; i++) {
		   bs[i] = (byte) Integer.parseInt(temp.substring(i*2, i*2+2), 16);
		  }

		  try {
			str=(new String(bs, "UTF-16"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return str; // 同unicode
	}*/
	/**
	 * 取二进制位数值
	 * @param num 原始数据
	 * @param offset 偏移位
	 * @param bit 位数
	 * @return
	 */
	public static long getBitValue(long num,int offset,int bit) {
		long temp=1;
		temp=temp<<offset;
		long sum=temp;
		for(int i=1;i<bit;i++) {
			sum+=(temp<<i);
		}
		
		return (num&sum)>>offset;
	}
	public static String getRandomString(int bit){
		Random random=new Random();
		StringBuilder sb=new StringBuilder();
		String temp="1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		int max=temp.length();
		for(int i=0;i<bit;i++){
			int t=random.nextInt(max);
			sb.append(temp.charAt(t));
		}
		return sb.toString();
	}

	public static String getRandomNumber(int bit){
		Random random=new Random();
		StringBuilder sb=new StringBuilder();
		String temp="1234567890";
		int max=10;
		for(int i=0;i<bit;i++){
			int t=random.nextInt(max);
			sb.append(temp.charAt(t));
		}
		return sb.toString();
	}
}
