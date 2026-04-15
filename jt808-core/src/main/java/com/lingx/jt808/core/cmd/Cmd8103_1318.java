package com.lingx.jt808.core.cmd;

import com.lingx.jt808.core.bean.Cmd8103Bean;
import com.lingx.jt808.core.utils.Utils;
/**
 * 设置IP与端口
 * @author lingx.com
 *
 */
public class Cmd8103_1318 extends Cmd8103 {

	public Cmd8103_1318(String tid,String ip,int port) {
		super(tid,getBody(ip,port));
	}

	public static Cmd8103Bean[] getBody(String ip,int port) {
		Cmd8103Bean[] array=new Cmd8103Bean[2];
		Cmd8103Bean bean=new Cmd8103Bean(0x13,Cmd8103.STRING,ip);
		array[0]=bean;
		
		bean=new Cmd8103Bean(0x18,Cmd8103.DWORD,String.valueOf(port));
		array[1]=bean;
		return array;
	}
	
	public static void main(String args[]) {
		System.out.println(Integer.parseInt("000019D0",16));
		System.out.println(new String(Utils.hexToBytes("7E8103001E0180000326001E0302000000130F3132322E3135322E3139362E3232360000001804000019D0CF7E")));
	}
}
