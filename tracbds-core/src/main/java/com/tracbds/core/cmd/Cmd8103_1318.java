package com.tracbds.core.cmd;

import com.tracbds.core.bean.Cmd8103Bean;
/**
 * 设置IP与端口
 * @author lingx.com
 *
 */
public class Cmd8103_1318 extends Cmd8103 {

	public Cmd8103_1318(String tid,String ip,int port,boolean isVersion) {
		super(tid,getBody(ip,port),isVersion);
	}

	public static Cmd8103Bean[] getBody(String ip,int port) {
		Cmd8103Bean[] array=new Cmd8103Bean[2];
		Cmd8103Bean bean=new Cmd8103Bean(0x13,Cmd8103.STRING,ip);
		array[0]=bean;
		
		bean=new Cmd8103Bean(0x18,Cmd8103.DWORD,String.valueOf(port));
		array[1]=bean;
		return array;
	}
}
