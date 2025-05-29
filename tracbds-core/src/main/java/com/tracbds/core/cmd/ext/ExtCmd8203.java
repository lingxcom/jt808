package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8203;

public class ExtCmd8203 extends Cmd8203 {

	public ExtCmd8203(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,Integer.parseInt(params.get("p1").toString()),Integer.parseInt(params.get("p2").toString()),isVersion);
	}

}
