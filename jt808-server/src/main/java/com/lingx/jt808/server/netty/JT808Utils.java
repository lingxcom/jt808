package com.lingx.jt808.server.netty;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.bean.RetBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class JT808Utils {
	public static void main(String args[]) {
		ByteBuf buff=decode(Utils.hexToBytes("7E00020000013800138999007D02467E"));
		byte array[]=new byte[buff.readableBytes()];
		buff.readBytes(array);
		System.out.println(Utils.bytesToHex(array));
	}

	public static final boolean check(byte bytes[], byte check, int length, boolean isFB, boolean isVersion) {
		ByteBuf buff= decode(bytes);//Unpooled.copiedBuffer(bytes) // new ByteBuf(); //decode(bytes)
		buff.readByte();
		length += 12;
		if (isFB)
			length += 4;
		if (isVersion)
			length += 5;
		byte xor = 0;
		for (int i = 0; i < length; i++) {
			xor = xor ^= buff.readByte();
		}
		return xor == check;
	}

	public static final ByteBuf encode(ByteBuf byteBuf) {
		int len=byteBuf.readableBytes();
		ByteBuf buf = Unpooled.buffer();
		for(int i=0;i<len;i++) {
			byte b=byteBuf.getByte(i);
			if (i == 0 || i == len-1 ){
				buf.writeByte(b);
			} else if (b == 0x7D) {
				buf.writeByte(0x7D);
				buf.writeByte(0x01);
			} else if( b == 0x7E ){
				buf.writeByte(0x7D);
				buf.writeByte(0x02);
			} else {
				buf.writeByte(b);
			}
		}
		byteBuf.release();
		return buf;
	}
	public static final ByteBuf decode(byte[] bytes) {
		int len = bytes.length;
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < len; i++) {
			if (bytes[i] == 0x7d && bytes[i + 1] == 0x01) {
				buf.writeByte(0x7d);
				i++;
			} else if (bytes[i] == 0x7d && bytes[i + 1] == 0x02) {
				buf.writeByte(0x7e);
				i++;
			} else {
				buf.writeByte(bytes[i]);
			}
		}
		bytes=null;
		return buf;
	}
	
	/**
	 * 计算距离(米)
	 <param name="n1">第一点的纬度坐标</param>latitude1
	 <param name="e1">第一点的经度坐标</param>longititude1
	 <param name="n2">第二点的纬度坐标</param>latitude2
	 <param name="e2">第二点的经度坐标</param>longititude2
	 * @param n1
	 * @param e1
	 * @param n2
	 * @param e2
	 * @return
	 */
	public static double distance(double n1, double e1, double n2, double e2) {
		double jl_jd = 102834.74258026089786013677476285;
		double jl_wd = 111712.69150641055729984301412873;
		double b = Math.abs((e1 - e2) * jl_jd);
		double a = Math.abs((n1 - n2) * jl_wd);
		return Math.sqrt((a * a + b * b));

	}
	
	
	
	/**
	 * 
	 <param name="n1">第一点的纬度坐标</param>latitude1
	 <param name="e1">第一点的经度坐标</param>longititude1
	 <param name="n2">第二点的纬度坐标</param>latitude2
	 <param name="e2">第二点的经度坐标</param>longititude2
	 */
	public static double distance(String t1,String t2,String t3,String t4){
		return distance(Double.parseDouble(t1),Double.parseDouble(t2),Double.parseDouble(t3),Double.parseDouble(t4));
	}
	
	/**
	 * 取方向
	 * @param obj
	 * @return
	 */
	public static String getDirection(Object obj) {
		int index = 0;
		int number = Integer.parseInt(obj.toString());
		int nums[] = new int[] { 0, 45, 90, 135, 180, 225, 270, 315, 360 };
		String array[] = new String[] { "北", "东北", "东", "东南", "南", "西南", "西",
				"西北", "北" };
		int min = 1000;
		for (int i = 0; i < nums.length; i++) {
			int t = Math.abs(number - nums[i]);
			if (t < min) {
				min = t;
				index = i;
			}
		}
		return array[index];
	}

	public static String getResKey(String tid,int msgId,int msgSn) {
		return String.format("%s_%s_%s", tid,msgId,msgSn);
	}
	public static RetBean getResBean(int code) {
		return new RetBean(code);
	}
	public static RetBean getResBean(int code,List<Map<String,Object>> params) {
		return new RetBean(code,params);
	}
}
