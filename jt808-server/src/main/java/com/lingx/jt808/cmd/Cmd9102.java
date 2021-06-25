package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9102 extends AbstractJT808Command {
	
	public Cmd9102(String tid,String tdh){
		super(0x9102,tid,getBody(tdh));
	}
	

	
	public static byte[] getBody(String tdh){
		StringBuilder sb=new StringBuilder();
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(tdh))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(1)), 2));
		return Utils.hexToBytes(sb.toString());
	}
}
