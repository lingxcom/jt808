package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9206 extends AbstractJT808Command {
	
	public Cmd9206(String tid,String ip,String port,String name,String password,String path,String tdh,String stime,String etime,String type,String mltype,String zcqtype){
		super(0x9206,tid,getBody(ip,port,name,password,path,tdh,stime,etime,type,mltype,zcqtype));
		//this.setName("上传设备媒体文件");
	}
	

	
	public static byte[] getBody(String ip,String port,String name,String password,String path,String tdh,String stime,String etime,String type,String mltype,String zcqtype){
		StringBuilder sb=new StringBuilder();
		String hexstring=Utils.asciiToHex(ip);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(hexstring.length()/2)), 2));
		sb.append(hexstring);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(port))), 4));

		hexstring=Utils.asciiToHex(name);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(hexstring.length()/2)), 2));
		sb.append(hexstring);
		
		hexstring=Utils.asciiToHex(password);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(hexstring.length()/2)), 2));
		sb.append(hexstring);
		
		hexstring=Utils.asciiToHex(path);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(hexstring.length()/2)), 2));
		sb.append(hexstring);
		

		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(tdh))), 2));

		sb.append(stime.substring(2));
		sb.append(etime.substring(2));
		sb.append("0000000000000000");//报警标志
		

		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(type))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(mltype))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(zcqtype))), 2));
		
		sb.append("FF");
		
		return Utils.hexToBytes(sb.toString());
	}
}
