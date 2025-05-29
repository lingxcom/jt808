package com.tracbds.core.cmd.ext;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.cmd.Cmd8300;

public class ExtCmd8300 extends Cmd8300 {

	public ExtCmd8300(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,getIntBy(params.get("p1").toString()),params.get("p2").toString(),isVersion);

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
