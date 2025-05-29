package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8400;

public class ExtCmd8400 extends Cmd8400 {

	public ExtCmd8400(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,Integer.parseInt(params.get("p1").toString()),params.get("p2").toString(),isVersion);

	}

}
