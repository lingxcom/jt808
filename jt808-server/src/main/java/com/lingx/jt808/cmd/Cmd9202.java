package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9202 extends AbstractJT808Command {
	
	public Cmd9202(String tid,int tdh,int type,int param,String time){
		super(0x9202,tid,getBody(tdh,type,param,time));
	}
	
	public Cmd9202(String tid,String tdh) {
		this(tid,Integer.parseInt(tdh),2,0,"210528145022");
	}

	
	public static byte[] getBody(int tdh,int type,int param,String time){
		StringBuilder sb=new StringBuilder();
		sb.append(Utils.leftAdd0(Integer.toHexString(tdh), 2));
		sb.append(Utils.leftAdd0(Integer.toHexString(type), 2));
		sb.append(Utils.leftAdd0(Integer.toHexString(param), 2));
		sb.append(time.substring(2));
		return Utils.hexToBytes(sb.toString());
	}
}
