package com.lingx.jt808.core.cmd.ext;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.cmd.Cmd8300;

public class ExtCmd8300UTF8 extends Cmd8300 {

	public ExtCmd8300UTF8(String tid,Map<String,Object> params) {
		super(tid,getIntBy(params.get("p1").toString()),params.get("p2").toString(),"UTF-8");

	}
	
	public static int getIntBy(String temp) {
		int sum=0;
		List<Integer> list=(List<Integer>)JSON.parse(temp);
		//if(list.size()==0)sum= 255;
		for(int bit:list) {
			sum=sum+(1 << bit);
		}
		return sum;
	}

}
