package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9201 extends AbstractJT808Command {
	
	public Cmd9201(String tid,String ip,String port,String tdh,String type,String mltype,String zcqtype,String stime,String etime){
		super(0x9201,tid,getBody(ip,port,tdh,type,mltype,zcqtype,stime,etime));
	}
	

	
	public static byte[] getBody(String ip,String port,String tdh,String type,String mltype,String zcqtype,String stime,String etime){
		StringBuilder sb=new StringBuilder();
		String iphexstring=Utils.asciiToHex(ip);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(iphexstring.length()/2)), 2));
		sb.append(iphexstring);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(port))), 4));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 4));//不传UDP端口

		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(tdh))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(type))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(mltype))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(zcqtype))), 2));

		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 2));

		sb.append(stime.substring(2));
		sb.append(etime.substring(2));
		return Utils.hexToBytes(sb.toString());
	}
}
