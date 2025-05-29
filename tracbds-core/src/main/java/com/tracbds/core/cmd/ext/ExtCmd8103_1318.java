package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8103_1318;

public class ExtCmd8103_1318 extends Cmd8103_1318 {

	public ExtCmd8103_1318(String tid,Map<String,Object> params,boolean isVersion) {
		
		super(tid,params.get("ip").toString(),Integer.parseInt(params.get("port").toString()),isVersion);
	}

}
