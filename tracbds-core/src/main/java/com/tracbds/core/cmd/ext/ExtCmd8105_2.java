package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8105;

public class ExtCmd8105_2 extends Cmd8105{

	public ExtCmd8105_2(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid, 2, params.get("p1").toString(),isVersion);
	}

}
