package com.tracbds.core.cmd.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.cmd.Cmd8801;

public class ExtCmd8801_0 extends Cmd8801 {

	public ExtCmd8801_0(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,(byte)Integer.parseInt(params.get("p1").toString())
				,(byte)Integer.parseInt(params.get("p2").toString())
				,(byte)Integer.parseInt(params.get("p3").toString())
				,(byte)Integer.parseInt(params.get("p4").toString())
				,(byte)Integer.parseInt(params.get("p5").toString())
				,(byte)Integer.parseInt(params.get("p6").toString())
				,(byte)Integer.parseInt(params.get("p7").toString())
				,(byte)Integer.parseInt(params.get("p8").toString())
				,(byte)Integer.parseInt(params.get("p9").toString())
				,(byte)Integer.parseInt(params.get("p10").toString()),isVersion);
	}
	
	public static void main(String args[]) {
		List<Map<String,Object>> list=new ArrayList<>();
		Map<String,Object> p=new HashMap<>();
		p.put("name", "通道号");
		p.put("code", "p1");
		p.put("value", 1);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "拍摄命令");
		p.put("code", "p2");
		p.put("value", 1);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "间隔时间");
		p.put("code", "p3");
		p.put("value", 0);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "保存标志");
		p.put("code", "p4");
		p.put("value", 0);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "分辨率");
		p.put("code", "p5");
		p.put("value", 3);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "图像质量");
		p.put("code", "p6");
		p.put("value", 5);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "亮度");
		p.put("code", "p7");
		p.put("value", 0);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "对比度");
		p.put("code", "p8");
		p.put("value", 0);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "饱和度");
		p.put("code", "p9");
		p.put("value", 0);
		list.add(p);

		p=new HashMap<>();
		p.put("name", "色度");
		p.put("code", "p10");
		p.put("value", 0);
		list.add(p);
		
		System.out.println(JSON.toJSONString(list));
	}

}
