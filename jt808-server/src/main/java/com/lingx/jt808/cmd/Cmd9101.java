package com.lingx.jt808.cmd;

import com.lingx.jt808.server.netty.Utils;

public class Cmd9101  extends AbstractJT808Command {
	
	public Cmd9101(String tid,String ip,String port,String tdh){
		super(0x9101,tid,getBody(ip,port,tdh,"0","1"));
	}
	public Cmd9101(String tid,String ip,String port,String tdh,String type,String ml){
		super(0x9101,tid,getBody(ip,port,tdh,type,ml));
	}

	
	public static byte[] getBody(String ip,String port,String tdh,String type,String ml){
		StringBuilder sb=new StringBuilder();
		String iphexstring=Utils.asciiToHex(ip);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(iphexstring.length()/2)), 2));
		sb.append(iphexstring);
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(port))), 4));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(0)), 4));//不传UDP端口
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(tdh))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(type))), 2));
		sb.append(Utils.leftAdd0(String.valueOf(Integer.toHexString(Integer.parseInt(ml))), 2));
		return Utils.hexToBytes(sb.toString());
	}
	public static void main(String args[]){
		Cmd9101 cmd=new Cmd9101("018000032600","47.100.112.218","6802","1");
		System.out.println(cmd.toMessageHexstring());
		System.out.println("7E91010016018000032600AA2F0E34372E3130302E3131322E3231381A920000010001347E");
	}
}
