package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9205 extends AbstractJT808Command {
	
	public Cmd9205(String tid,String tdh,String stime,String etime,String type,String ml,String zcqtype){
		super(0x9205,tid,getBody(tdh,stime,etime,type,ml,zcqtype));
		//this.setName("查询设备媒体记录");
	}
	

	
	public static byte[] getBody(String tdh,String stime,String etime,String type,String ml,String zcqtype){
		StringBuilder sb=new StringBuilder();
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(tdh))), 2));
		sb.append(stime.substring(2));
		sb.append(etime.substring(2));
		sb.append("0000000000000000");//报警标志
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(type))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(ml))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(zcqtype))), 2));
		return Utils.hexToBytes(sb.toString());
	}
}
